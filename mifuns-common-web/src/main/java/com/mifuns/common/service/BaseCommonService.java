package com.mifuns.common.service;

import com.github.pagehelper.Page;
import com.mifuns.common.page.PageBean;

/**
 * Created by miguangying on 2017/3/4.
 */
public interface BaseCommonService {
    public Page queryPageList(PageBean pageBean);
}
