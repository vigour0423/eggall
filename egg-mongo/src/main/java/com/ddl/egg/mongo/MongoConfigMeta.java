package com.ddl.egg.mongo;

import org.springframework.util.Assert;

/**
 * Created by mark on 2017/3/21.
 */
public class MongoConfigMeta {

    private String dbName;
    private String servers;
    private String userName;
    private String password;
    private String mongoDTOPackage;
    private String replicaSet;

    public MongoConfigMeta(String dbName, String servers, String userName, String password, String mongoDTOPackage, String replicaSet) {
        Assert.hasText(dbName, "mongo db name can not be empty.");
        Assert.hasText(servers, "mongo servers can not be empty.");
        this.dbName = dbName;
        this.servers = servers;
        this.userName = userName;
        this.password = password;
        this.mongoDTOPackage = mongoDTOPackage;
        this.replicaSet = replicaSet;
    }

    public String getDbName() {
        return dbName;
    }

    public String getServers() {
        return servers;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getMongoDTOPackage() {
        return mongoDTOPackage;
    }

    public String getReplicaSet() {
        return replicaSet;
    }
}
