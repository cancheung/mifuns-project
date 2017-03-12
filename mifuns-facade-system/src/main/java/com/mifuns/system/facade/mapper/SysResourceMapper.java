package com.mifuns.system.facade.mapper;

import com.mifuns.common.page.CommonPageMapper;
import com.mifuns.system.facade.entity.SysResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysResourceMapper extends CommonPageMapper<SysResource>{
    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long resourceId);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int insert(SysResource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int insertSelective(SysResource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    SysResource selectByPrimaryKey(Long resourceId);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysResource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysResource record);


    List<SysResource> findAll();

    List<SysResource> findTypeAll(@Param("type") String type);

    /**
     * 查询资源
     * @param ids 资源IDS
     * @return
     */
    List<SysResource> findResources(@Param("ids") String ids);

    List<SysResource> findResourcesByAppUser(Map<String, String> record);

    /**
     * 查询角色资源
     * @param roleIds 角色IDS
     * @return
     */
    List<SysResource> findResourcesByRoleIds(@Param("roleIds") String roleIds);
}