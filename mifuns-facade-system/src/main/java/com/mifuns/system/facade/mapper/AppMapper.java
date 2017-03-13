package com.mifuns.system.facade.mapper;

import com.mifuns.system.facade.entity.App;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppMapper {
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
    int insert(App record);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    int insertSelective(App record);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    App selectByPrimaryKey(Long appId);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(App record);

    /**
     * sys_app
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(App record);

    List<App> findAll();

    App findAppByAppKey(@Param("appKey") String appKey);

    App findAppByAppSecret(@Param("appSecret") String appSecret);
}