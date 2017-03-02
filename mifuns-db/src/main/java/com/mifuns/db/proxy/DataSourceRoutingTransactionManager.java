package com.mifuns.db.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/8/5 </p>
 * <p>Time: 15:54 </p>
 * <p>Version: 1.0.1 </p>
 */
public class DataSourceRoutingTransactionManager extends AbstractRoutingTransactionManager implements ResourceTransactionManager, InitializingBean {

    protected static final Logger logger = LoggerFactory.getLogger(DataSourceRoutingTransactionManager.class);

    private DataSource dataSource;


    /**
     * Create a new DataSourceRoutingTransactionManager instance.
     * A DataSource has to be set to be able to use it.
     *
     * @see #setDataSource
     */
    public DataSourceRoutingTransactionManager() {
        setNestedTransactionAllowed(true);
    }

    /**
     * Create a new DataSourceRoutingTransactionManager instance.
     *
     * @param dataSource JDBC DataSource to manage transactions for
     */
    public DataSourceRoutingTransactionManager(DataSource dataSource) {
        this();
        setDataSource(dataSource);
        afterPropertiesSet();
    }

    /**
     * Set the JDBC DataSource that this instance should manage transactions for.
     * <p>This will typically be a locally defined DataSource, for example a
     * Jakarta Commons DBCP connection pool. Alternatively, you can also drive
     * transactions for a non-XA J2EE DataSource fetched from JNDI. For an XA
     * DataSource, use JtaTransactionManager.
     * <p>The DataSource specified here should be the target DataSource to manage
     * transactions for, not a TransactionAwareDataSourceProxy. Only data access
     * code may work with TransactionAwareDataSourceProxy, while the transaction
     * manager needs to work on the underlying target DataSource. If there's
     * nevertheless a TransactionAwareDataSourceProxy passed in, it will be
     * unwrapped to extract its target DataSource.
     * <p><b>The DataSource passed in here needs to return independent Connections.</b>
     * The Connections may come from a pool (the typical case), but the DataSource
     * must not return thread-scoped / request-scoped Connections or the like.
     *
     * @see TransactionAwareDataSourceProxy
     * @see org.springframework.transaction.jta.JtaTransactionManager
     */
    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            // If we got a TransactionAwareDataSourceProxy, we need to perform transactions
            // for its underlying target DataSource, else data access code won't see
            // properly exposed transactions (i.e. transactions for the target DataSource).
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }
    }

    /**
     * Return the JDBC DataSource that this instance manages transactions for.
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
    }


    public Object getResourceFactory() {
        return getDataSource();
    }

    @Override
    protected Object doGetTransaction() {
        DataSourceTransactionObject txObject = new DataSourceTransactionObject();
        txObject.setSavepointAllowed(isNestedTransactionAllowed());
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(this.dataSource);
        txObject.setConnectionHolder(conHolder, false);
        return txObject;
    }

    @Override
    protected boolean isExistingTransaction(Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        //txObject.getConnectionHolder().isTransactionActive());
        return (txObject.getConnectionHolder() != null && isTransactionActive(txObject.getConnectionHolder()));
    }

    /**
     * This implementation sets the isolation level but ignores the timeout.
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        Connection con = null;
        boolean isSuspendedTransactionActive = false;
        try {
            if (txObject.getConnectionHolder() == null || txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
                Connection newCon = this.dataSource.getConnection();
                if (logger.isDebugEnabled()) {
                    logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
                }
                //将挂起事务的连接存入本地线程
                isSuspendedTransactionActive = DataSourceProxyManager.isSuspendedTransactionActive(getDataSource());
                txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
            }

            txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
            con = txObject.getConnectionHolder().getConnection();

            Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
            txObject.setPreviousIsolationLevel(previousIsolationLevel);

            // Switch to manual commit if necessary. This is very expensive in some JDBC drivers,
            // so we don't want to do it unnecessarily (for example if we've explicitly
            // configured the connection pool to set it already).
            if (con.getAutoCommit()) {
                txObject.setMustRestoreAutoCommit(true);
                if (logger.isDebugEnabled()) {
                    logger.debug("Switching JDBC Connection [" + con + "] to manual commit");
                }
                con.setAutoCommit(false);
            }
            setTransactionActive(txObject.getConnectionHolder(), true);
//            txObject.getConnectionHolder().setTransactionActive(true);

            int timeout = determineTimeout(definition);
            if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
                txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
            }

            // Bind the session holder to the thread.
            if (txObject.isNewConnectionHolder()) {
                TransactionSynchronizationManager.bindResource(getDataSource(), txObject.getConnectionHolder());
            }
            //将挂起事务的连接存入本地线程
            if(isSuspendedTransactionActive){
                DataSourceProxyManager.setCurrentSuspendedConnection(getDataSource(), txObject.getConnectionHolder().getConnection());
            }
        } catch (Throwable ex) {
            DataSourceUtils.releaseConnection(con, this.dataSource);
            throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", ex);
        }
    }

    void setTransactionActive(ConnectionHolder connectionHolder, boolean active) {
        try {
            Method method = connectionHolder.getClass().getDeclaredMethod("setTransactionActive",boolean.class);
            method.setAccessible(true);
            method.invoke(connectionHolder,new Object[]{active});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isTransactionActive(ConnectionHolder connectionHolder) {
        try {
            Method method = connectionHolder.getClass().getDeclaredMethod("isTransactionActive");
            method.setAccessible(true);
            return (boolean) method.invoke(connectionHolder,new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected Object doSuspend(Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        txObject.setConnectionHolder(null);
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.unbindResource(this.dataSource);
        return conHolder;
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        ConnectionHolder conHolder = (ConnectionHolder) suspendedResources;
        TransactionSynchronizationManager.bindResource(this.dataSource, conHolder);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (status.isDebug()) {
            logger.debug("提交 JDBC transaction on Connection [" + con + "]");
        }
        try {
//            con.commit();
            DataSourceProxyManager.commit(con);
        } catch (SQLException ex) {
            throw new TransactionSystemException("Could not commit JDBC transaction", ex);
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (status.isDebug()) {
            logger.debug("回滚 JDBC transaction on Connection [" + con + "]");
        }
        try {
//            con.rollback();
            DataSourceProxyManager.rollback(con);
        } catch (SQLException ex) {
            throw new TransactionSystemException("Could not roll back JDBC transaction", ex);
        }
    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        if (status.isDebug()) {
            logger.debug("Setting JDBC transaction [" + txObject.getConnectionHolder().getConnection() +
                    "] rollback-only");
        }
        txObject.setRollbackOnly();
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;

        // Remove the connection holder from the thread, if exposed.
        if (txObject.isNewConnectionHolder()) {
            TransactionSynchronizationManager.unbindResource(this.dataSource);
        }

        // Reset connection.
        Connection con = txObject.getConnectionHolder().getConnection();
        try {
            if (txObject.isMustRestoreAutoCommit() && !isSuspendedActive()) {
                con.setAutoCommit(true);
            }
            DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
        } catch (Throwable ex) {
            logger.debug("Could not reset JDBC Connection after transaction", ex);
        }
        //是一个新事物，并且是不是一个挂起的事务
        if (txObject.isNewConnectionHolder() && !isSuspendedActive()) {
            if (logger.isDebugEnabled()) {
                logger.debug("释放 JDBC Connection [" + con + "] after transaction");
            }
            DataSourceUtils.releaseConnection(con, this.dataSource);
        }

        txObject.getConnectionHolder().clear();
    }
    protected boolean isSuspendedActive(){
        return DataSourceProxyManager.isSuspendedTransactionActive(getDataSource());
    }

    /**
     * DataSource transaction object, representing a ConnectionHolder.
     * Used as transaction object by DataSourceTransactionManager.
     */
    protected static class DataSourceTransactionObject extends JdbcTransactionObjectSupport {

        private boolean newConnectionHolder;

        private boolean mustRestoreAutoCommit;

        public void setConnectionHolder(ConnectionHolder connectionHolder, boolean newConnectionHolder) {
            super.setConnectionHolder(connectionHolder);
            this.newConnectionHolder = newConnectionHolder;
        }

        public boolean isNewConnectionHolder() {
            return this.newConnectionHolder;
        }

        public void setMustRestoreAutoCommit(boolean mustRestoreAutoCommit) {
            this.mustRestoreAutoCommit = mustRestoreAutoCommit;
        }

        public boolean isMustRestoreAutoCommit() {
            return this.mustRestoreAutoCommit;
        }

        public void setRollbackOnly() {
            getConnectionHolder().setRollbackOnly();
        }

        public boolean isRollbackOnly() {
            return getConnectionHolder().isRollbackOnly();
        }
    }

}
