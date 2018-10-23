package com.ddl.egg.rest.client.http;

import com.ddl.egg.log.util.StopWatch;
import com.ddl.egg.log.util.TimeLength;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.http.client.config.CookieSpecs.IGNORE_COOKIES;


public class HTTPClient {
    public static final TimeLength DEFAULT_TIME_OUT = TimeLength.seconds(30);

    public static final TimeLength NO_TIME_OUT = TimeLength.ZERO;

    private static final int CONNECTION_POOL_MAX_SIZE = 300;

    private final Logger logger = LoggerFactory.getLogger(HTTPClient.class);

    private final ReentrantLock lock = new ReentrantLock();

    private transient HttpClient httpClient;

    private boolean connectionPoolEnabled = false;

    private boolean ignoreCookie = true;

    private boolean redirectEnabled = false;

    private boolean validateStatusCode = false;

    private boolean keepAlive = false; //todo 这个参数在http1.1一般都可以设置成true

    private TimeLength timeout = DEFAULT_TIME_OUT;

    private HTTPRequestRetryHandler requestRetryHandler = new HTTPRequestRetryHandler();

    public HTTPClient enableConnectionPool() {
        this.connectionPoolEnabled = true;
        return this;
    }

    public HTTPClient enableKeepAlive() {
        this.keepAlive = true;
        return this;
    }

    public HTTPClient enableValidateStatus() {
        this.validateStatusCode = true;
        return this;
    }

    public HTTPClient enableCookie() {
        this.ignoreCookie = false;
        return this;
    }

    public HTTPClient enableRedirect() {
        this.redirectEnabled = true;
        return this;
    }

    public HTTPClient setTimeout(TimeLength timeout) {
        this.timeout = timeout;
        return this;
    }

    public HTTPResponse execute(HTTPRequest request, int timeout) {
        StopWatch watch = new StopWatch();
        try {
            logger.debug("send request, url={}, method={}", request.url(), request.method());
            logger.debug("====== http request begin ======");
            logRequest(request);

            HttpRequestBase httpRequest = prepareRequest(request, timeout);
            HttpResponse response = getHttpClient().execute(httpRequest);

            logger.debug("====== http request end ======");
            logger.debug("received response, statusCode={}", response.getStatusLine().getStatusCode());

            HTTPStatusCode statusCode = new HTTPStatusCode(response.getStatusLine().getStatusCode());

            HTTPContentType contentType = new HTTPContentType();
            contentType.setContentType(ContentType.get(response.getEntity()));

            byte[] content = EntityUtils.toByteArray(response.getEntity());
            httpRequest.releaseConnection();
            HTTPResponse httpResponse = new HTTPResponse(statusCode, createResponseHeaders(response), contentType, content);
            logResponse(httpResponse);

            if (validateStatusCode) {
                validateStatusCode(statusCode);
            }

            return httpResponse;
        } catch (IOException e) {
            throw new HTTPException(e);
        } finally {
            logger.info("execute finished, elapsedTime={}(ms)", watch.elapsedTime());
        }
    }

    private HttpRequestBase prepareRequest(HTTPRequest request, int timeout) {
        HttpRequestBase httpRequest = request.getRawRequest();
        if (timeout > 0) {
            httpRequest.setConfig(prepareRequestConfig(timeout));
        }

        return httpRequest;
    }

    HTTPHeaders createResponseHeaders(HttpResponse response) {
        HTTPHeaders headers = new HTTPHeaders();
        for (Header header : response.getAllHeaders()) {
            headers.add(header.getName(), header.getValue());
        }
        return headers;
    }

    void validateStatusCode(HTTPStatusCode statusCode) {
        if (statusCode.isSuccess() || statusCode.isRedirect()) {
            return;
        }
        throw new HTTPException("invalid response status code, statusCode=" + statusCode);
    }

    void logRequest(HTTPRequest httpRequest) {
        if (httpRequest.headers() != null)
            for (HTTPHeader header : httpRequest.headers()) {
                logger.debug("[header] {}={}", header.name(), header.value());
            }

        if (httpRequest.parameters() != null) {
            for (NameValuePair parameter : httpRequest.parameters()) {
                logger.debug("[param] {}={}", parameter.getName(), parameter.getValue());
            }
        }

        logger.debug("body={}", httpRequest.body());
    }

    void logResponse(HTTPResponse httpResponse) {
        if (httpResponse.headers() != null)
            for (HTTPHeader header : httpResponse.headers()) {
                logger.debug("[header] {}={}", header.name(), header.value());
            }

        logger.debug("responseText={}", httpResponse.responseText());
    }

    HttpClient getHttpClient() {
        if (httpClient == null) {
            try {
                lock.lock();
                if (httpClient == null)
                    httpClient = createDefaultHttpClient();
            } finally {
                lock.unlock();
            }
        }
        return httpClient;
    }

    HttpClient createDefaultHttpClient() {
        HttpClientConnectionManager connectionManager = createClientConnectionManager();
        RequestConfig defaultRequestConfig = prepareRequestConfig((int) timeout.toMilliseconds());

        HttpClientBuilder builder = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(defaultRequestConfig);
        if (!redirectEnabled) {
            builder.disableRedirectHandling();
        }
        builder.setRetryHandler(requestRetryHandler);

        return builder.build();
    }

    private RequestConfig prepareRequestConfig(int timeouts) {
        RequestConfig.Builder requestBuilder = RequestConfig.custom().setSocketTimeout(timeouts).setConnectTimeout(timeouts);
        if (ignoreCookie) {
            requestBuilder.setCookieSpec(IGNORE_COOKIES);
        }

        return requestBuilder.build();
    }

    private HttpClientConnectionManager createClientConnectionManager() {
        Registry<ConnectionSocketFactory> registry = schemaRegistry();
        if (connectionPoolEnabled) {
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
            connectionManager.setMaxTotal(CONNECTION_POOL_MAX_SIZE);
            connectionManager.setDefaultMaxPerRoute(CONNECTION_POOL_MAX_SIZE);
            connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoKeepAlive(keepAlive).setSoTimeout((int) timeout.toMilliseconds()).build());
            return connectionManager;
        } else {
            BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registry);
            //SoTimeout 没必要设置，requestconfig可以盖掉
            connectionManager.setSocketConfig(SocketConfig.custom().setSoKeepAlive(keepAlive).build());
            return connectionManager;
        }
    }

    private Registry<ConnectionSocketFactory> schemaRegistry() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registerHttpScheme(registryBuilder);
        registerHttpsScheme(registryBuilder);
        return registryBuilder.build();
    }

    void registerHttpScheme(RegistryBuilder registryBuilder) {
        registryBuilder.register(HTTPConstants.SCHEME_HTTP, PlainConnectionSocketFactory.getSocketFactory());
    }

    void registerHttpsScheme(RegistryBuilder registryBuilder) {
        TrustManager[] trustManagers = new TrustManager[]{new SelfSignedX509TrustManager()};
        try {
            SSLContext context = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            context.init(null, trustManagers, null);

            X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, hostnameVerifier);

            registryBuilder.register(HTTPConstants.SCHEME_HTTPS, socketFactory);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new HTTPException(e);
        }
    }
}
