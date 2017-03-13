package com.mifuns.system.facade.mapper;

import com.mifuns.common.page.CommonPageMapper;
import com.mifuns.system.facade.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends CommonPageMapper<Role> {
    /**
     * sys_role
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long roleId);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int insert(Role record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int insertSelective(Role record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    Role selectByPrimaryKey(Long roleId);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Role record);


    List<Role> findAll();

    List<Role> findRoles(@Param("roleIdsStr") String roleIdsStr);

    List<Role> findRolesByAppUser(Map<String, String> record);


}