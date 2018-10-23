package com.ddl.egg.mq.mongo.config;

import com.ddl.egg.mongo.access.MongoAccess;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lincn on 2016/8/5.
 */
@Configuration
public class MongoConfig {

	public static final String SEPARATOR_SERVERS = ",";
	public static final String SEPARATOR_SERVER = ":";

	private String username;

	private String password;

	private String dbname;

	private String servers;

	private String replicaSetName;

	@Bean
	public MongoClient mongoClient() throws MongoException, UnknownHostException {
		MongoCredential mongoCredential = newMongoCredential();
		return mongoCredential == null ?
				new MongoClient(newServerAddresses(), newMongoClientOptions()) :
				new MongoClient(newServerAddresses(), Arrays.asList(mongoCredential), newMongoClientOptions());
	}

	@Bean
	public MongoDbFactory mongoDbFactory(@Qualifier("mongoClient") MongoClient mongoClient) throws MongoException, UnknownHostException {
		return new SimpleMongoDbFactory(mongoClient(), dbname);
	}

	/*@Bean
	public MappingMongoConverter mappingMongoConverter() throws UnknownHostException {
		MongoMappingContext mappingContext = new MongoMappingContext();
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
		MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
		mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

		return mappingMongoConverter;
	}*/

	@Bean
	public MongoTemplate mongoTemplate(@Qualifier("mongoDbFactory") MongoDbFactory mongoDbFactory) throws MongoException, UnknownHostException {
		return new MongoTemplate(mongoDbFactory);
	}

	@Bean
	public MongoAccess mongoAccess(@Qualifier("mongoTemplate") MongoTemplate mongoTemplate) throws UnknownHostException {
		MongoAccess mongoAccess = new MongoAccess();
		mongoAccess.setMongoTemplate(mongoTemplate);
		return mongoAccess;
	}

	private List<ServerAddress> newServerAddresses() throws UnknownHostException {
		List<ServerAddress> serverAddresses = new ArrayList<>();
		String[] myServers = servers.split(SEPARATOR_SERVERS);
		for (String server : myServers) {
			serverAddresses.add(newServerAddress(server));
		}
		return serverAddresses;
	}

	private ServerAddress newServerAddress(String server) throws UnknownHostException {
		String[] serverConfigs = server.split(SEPARATOR_SERVER);
		String host = serverConfigs[0];
		int port = Integer.parseInt(serverConfigs[1]);
		return new ServerAddress(host, port);
	}

	private MongoClientOptions newMongoClientOptions() {
		MongoClientOptions.Builder optionBuilder = MongoClientOptions.builder();
		if (StringUtils.hasText(replicaSetName)) {
			optionBuilder.requiredReplicaSetName(replicaSetName);
		}
		return optionBuilder.connectionsPerHost(20).threadsAllowedToBlockForConnectionMultiplier(10).connectTimeout(5000).maxWaitTime(6000).socketKeepAlive(true)
				.socketTimeout(5000).build();
	}

	private MongoCredential newMongoCredential() {
		if (StringUtils.hasText(username)) {
			char[] passwordArray = StringUtils.hasText(password) ? password.toCharArray() : new char[0];
			return MongoCredential.createCredential(username, dbname, passwordArray);
		}
		return null;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public void setReplicaSetName(String replicaSetName) {
		this.replicaSetName = replicaSetName;
	}
}
