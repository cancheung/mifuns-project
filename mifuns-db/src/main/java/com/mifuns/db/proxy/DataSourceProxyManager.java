package com.mifuns.db.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.support.ResourceHolderSupport;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/1/9 </p>
 * <p>Time: 10:47 </p>
 * <p>Version: 1.0.1 </p>
 */
public class DataSourceProxyManager {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceProxyManager.class);


    private enum DataSourceType{
        MASTER,SLAVE,ALWAYS_MASTER
    }
    /** holder Current Thread connection status **/
    private static ThreadLocal<Map<Object, DataSourceType>> holder =
            new NamedThreadLocal<Map<Object, DataSourceType>>("Current connection status");

    /** holder Current Thread master connection **/
    private static ThreadLocal<Map<Object, Connection>> masterConnectionHolder =
            new NamedThreadLocal<Map<Object, Connection>>("Current master transaction connection");

    /** holder Current Thread slave connection **/
    private static ThreadLocal<Map<Object, Connection>> slaveConnectionHolder =
            new NamedThreadLocal<Map<Object, Connection>>("Current slave transaction connection");

    private static final ThreadLocal<Map<Object, Boolean>> currentSuspendedTransactionActive =
            new NamedThreadLocal<Map<Object, Boolean>>("Current suspended transaction active");

    private static final ThreadLocal<Map<Object, Connection>> currentSuspendedConnection =
            new NamedThreadLocal<Map<Object, Connection>>("Current suspended transaction connection");

    public static boolean isSlave(Object key){
        Map<Object, DataSourceType> map = holder.get();
        if (map == null) {
            return false;
        }
        return DataSourceType.SLAVE == map.get(key);
    }
    public static boolean isMaster(Object key){
        Map<Object, DataSourceType> map = holder.get();
        if (map == null) {
            return false;
        }
        return DataSourceType.MASTER == map.get(key) ||  DataSourceType.ALWAYS_MASTER == map.get(key);
    }
    public static boolean isNone(Object key){
        Map<Object, DataSourceType> map = holder.get();
        return (map == null) || (null == map.get(key));
    }
    public static void markSlave(Object key) {
        Map<Object, DataSourceType> map = holder.get();
        if (map == null) {
            map = new HashMap<Object, DataSourceType>();
            holder.set(map);
        }
        Object oldValue = map.put(key, DataSourceType.SLAVE);
    }
    public static void markMaster(Object key){
        Map<Object, DataSourceType> map = holder.get();
        if (map == null) {
            map = new HashMap<Object, DataSourceType>();
            holder.set(map);
        }
        Object oldValue = map.put(key, DataSourceType.MASTER);
    }
    public static void rest(Object key){
        Map<Object, DataSourceType> map = holder.get();
        if((null != map) && (!map.isEmpty())) {
            map.remove(key);
        }else{
            holder.set(null);
        }
    }
    public static void alwaysMaster(Object key){
        Map<Object, DataSourceType> map = holder.get();
        if (map == null) {
            map = new HashMap<Object, DataSourceType>();
            holder.set(map);
        }
        Object oldValue = map.put(key, DataSourceType.ALWAYS_MASTER);
    }

    public static DataSourceType getType(Object key) {
        Map<Object, DataSourceType> map = holder.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static Connection getMasterConnection(Object key) {
        Map<Object, Connection> map = masterConnectionHolder.get();
        // set ThreadLocal Map if none found
        if (map == null) {
            return null;
        }else{
            return map.get(key);
        }
    }
    public static void setMasterConnection(Object key, Connection conn) {
        Map<Object, Connection> map = masterConnectionHolder.get();
        // set ThreadLocal Map if none found
        if (map == null) {
            map = new HashMap<Object, Connection>();
            masterConnectionHolder.set(map);
        }
        Object oldValue = map.put(key, conn);
        logger.trace("保存Master连接 [{}] for key [{}] in map {}.", conn, key, map);
    }
    public static Connection getSlaveConnection(Object key) {
        Map<Object, Connection> map = slaveConnectionHolder.get();
        // set ThreadLocal Map if none found
        if (map == null) {
            return null;
        }else{
            return map.get(key);
        }
    }
    public static void setSlaveConnection(Object key,Connection conn) {
        Map<Object, Connection> map = slaveConnectionHolder.get();
        // set ThreadLocal Map if none found
        if (map == null) {
            map = new HashMap<Object, Connection>();
            slaveConnectionHolder.set(map);
        }
        Object oldValue = map.put(key, conn);
        logger.trace("保存Slave连接 [{}] for key [{}] in map {}.", conn, key, map);
    }

    public static void setSuspendedTransactionActive(Object key, boolean active) {
        Map<Object, Boolean> map = currentSuspendedTransactionActive.get();
        // set ThreadLocal Map if none found
        if (map == null) {
            map = new HashMap<Object, Boolean>();
            currentSuspendedTransactionActive.set(map);
        }
        Object oldValue = map.put(key, (active ? Boolean.TRUE : null));
        logger.trace("挂起事务活动 [{}] for key [{}] in map {}.", active,key, map);
    }
    public static boolean isSuspendedTransactionActive(Object key) {
        Map<Object, Boolean> map = currentSuspendedTransactionActive.get();
        boolean isAvtive = false;
        if(null == map){
            isAvtive =  false;
        }else{
            isAvtive =  (map.get(key) != null);
        }
        logger.debug("挂起事务活动 is [{}] for key [{}] in map {}.", isAvtive,key, map);
        return isAvtive;
    }
    public static boolean hasSuspendedTransactionActive(){
        Map<Object, Boolean> map = currentSuspendedTransactionActive.get();
        if(null == map){
            return false;
        }else{
            for(Map.Entry<Object, Boolean> e : map.entrySet()){
                if(e.getValue() != null){
                    //logger.debug("存在挂起事务活动 is [{}] for key [{}] in map {}.", true, map);
                    return true;
                }
            }
        }
        return false;
    }
    public static void setCurrentSuspendedConnection(Object key, Connection conn) {
        Map<Object, Connection> map = currentSuspendedConnection.get();
        // set ThreadLocal Map if none found
        if (map == null) {
            map = new HashMap<Object, Connection>();
            currentSuspendedConnection.set(map);
        }
        Object oldValue = map.put(key, conn);
        logger.trace("挂起事务连接 [{}] for key [{}] in map {}.", conn, key, map);
    }


    /**
     *
     * @param map ThreadLocal
     */
    private static void closeConnectionByMap(Map<Object,Connection> map){
        if((null != map) && (!map.isEmpty())){
            try {
                Connection conn;
                for(Map.Entry<Object, Connection> e : map.entrySet()){
                    conn = e.getValue();
                    //清理挂起事务连接
                    removeSuspendedConnection(e.getKey());
                    close(conn);
                }
            } catch (Throwable ex) {
                logger.debug("Could not close JDBC Connection after transaction", ex);
            }
        }
    }

    /**
     * 清理挂起事务连接
     * @param key datasource
     */
    public static void removeSuspendedConnection(Object key){
        Map<Object, Connection> map = currentSuspendedConnection.get();
        if((null != map) && (!map.isEmpty())){
            map.remove(key);
        }
    }
    public static void closeSuspendedConnection(){
        Map<Object, Connection> map = currentSuspendedConnection.get();
        if((null != map) && (!map.isEmpty())){
            try {
                Connection conn;
                for(Map.Entry<Object, Connection> e : map.entrySet()){
                    conn = e.getValue();
                    close(conn);
                }
                map = null;
                currentSuspendedTransactionActive.set(null);
                currentSuspendedConnection.set(null);
            } catch (Throwable ex) {
                logger.debug("Could not close JDBC Connection after transaction", ex);
            }
        }
    }
    public static void commitSuspendedConnection(){
        Map<Object, Connection> map = currentSuspendedConnection.get();
        logger.debug("提交挂起连接 {}",map);
        if((null != map) && (!map.isEmpty())){
            try {
                Connection conn;
                for(Map.Entry<Object, Connection> e : map.entrySet()){
                    conn = e.getValue();
                    commit(conn);
                }
            } catch (Throwable ex) {
                logger.debug("Could not commit JDBC Connection after transaction", ex);
            }
        }
    }
    public static void rollbackSuspendedConnection(){
        Map<Object, Connection> map = currentSuspendedConnection.get();
        logger.debug("回滚挂起连接 {}",map);
        if((null != map) && (!map.isEmpty())){
            try {
                Connection conn;
                for(Map.Entry<Object, Connection> e : map.entrySet()){
                    conn = e.getValue();
                    rollback(conn);
                }
            } catch (Throwable ex) {
                logger.debug("Could not rollback JDBC Connection after transaction", ex);
            }
        }
    }


    /**
     * 关闭当前事务管理器连接
     * @param key
     */
    public static void rollbackSlaveConnection(Object key) {
        Map<Object,Connection> map = slaveConnectionHolder.get();
        logger.debug("回滚Slave连接 {} by [{}].", map, key);
        rollbackConnectionByKey(map, key);
    }
    public static void rollbackMasterConnection(Object key) {
        Map<Object,Connection> map = masterConnectionHolder.get();
        logger.debug("回滚Master连接 {} by [{}].", map, key);
        rollbackConnectionByKey(map, key);
    }
    public static void rollbackSuspendedConnection(Object key){
        Map<Object,Connection> map = currentSuspendedConnection.get();
        logger.debug("回滚挂起连接 {} by [{}].", map, key);
        rollbackConnectionByKey(map, key);
    }
    public static void rollbackConnectionByKey(Map<Object,Connection> map,Object key){
        if((null != map) && (!map.isEmpty())){
            Connection conn = map.get(key);
            try{
                rollback(conn);
            } catch (Throwable ex) {
                logger.debug("Could not rollback JDBC Connection after transaction", ex);
            }
        }
    }
    public static void rollbackSlaveConnection() {
        Map<Object,Connection> map = slaveConnectionHolder.get();
        logger.debug("回滚Slave连接 {} in ",map);
        rollbackConnectionByMap(map);
    }
    public static void rollbackMasterConnection() {
        Map<Object,Connection> map = masterConnectionHolder.get();
        logger.debug("回滚Master连接 {} in ",map);
        rollbackConnectionByMap(map);
    }
    public static void rollbackConnectionByMap(Map<Object,Connection> map){
        if((null != map) && (!map.isEmpty())){
            try {
                Connection conn;
                for(Map.Entry<Object, Connection> e : map.entrySet()){
                    conn = e.getValue();
                    rollback(conn);
                }
            } catch (Throwable ex) {
                logger.debug("Could not commit JDBC Connection after transaction", ex);
            }
        }
    }


    /**
     * 提交当前事务管理器连接
     * @param key
     */
    public static void commitSlaveConnection(Object key){
        Map<Object,Connection> map = slaveConnectionHolder.get();
        logger.debug("提交Slave连接 {} by [{}].", map, key);
        commitConnectionByKey(map,key);
    }
    public static void commitMasterConnection(Object key){
        Map<Object,Connection> map = masterConnectionHolder.get();
        logger.debug("提交Master连接 {} by [{}].", map, key);
        commitConnectionByKey(map,key);
    }
    public static void commitSuspendedConnection(Object key){
        Map<Object,Connection> map = currentSuspendedConnection.get();
        logger.debug("提交挂起连接 {} by [{}].", map, key);
        commitConnectionByKey(map, key);
    }
    public static void commitConnectionByKey(Map<Object,Connection> map,Object key){
        if((null != map) && (!map.isEmpty())){
            Connection conn = map.get(key);
            try{
                commit(conn);
            } catch (Throwable ex) {
                logger.debug("Could not commit JDBC Connection after transaction", ex);
            }
        }
    }
    /**
     * 关闭当前事务管理器连接
     * @param key
     */
    public static void cleanupSlaveConnection(Object key){
        Map<Object,Connection> map = slaveConnectionHolder.get();
        logger.debug("释放Slave连接 {} by [{}].", map, key);
        closeConnectionByKey(map,key);
        logger.debug("释放Slave连接完成 {} by [{}].", map, key);
    }
    public static void cleanupMasterConnection(Object key){
        Map<Object,Connection> map = masterConnectionHolder.get();
        logger.debug("释放Master连接 {} by [{}].", map, key);
        closeConnectionByKey(map,key);
        logger.debug("释放Master连接完成 {} by [{}].", map, key);
    }
    public static void cleanupSuspendedConnection(Object key){
        Map<Object,Connection> mapConn = currentSuspendedConnection.get();
        Map<Object,Boolean> mapActive = currentSuspendedTransactionActive.get();
        logger.debug("释放挂起连接 {} by [{}].", mapConn, key);
        logger.debug("释放挂起连接活动 {} by [{}].", mapActive, key);
        closeConnectionByKey(mapConn, key);
        closeConnectionActiveByKey(mapActive, key);
        logger.debug("释放挂起连接完成 {} by [{}].", mapConn, key);
        logger.debug("释放挂起连接活动完成 {} by [{}].", mapActive, key);
        if(mapConn == null || mapConn.isEmpty()) {
            currentSuspendedConnection.set(null);
        }
        if(mapActive == null || mapActive.isEmpty()) {
            currentSuspendedTransactionActive.set(null);
        }
    }
    public static void closeConnectionActiveByKey(Map<Object,Boolean> map, Object key){
        if((null != map) && (!map.isEmpty())){
            Boolean active = map.get(key);
            if(active != null){
                map.remove(key);
            }
            if(map.isEmpty()){
                map.clear();
            }
        }
    }
    public static void closeConnectionByKey(Map<Object,Connection> map, Object key){
        if((null != map) && (!map.isEmpty())){
            Connection conn = map.get(key);
            try{
                close(conn);
                if(conn != null){
                    map.remove(key);
                }
                if(map.isEmpty()){
                    map.clear();
                }
            } catch (Throwable ex) {
                logger.debug("Could not close JDBC Connection after transaction", ex);
            }
        }
    }
    static void close(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            logger.debug("释放 JDBC Connection [" + conn + "] after transaction");
            conn.close();
        }
    }
    static void rollback(Connection conn) throws SQLException {
        if ((conn != null) && (!conn.isClosed()) && (!conn.getAutoCommit())) {
            logger.debug("回滚 JDBC Connection [" + conn + "] after transaction");
            conn.rollback();
        }
    }
    static void commit(Connection conn) throws SQLException {
        if ((conn != null) && (!conn.isClosed()) && (!conn.getAutoCommit())) {
            logger.debug("提交 JDBC Connection [" + conn + "] after transaction");
            conn.commit();
        }
    }
    public static class ProxyConnectionHolder extends ResourceHolderSupport {
        Connection connection;

        public Connection getConnection() {
            return connection;
        }

        public ProxyConnectionHolder(Connection connection) {
            this.connection = connection;
        }
    }
}
