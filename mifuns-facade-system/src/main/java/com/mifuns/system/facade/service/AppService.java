package com.mifuns.system.facade.service;

import com.mifuns.system.facade.entity.App;

import java.util.List;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/12.
 */
public interface AppService {
    public App createApp(App app);
    public App updateApp(App app);
    int deleteApp(Long appId);

    /**
     * 批量删除应用
     * @param appIds
     */
    int deleteApps(String appIds);

    /**
     * 批量禁用应用
     * @param appIds
     */
    int disableApps(String appIds);

    public App findOne(Long appId);
    public List<App> findAll();

    /**
     * 根据appKey查找AppId
     * @param appKey
     * @return
     */
    public App findAppByAppKey(String appKey);

    public App findAppByAppSecret(String appSecret);

    /**
     *
     * @return
     */
    Map<Long, App> findApps();
}
