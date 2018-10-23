package com.ddl.egg.redis;

import org.springframework.util.Assert;

/**
 * Created by mark on 2017/3/21.
 */
public class RedisConfigMeta {

    private String host;
    private int port;
    private String password;

    public RedisConfigMeta(String host, int port, String password) {
        Assert.hasText(host, "host can not be null.");
        Assert.isTrue(port > 0, "port must be more than 0.");
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }
}
