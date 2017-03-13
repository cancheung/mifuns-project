package com.mifuns.system.facade.mapper;

import com.mifuns.common.page.CommonPageMapper;
import com.mifuns.system.facade.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends CommonPageMapper<User> {
    /**
     * sys_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int insert(User record);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int insertSelective(User record);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    User selectByPrimaryKey(Long userId);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * sys_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(User record);

    User validateUser(@Param("username") String username, @Param("password") String password);

    List<User> queryUserByIds(List<Long> userIds);

    List<User> findAll();

    User findByUsername(String username);

    /**
     * 拥有角色的用户列表
     * @param roleIds
     * @return
     */
    List<User> findUserByRole(@Param("roleIds") String roleIds);
}