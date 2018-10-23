package com.ddl.egg.common.util;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

/**
 * Created by lincn on 2017/12/4.
 */
public class HttpclientUtil {
	private static final CloseableHttpClient HTTP_CLIENT;
	private static final RequestConfig DEFAULT_REQUEST_CONFIG;

	static {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		// 设置不使用Expect:100-Continue握手
		DEFAULT_REQUEST_CONFIG = RequestConfig.custom().setExpectContinueEnabled(false)
				.setConnectTimeout(10000) // 设置连接超时时间
				.setSocketTimeout(60000) // 设置读数据超时时间
				.build();
		httpClientBuilder.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
		                                                            .register("http", PlainConnectionSocketFactory.INSTANCE)
		                                                            .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(200);

		SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connectionManager.setDefaultSocketConfig(defaultSocketConfig);

		// Create connection configuration
		ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
		connectionManager.setDefaultConnectionConfig(defaultConnectionConfig);

		httpClientBuilder.setConnectionManager(connectionManager);
		httpClientBuilder.setRedirectStrategy(DefaultRedirectStrategy.INSTANCE);
		HTTP_CLIENT = httpClientBuilder.build();
	}

	public static HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return HTTP_CLIENT.execute(request);
	}
}
