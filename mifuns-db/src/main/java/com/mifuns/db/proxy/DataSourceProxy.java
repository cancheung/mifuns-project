package com.mifuns.db.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/1/9 </p>
 * <p>Time: 10:47 </p>
 * <p>Version: 1.0.1 </p>
 */
public class DataSourceProxy extends AbstractDataSource implements InitializingBean{

    private static Logger logger = LoggerFactory.getLogger(DataSourceProxy.class);

    private DataSource[] masters;
    private DataSource[] slaves;
    private int slavesCount = 0;
    private int mastersCount = 0;
    private AtomicInteger slaveRequest = new AtomicInteger(0);
    private AtomicInteger masterRequest = new AtomicInteger(0);
    private final Object slave_monitor = new Object();
    private final Object master_monitor = new Object();
    /** 最大请求次数 **/
    public static final int MAX_REQUEST = (Integer.MAX_VALUE-3);

    public void setMaster(DataSource ds) {
        Assert.notNull(ds, "setMaster arg ds must not null.");
        int count = getMastersCount();
        DataSource[] result = new DataSource[count + 1];
        if(count > 0){
            System.arraycopy(this.masters, 0, result, 0, count);
        }
        result[count] = ds;
        this.masters = result;
    }

    public DataSource[] getMasters() {
        if(this.masters == null){
            this.masters = new DataSource[0];
        }
        return masters;
    }

    public void setMasters(List<DataSource> list) {
        Assert.notNull(list, "setMasters arg list must not null.");
        int count = getMastersCount();
        DataSource[] result = new DataSource[count +list.size()];
        if(count > 0) {
            System.arraycopy(this.masters, 0, result, 0, count);
        }
        for(DataSource ds : list){
            result[count] = ds;
            count++;
        }
        this.masters = result;
    }

    public void setSlave(DataSource ds) {
        Assert.notNull(ds, "setSlave arg ds must not null.");
        int count = getSlavesCount();
        DataSource[] result = new DataSource[count + 1];
        if(count > 0){
            System.arraycopy(this.slaves,0,result,0,count);
        }
        result[count] = ds;
        this.slaves = result;
    }
    public void setSlaves(List<DataSource> list) {
        Assert.notNull(list, "setSlaves arg list must not null.");
        int count = getSlavesCount();
        DataSource[] result = new DataSource[count + list.size()];
        if(count > 0) {
            System.arraycopy(this.slaves, 0, result, 0, count);
        }
        for(DataSource ds : list){
            result[count] = ds;
            count++;
        }
        this.slaves = result;
    }
    public DataSource[] getSlaves() {
        if(this.slaves == null){
            slaves = new DataSource[0];
        }
        return slaves;
    }
    private DataSource determineDataSource(){
        if(DataSourceProxyManager.isNone(this)){
            logger.debug(">>> STATUS isNone current determine db is master");
            return determineMasterDataSource();
        }
        if(DataSourceProxyManager.isMaster(this)){
            logger.debug(">>> STATUS isMaster current determine db is master");
            return determineMasterDataSource();
        }
        logger.debug(">>> STATUS isSlave current determine db is slave");
        return determineSlaveDataSource();
    }

    /**
     * 主库轮询选择
     * @return DataSource
     */
    private DataSource determineMasterDataSource() {
        int index = masterRequest.incrementAndGet() % mastersCount;
        if(index == MAX_REQUEST){
            logger.info("Master Request count rest to 0.");
            masterRequest.set(0);
        }
        if(index < 0) index = - 0;
        DataSource ds;
        synchronized (master_monitor){
            ds = this.masters[index];
        }
        logger.info(">>> STATUS isMaster[{}] current determine db is masters request count {}",index, masterRequest.get());
        return ds;
    }

    /**
     * 从库轮询选择
     * @return DataSource
     */
    private DataSource determineSlaveDataSource() {
        int index = slaveRequest.incrementAndGet() % slavesCount;
        if(index == MAX_REQUEST){
            logger.info("Slave Request count rest to 0.");
            slaveRequest.set(0);
        }
        if(index < 0) index = - 0;
        DataSource ds;
        synchronized (slave_monitor){
            ds = this.slaves[index];
        }
        logger.info(">>> STATUS isSlave[{}] current determine db is slaves request count {}",index, slaveRequest.get());
        return ds;
    }

    @Override
    public Connection getConnection() throws SQLException {
        logger.debug("Enter");
        Connection conn = null;
        boolean isNewConnection = false;
        if (DataSourceProxyManager.isSlave(this)) {
            conn = DataSourceProxyManager.getSlaveConnection(this);
            if (null == conn) {
                conn = determineDataSource().getConnection();
                isNewConnection = true;
            }
            if (conn.isClosed()) {
                logger.info("conn is closed by {}", conn, DataSourceProxyManager.getType(this));
                conn = determineDataSource().getConnection();
                isNewConnection = true;
            }
            if (isNewConnection) {
                DataSourceProxyManager.setSlaveConnection(this, conn);
            }
        } else {
            //先从本地线程获取Master Connection
            conn = DataSourceProxyManager.getMasterConnection(this);
            if (null == conn) {
                conn = determineDataSource().getConnection();
                isNewConnection = true;
            }
            if (conn.isClosed()) {
                logger.info("conn is closed by {}", conn, DataSourceProxyManager.getType(this));
                conn = determineDataSource().getConnection();
                isNewConnection = true;
            }
            if (isNewConnection) {
                if(DataSourceProxyManager.isMaster(this)) {
                    if (conn.getAutoCommit()) {
                        conn.setAutoCommit(false);
                    }
                }
                DataSourceProxyManager.setMasterConnection(this, conn);
            }
        }
        logger.info("conn = {} isNew [{}] by {}", conn, isNewConnection, DataSourceProxyManager.getType(this));
        return conn;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineDataSource().getConnection(username,password);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.masters, "property master/masters is required");
        Assert.notNull(this.slaves, "property slave/slaves is required");
        this.slavesCount = this.slaves.length;
        this.mastersCount = this.masters.length;
    }
    private int getMastersCount(){
        if(this.masters == null){
            return 0;
        }
        return this.masters.length;
    }
    private int getSlavesCount(){
        if(this.slaves == null){
            return 0;
        }
        return this.slaves.length;
    }
}
