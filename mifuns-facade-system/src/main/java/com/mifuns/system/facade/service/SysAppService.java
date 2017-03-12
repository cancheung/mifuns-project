package com.mifuns.system.facade.service;

import com.mifuns.system.facade.entity.SysApp;

import java.util.List;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/12.
 */
public interface SysAppService {
    public SysApp createApp(SysApp app);
    public SysApp updateApp(SysApp app);
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

    public SysApp findOne(Long appId);
    public List<SysApp> findAll();

    /**
     * 根据appKey查找AppId
     * @param appKey
     * @return
     */
    public SysApp findAppByAppKey(String appKey);

    public SysApp findAppByAppSecret(String appSecret);

    /**
     *
     * @return
     */
    Map<Long, SysApp> findSysApps();
}
