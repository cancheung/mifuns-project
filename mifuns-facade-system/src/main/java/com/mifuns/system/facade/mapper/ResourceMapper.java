package com.mifuns.system.facade.mapper;

import com.mifuns.common.page.CommonPageMapper;
import com.mifuns.system.facade.entity.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ResourceMapper extends CommonPageMapper<Resource>{
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
    int insert(Resource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int insertSelective(Resource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    Resource selectByPrimaryKey(Long resourceId);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Resource record);

    /**
     * sys_resource
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Resource record);


    List<Resource> findAll();

    List<Resource> findTypeAll(@Param("type") String type);

    /**
     * 查询资源
     * @param ids 资源IDS
     * @return
     */
    List<Resource> findResources(@Param("ids") String ids);

    List<Resource> findResourcesByAppUser(Map<String, String> record);

    /**
     * 查询角色资源
     * @param roleIds 角色IDS
     * @return
     */
    List<Resource> findResourcesByRoleIds(@Param("roleIds") String roleIds);
}