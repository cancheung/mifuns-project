package com.mifuns.system.service.sysuser;

import com.github.pagehelper.Page;
import com.google.common.collect.Sets;
import com.mifuns.common.page.PageBean;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.User;
import com.mifuns.system.facade.enums.UserStatus;
import com.mifuns.system.facade.mapper.UserMapper;
import com.mifuns.system.facade.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Created by miguangying on 2017/3/3.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @javax.annotation.Resource
    UserMapper userMapper;


    @Override
    public Page<User> queryPageList(PageBean pageBean) {
        return userMapper.queryPageList(pageBean);
    }

    @Override
    public User queryUserByPassword(String username, String password) {
        return userMapper.validateUser(username,password);
    }

    @Override
    public User createUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user) > 0 ? user : null;
    }

    @Override
    public int deleteUser(Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setLocked(UserStatus.LOCKED.STATUS);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int disableUsers(String userIds) {
        Iterable<String> ids = CollectionUtil.split(userIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String userId : _ids) {
            if(null != updateUser(new User(Long.valueOf(userId), UserStatus.LOCKED.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public int cleanUser(Long userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        //不更新字段
        User _user = new User();
        _user.setPassword(user.getPassword());
        _user.setSalt(user.getSalt());
        _user.setUserId(user.getUserId());
        _user.setUpdateDate(DateUtils.now());
        userMapper.updateByPrimaryKeySelective(_user);
    }

    @Override
    public User findOne(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        logger.info("findByUsername = {}",user);
        return user;
    }

    @Override
    public Map<Long, User> findUsers() {
        List<User> users = findAll();
        Map<Long,User> map = new HashMap<>();
        for(User user : users){
            map.put(user.getUserId(), user);
        }
        return map;
    }

    @Override
    public List<User> findUserByRole(String roleIds) {
        return userMapper.findUserByRole(roleIds);
    }
}
