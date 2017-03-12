package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.Authorization;

import java.util.List;

public interface AuthorizationMapper {
    /**
     * sys_user_app_role
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * sys_user_app_role
     *
     * @mbggenerated
     */
    int insert(Authorization record);

    /**
     * sys_user_app_role
     *
     * @mbggenerated
     */
    int insertSelective(Authorization record);

    /**
     * sys_user_app_role
     *
     * @mbggenerated
     */
    Authorization selectByPrimaryKey(Long id);

    /**
     * sys_user_app_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Authorization record);

    /**
     * sys_user_app_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Authorization record);


    List<Authorization> findAll();

    Authorization findByAppUser(Authorization record);

    int saveAuthorization(List<Authorization> record);

    int delete(Authorization authorization);
}