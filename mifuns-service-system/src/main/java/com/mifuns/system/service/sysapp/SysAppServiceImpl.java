package com.mifuns.system.service.sysapp;

import com.google.common.collect.Sets;
import com.mifuns.common.util.CollectionUtil;
import com.mifuns.common.util.DateUtils;
import com.mifuns.system.facade.entity.SysApp;
import com.mifuns.system.facade.enums.ResourceStatus;
import com.mifuns.system.facade.mapper.SysAppMapper;
import com.mifuns.system.facade.service.SysAppService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/12.
 */
@Service("sysAppService")
public class SysAppServiceImpl implements SysAppService {

    @javax.annotation.Resource
    private SysAppMapper sysAppMapper;


    @Override
    public SysApp createApp(SysApp app) {
        sysAppMapper.insert(app);
        return app;
    }

    @Override
    public SysApp updateApp(SysApp app) {
        return sysAppMapper.updateByPrimaryKeySelective(app) > 0 ? app : null;
    }

    @Override
    public int deleteApp(Long appId) {
        return sysAppMapper.deleteByPrimaryKey(appId);
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
            if(null != updateApp(new SysApp((Long.valueOf(appId)), ResourceStatus.UNAVAILABLE.STATUS, DateUtils.now()))){
                i++;
            }
        }
        return i;
    }

    @Override
    public SysApp findOne(Long appId) {
        return sysAppMapper.selectByPrimaryKey(appId);
    }

    @Override
    public List<SysApp> findAll() {
        return sysAppMapper.findAll();
    }

    @Override
    public SysApp findAppByAppKey(String appKey) {
        return sysAppMapper.findAppByAppKey(appKey);
    }

    @Override
    public SysApp findAppByAppSecret(String appSecret) {
        return sysAppMapper.findAppByAppSecret(appSecret);
    }

    @Override
    public Map<Long, SysApp> findSysApps() {
        Map<Long, SysApp> map = new HashMap<>();
        List<SysApp> list = findAll();
        for(SysApp app : list){
            map.put(app.getAppId(), app);
        }
        return map;
    }
}
