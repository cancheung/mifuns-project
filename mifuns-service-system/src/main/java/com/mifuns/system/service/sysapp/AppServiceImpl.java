package com.mifuns.system.service.sysapp;

import com.google.common.collect.Sets;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.App;
import com.mifuns.system.facade.enums.ResourceStatus;
import com.mifuns.system.facade.mapper.AppMapper;
import com.mifuns.system.facade.service.AppService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/12.
 */
@Service("appService")
public class AppServiceImpl implements AppService {

    @javax.annotation.Resource
    private AppMapper appMapper;


    @Override
    public App createApp(App app) {
        appMapper.insert(app);
        return app;
    }

    @Override
    public App updateApp(App app) {
        return appMapper.updateByPrimaryKeySelective(app) > 0 ? app : null;
    }

    @Override
    public int deleteApp(Long appId) {
        return appMapper.deleteByPrimaryKey(appId);
    }

    @Override
    public int deleteApps(String appIds) {
        Iterable<String> ids = CollectionUtil.split(appIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String appId : _ids) {
            i += deleteApp(Long.valueOf(appId));
        }
        return i;
    }

    @Override
    public int disableApps(String appIds) {
        Iterable<String> ids = CollectionUtil.split(appIds);
        HashSet<String> _ids = Sets.newHashSet(ids);
        int i = 0;
        for (String appId : _ids) {
            if(null != updateApp(new App((Long.valueOf(appId)), ResourceStatus.UNAVAILABLE.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public App findOne(Long appId) {
        return appMapper.selectByPrimaryKey(appId);
    }

    @Override
    public List<App> findAll() {
        return appMapper.findAll();
    }

    @Override
    public App findAppByAppKey(String appKey) {
        return appMapper.findAppByAppKey(appKey);
    }

    @Override
    public App findAppByAppSecret(String appSecret) {
        return appMapper.findAppByAppSecret(appSecret);
    }

    @Override
    public Map<Long, App> findApps() {
        Map<Long, App> map = new HashMap<>();
        List<App> list = findAll();
        for(App app : list){
            map.put(app.getAppId(), app);
        }
        return map;
    }
}
