package com.ddl.egg.mongo;

import com.github.fakemongo.Fongo;
import com.ddl.egg.log.util.IpUtil;
import com.ddl.egg.mongo.access.MongoAccess;
import com.ddl.egg.mongo.converter.EnumToIntegerConverter;
import com.ddl.egg.mongo.converter.IntegerToEnumConverter;
import com.mongodb.MockMongoClient;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by mark.huang on 2016-06-16.
 */
@Configuration
public class TestMongoConfig {

    private static final String TEST_DB_NAME = "ums-test-";
    private static final String UN_KNOWN = "unknown";

    @Autowired
    Environment env;

    @Bean(destroyMethod = "end")
    public TestMongoBean testCaseMongoBean() throws UnknownHostException {
        TestMongoBean testCaseMongoBean = new TestMongoBean();
        testCaseMongoBean.setMongoTemplate(mongoTemplate());
        return testCaseMongoBean;
    }

    @Bean
    public MongoClient mongoClient() throws MongoException, UnknownHostException {
        return MockMongoClient.create(new Fongo("TestCaseMongo"));
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws MongoException, UnknownHostException {
        return new SimpleMongoDbFactory(mongoClient(), TEST_DB_NAME + getComputerName());
    }


    @Bean
    public MongoTemplate mongoTemplate() throws MongoException, UnknownHostException {
        return new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter() throws UnknownHostException {
        MongoMappingContext mappingContext = new MongoMappingContext();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        CustomConversions customConversions = new CustomConversions(Arrays.asList(new IntegerToEnumConverter(), new EnumToIntegerConverter()));
        mappingMongoConverter.setCustomConversions(customConversions);

        return mappingMongoConverter;
    }

    private MongoClientOptions newMongoClientOptions() {
        MongoClientOptions.Builder optionBuilder = MongoClientOptions.builder();
        return optionBuilder.connectionsPerHost(50)
                .threadsAllowedToBlockForConnectionMultiplier(10)
                .connectTimeout(3000)
                .maxWaitTime(3500)
                .socketKeepAlive(true)
                .socketTimeout(3000)
                .readPreference(ReadPreference.primary())
                .build();
    }

    private String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        } else if (env.containsKey("HOSTNAME")) {
            return env.get("HOSTNAME");
        } else {
            String ip = IpUtil.getLocalIp();
            if (StringUtils.isBlank(ip)) {
                ip = UN_KNOWN;
            } else {
                ip = ip.replaceAll("\\.", "");
            }
            return ip;
        }
    }


    @Bean
    public MongoAccess mongoAccess() throws UnknownHostException {
        MongoAccess mongoAccess = new MongoAccess();
        mongoAccess.setMongoTemplate(mongoTemplate());
        return mongoAccess;
    }

}
