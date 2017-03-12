package com.mifuns.system.facade.mapper;

import com.github.pagehelper.Page;
import com.mifuns.common.page.CommonPageMapper;
import com.mifuns.system.facade.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysRoleMapper extends CommonPageMapper<SysRole> {
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
    int insert(SysRole record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int insertSelective(SysRole record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    SysRole selectByPrimaryKey(Long roleId);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysRole record);

    /**
     * sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysRole record);


    List<SysRole> findAll();

    List<SysRole> findRoles(@Param("roleIdsStr") String roleIdsStr);

    List<SysRole> findRolesByAppUser(Map<String, String> record);


}