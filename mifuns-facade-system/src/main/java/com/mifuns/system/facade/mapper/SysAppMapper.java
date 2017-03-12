package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.SysApp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAppMapper {
    /**
     * sys_app
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long appId);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    int insert(SysApp record);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    int insertSelective(SysApp record);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    SysApp selectByPrimaryKey(Long appId);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysApp record);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysApp record);

    List<SysApp> findAll();

    SysApp findAppByAppKey(@Param("appKey") String appKey);

    SysApp findAppByAppSecret(@Param("appSecret")  String appSecret);
}