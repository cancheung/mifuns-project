package com.mifuns.system.service.sysuser;

import com.github.pagehelper.Page;
import com.google.common.collect.Sets;
import com.mifuns.common.page.PageBean;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.SysRole;
import com.mifuns.system.facade.entity.SysUser;
import com.mifuns.system.facade.enums.SysUserStatus;
import com.mifuns.system.facade.mapper.SysRoleMapper;
import com.mifuns.system.facade.mapper.SysUserMapper;
import com.mifuns.system.facade.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Created by miguangying on 2017/3/3.
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    SysUserMapper sysUserMapper;


    @Override
    public Page<SysUser> queryPageList(PageBean pageBean) {
        return sysUserMapper.queryPageList(pageBean);
    }

    @Override
    public SysUser queryUserByPassword(String username, String password) {
        return sysUserMapper.validateUser(username,password);
    }

    @Override
    public SysUser createSysUser(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
        return sysUser;
    }

    @Override
    public SysUser updateSysUser(SysUser sysUser) {
        return sysUserMapper.updateByPrimaryKeySelective(sysUser) > 0 ? sysUser : null;
    }

    @Override
    public int deleteSysUser(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLocked(SysUserStatus.LOCKED.STATUS);
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int disableSysUsers(String userIds) {
        Iterable<String> ids = CollectionUtil.split(userIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String userId : _ids) {
            if(null != updateSysUser(new SysUser(Long.valueOf(userId), SysUserStatus.LOCKED.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public int cleanSysUser(Long userId) {
        return sysUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        //不更新字段
        SysUser _user = new SysUser();
        _user.setPassword(user.getPassword());
        _user.setSalt(user.getSalt());
        _user.setUserId(user.getUserId());
        _user.setUpdateDate(DateUtils.now());
        sysUserMapper.updateByPrimaryKeySelective(_user);
    }

    @Override
    public SysUser findOne(Long userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<SysUser> findAll() {
        return sysUserMapper.findAll();
    }

    @Override
    public SysUser findByUsername(String username) {
        SysUser user = sysUserMapper.findByUsername(username);
        logger.info("findByUsername = {}",user);
        return user;
    }

    @Override
    public Map<Long, SysUser> findSysUsers() {
        List<SysUser> users = findAll();
        Map<Long,SysUser> map = new HashMap<>();
        for(SysUser user : users){
            map.put(user.getUserId(), user);
        }
        return map;
    }

    @Override
    public List<SysUser> findUserByRole(String roleIds) {
        return sysUserMapper.findUserByRole(roleIds);
    }
}
