package com.mifuns.common.page;

import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by miguangying on 2017/3/4.
 */
public interface CommonPageMapper<T> {
    Page<T> queryPageList(PageBean pageBean);
}
