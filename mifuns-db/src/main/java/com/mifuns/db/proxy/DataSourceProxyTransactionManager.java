package com.mifuns.db.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 *
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/1/9 </p>
 * <p>Time: 10:47 </p>
 * <p>Version: 1.0.1 </p>
 *
 * 1.事务管理由 TransactionInterceptor 拦截，执行invoke
 * 2.调用 TransactionAspectSupport#invokeWithinTransaction 实现环绕通知
 * 3.getTransaction>>doGetTransaction>>isExistingTransaction{
 *      return handleExistingTransaction
 * }
 * 4.if>>[PROPAGATION_REQUIRED || PROPAGATION_REQUIRES_NEW  || PROPAGATION_NESTED]{
 *      doBegin 在此将创建Connection,如果Connection 为新创建，绑定到TransactionSynchronizationManager#bindResource(DataSource, ConnectionHolder);
 *      prepareTransactionStatus
 *      return
 * }
 * 5.prepareTransactionStatus 将当前事务事务绑定到本地线程
 * 如果第四步没有执行，Connection 将由 DataSourceUtils#doGetConnection 创建，绑定到TransactionSynchronizationManager#bindResource(DataSource, ConnectionHolder)
 * 6.执行ReflectiveMethodInvocation#proceed 如果抛出异常执行completeTransactionAfterThrowing处理
 * 7.cleanupTransactionInfo
 * 8.commitTransactionAfterReturning>TransactionManager#commit
 *
 */
public class DataSourceProxyTransactionManager extends DataSourceRoutingTransactionManager {

    private static Logger logger = LoggerFactory.getLogger(DataSourceProxyTransactionManager.class);

    protected Object doGetTransaction() {
        logger.debug(">>> 获得事务.");
        DataSourceProxyManager.setSuspendedTransactionActive(getDataSource(),false);
        return super.doGetTransaction();
    }
    @Override
    protected boolean isExistingTransaction(Object transaction) {
        boolean isExisting = super.isExistingTransaction(transaction);
        if(isExisting) {
            logger.debug(">>> 存在事务.");
        }
        return isExisting;
    }
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        logger.info(">>> 开始事务.");
        logger.debug(">>> TransactionDefinition[name = {}, level = {}, propagation = {}, isReadOnly = {}]"
                ,definition.getName()
                ,getIsolationLevelName(definition.getIsolationLevel())
                ,getPropagationBehaviorName(definition.getPropagationBehavior())
                ,definition.isReadOnly());
        determineDataSource(definition);
        super.doBegin(transaction, definition);
    }
    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        logger.debug(">>> 提交事务.{}",status);
        boolean isSuspendedActive = isSuspendedActive();
        if(isSuspendedActive){
            logger.debug(">>> 存在挂起事务，暂不提交.");
            return;
        }
        super.doCommit(status);
        logger.debug(">>> 提交挂起事务.");
        DataSourceProxyManager.commitMasterConnection(getDataSource());
        DataSourceProxyManager.commitSlaveConnection(getDataSource());
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        logger.debug(">>> 回滚事务.{}",status);
        boolean isSuspendedActive = isSuspendedActive();
        if(isSuspendedActive){
            logger.debug(">>> 存在挂起事务，暂不回滚.");
            return;
        }
        super.doRollback(status);
        logger.debug(">>> 回滚挂起事务.");
        DataSourceProxyManager.rollbackMasterConnection(getDataSource());
        DataSourceProxyManager.rollbackSlaveConnection(getDataSource());
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        logger.debug(">>> 恢复事务. transaction = {}, suspendedResources = {}", transaction,suspendedResources);
        if(DataSourceProxyManager.isSuspendedTransactionActive(getDataSource())){
            DataSourceProxyManager.setSuspendedTransactionActive(getDataSource(), false);
        }
        super.doResume(transaction, suspendedResources);
    }

    @Override
    protected Object doSuspend(Object transaction) {
        logger.debug(">>> 挂起事务. {}",transaction);
        DataSourceProxyManager.setSuspendedTransactionActive(getDataSource(), true);
        return super.doSuspend(transaction);
    }
    /**
     * Initialize transaction synchronization as appropriate.
     */
    @Override
    protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition) {
        logger.info(">>> DefaultTransactionStatus[newTransaction = {}, newSynchronization = {}, readOnly = {}, debug = {}, point = {}]",
                status.isNewTransaction(),status.isNewSynchronization(),status.isReadOnly(), status.isDebug(),definition.getName()
        );
        determineDataSource(definition);
        super.prepareSynchronization(status, definition);
    }
    private void determineDataSource(TransactionDefinition definition){
        if (definition.isReadOnly()) {
            DataSourceProxyManager.markSlave(getDataSource());
            logger.info(">>> 选择从库 because readOnly is {} in [{}]", definition.isReadOnly(), definition.getName());
        } else {
            DataSourceProxyManager.markMaster(getDataSource());
            logger.info(">>> 选择主库 because readOnly is {} in [{}]", definition.isReadOnly(), definition.getName());
        }
    }
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        logger.debug(">>> 清理事务 DataSourceProxyManager holder by {}", DataSourceProxyManager.getType(getDataSource()));
        DataSourceProxyManager.rest(getDataSource());
        boolean isSuspendedActive = isSuspendedActive();
        if(isSuspendedActive){
            logger.debug(">>> 存在挂起事务，暂不清理..");
        }
        super.doCleanupAfterCompletion(transaction);
        if(!isSuspendedActive){
            logger.debug(">>> 清理挂起事务.");
            DataSourceProxyManager.cleanupSlaveConnection(getDataSource());
            DataSourceProxyManager.cleanupMasterConnection(getDataSource());
            DataSourceProxyManager.cleanupSuspendedConnection(getDataSource());
        }
    }

    private String getIsolationLevelName(int code){
        switch (code){
            case TransactionDefinition.ISOLATION_DEFAULT:
                return "ISOLATION_DEFAULT";
            case TransactionDefinition.ISOLATION_READ_COMMITTED:
                return "ISOLATION_READ_COMMITTED";
            case TransactionDefinition.ISOLATION_READ_UNCOMMITTED:
                return "ISOLATION_READ_UNCOMMITTED";
            case TransactionDefinition.ISOLATION_REPEATABLE_READ:
                return "ISOLATION_REPEATABLE_READ";
            case TransactionDefinition.ISOLATION_SERIALIZABLE:
                return "ISOLATION_SERIALIZABLE";
            default: return "ISOLATION_DEFAULT";
        }
    }
    private String getPropagationBehaviorName(int code){
        switch (code){
            case TransactionDefinition.PROPAGATION_MANDATORY:
                return "PROPAGATION_MANDATORY";
            case TransactionDefinition.PROPAGATION_NESTED:
                return "PROPAGATION_NESTED";
            case TransactionDefinition.PROPAGATION_NEVER:
                return "PROPAGATION_NEVER";
            case TransactionDefinition.PROPAGATION_NOT_SUPPORTED:
                return "PROPAGATION_NOT_SUPPORTED";
            case TransactionDefinition.PROPAGATION_REQUIRED:
                return "PROPAGATION_REQUIRED";
            case TransactionDefinition.PROPAGATION_REQUIRES_NEW:
                return "PROPAGATION_REQUIRES_NEW";
            case TransactionDefinition.PROPAGATION_SUPPORTS:
                return "PROPAGATION_SUPPORTS";
            default:
                return "PROPAGATION_SUPPORTS";
        }
    }

    /**
     * 事务管理器描述
     */
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
