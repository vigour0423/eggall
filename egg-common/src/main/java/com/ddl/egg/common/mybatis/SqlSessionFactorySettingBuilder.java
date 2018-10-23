package com.ddl.egg.common.mybatis;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

import static org.apache.ibatis.session.LocalCacheScope.STATEMENT;

/**
 * Created by mark.huang on 2016-06-07.
 * for sqlSessionFactoryBean not supply method to modify default setting
 */
public class SqlSessionFactorySettingBuilder extends SqlSessionFactoryBuilder {


    @Override
    public SqlSessionFactory build(Configuration config) {
        config.setLazyLoadingEnabled(false);
        config.setCacheEnabled(false);
        config.setLocalCacheScope(STATEMENT);
        config.setCallSettersOnNulls(true);
        return new DefaultSqlSessionFactory(config);
    }

}
