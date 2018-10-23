package com.ddl.egg.mongo;

import com.ddl.egg.mongo.access.MongoAccess;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by mark.huang on 2016-06-12.
 */
public abstract class DefaultMongoConfig {

    public static final String SEPARATOR_SERVERS = ",";
    public static final String SEPARATOR_SERVER = ":";

    public abstract MongoConfigMeta configMeta();

    @Bean
    public MongoDbFactory mongoDbFactory() throws MongoException, UnknownHostException {
        return new SimpleMongoDbFactory(mongoClient(), configMeta().getDbName());
    }

    private List<ServerAddress> newServerAddresses() throws UnknownHostException {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        String[] servers = configMeta().getServers().split(SEPARATOR_SERVERS);
        for (String server : servers) {
            serverAddresses.add(newServerAddress(server));
        }
        return serverAddresses;
    }

    private MongoCredential newMongoCredential() {
        String username = configMeta().getUserName();
        String password = configMeta().getPassword();

        if (StringUtils.hasText(username)) {
            char[] passwordArray = StringUtils.hasText(password) ? password.toCharArray() : new char[0];
            return MongoCredential.createCredential(username, configMeta().getDbName(), passwordArray);
        }
        return null;
    }

    private Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
        String basePackage = configMeta().getMongoDTOPackage();
        Set<Class<?>> initialEntitySet = new HashSet<Class<?>>();

        if (StringUtils.hasText(basePackage)) {
            ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
                    false);
            componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
            componentProvider.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));

            for (BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
                initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(),
                        AbstractMongoConfiguration.class.getClassLoader()));
            }
        }

        return initialEntitySet;
    }

    @Bean
    public MongoClient mongoClient() throws MongoException, UnknownHostException {
        MongoCredential mongoCredential = newMongoCredential();
        return mongoCredential == null ? new MongoClient(newServerAddresses(), newMongoClientOptions()) : new MongoClient(newServerAddresses(), Arrays.asList(mongoCredential), newMongoClientOptions());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MongoMappingContext mappingContext = mongoMappingContext();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return mappingMongoConverter;
    }

    @Bean
    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setInitialEntitySet(getInitialEntitySet());
        return mappingContext;
    }

    @Bean
    public MongoAccess mongoAccess() throws Exception {
        MongoAccess mongoAccess = new MongoAccess();
        mongoAccess.setMongoTemplate(mongoTemplate());
        return mongoAccess;
    }
    
    private ServerAddress newServerAddress(String server) throws UnknownHostException {
        String[] serverConfigs = server.split(SEPARATOR_SERVER);
        String host = serverConfigs[0];
        int port = Integer.parseInt(serverConfigs[1]);
        return new ServerAddress(host, port);
    }

    private MongoClientOptions newMongoClientOptions() {
        MongoClientOptions.Builder optionBuilder = MongoClientOptions.builder();
        String replicaSetName = configMeta().getReplicaSet();
        if (StringUtils.hasText(replicaSetName)) {
            optionBuilder.requiredReplicaSetName(replicaSetName);
        }

        return optionBuilder.connectionsPerHost(50)
                .threadsAllowedToBlockForConnectionMultiplier(10)
                .connectTimeout(5000)
                .maxWaitTime(5000)
                .socketKeepAlive(true)
                .socketTimeout(5000)
                .readPreference(ReadPreference.primaryPreferred())
                .build();
    }
}
