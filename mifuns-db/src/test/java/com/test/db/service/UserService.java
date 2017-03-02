package com.test.db.service;

import com.test.db.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by miguangying on 2016/10/10.
 */
@Service("userService")
public class UserService {

    @Resource(name="seckillDataSourceProxy")
    private DataSource dataSource;

    @Resource(name="seckillTransactionManager")
    private PlatformTransactionManager transactionManager;





    public void insertUser(User user) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.update("INSERT INTO p_user(name,sex) VALUES('moddcmor','1')");
            jdbcTemplate.update("INSERT INTO p_user(name,sex) VALUES('mofmor','1')");
            jdbcTemplate.update("INSERT INTO p_user(name,sex) VALUES('mocmor','1')");
        } catch (DataAccessException ex) {
            transactionManager.rollback(status); // 也可以執行status.setRollbackOnly();
            throw ex;
        }
        transactionManager.commit(status);
    }
}
