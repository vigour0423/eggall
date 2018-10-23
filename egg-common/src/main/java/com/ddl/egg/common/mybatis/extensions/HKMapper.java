package com.ddl.egg.common.mybatis.extensions;

import tk.mybatis.mapper.common.Mapper;

/**
 * Created by lincn on 2016/6/3.
 */
public interface HKMapper<T> extends Mapper<T>,SelectLockMapper<T> {
}
