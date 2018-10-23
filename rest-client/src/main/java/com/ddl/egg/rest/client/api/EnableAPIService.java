package com.ddl.egg.rest.client.api;

import com.ddl.egg.rest.client.RESTServiceClient;
import com.ddl.egg.rest.client.http.HTTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnableAPIService {

    private final Logger logger = LoggerFactory.getLogger(EnableAPIService.class);
    private final EnableAPISettings settings;
    private final Map<Class<?>, Object> proxy = new HashMap<>();

    private final APIProxy apiProxy;
    private final APIScanner apiScanner;
    private final DefaultListableBeanFactory defaultListableBeanFactory;

    public EnableAPIService(EnableAPISettings settings, String packageName, DefaultListableBeanFactory defaultListableBeanFactory) {
        this.settings = settings;
        this.apiScanner = new APIScanner(packageName);
        if (settings.getUseMock()) {
            apiProxy = defaultListableBeanFactory.getBean(APIProxy.class);
            Assert.notNull(apiProxy, "apiProxy must be Inject when enableAPISetting enable mock");
        } else {
            this.apiProxy = new APIProxyFactory(settings.getServerURLMapping(),
                    createRESTServiceClient(defaultListableBeanFactory.getBean(HTTPClient.class)), defaultListableBeanFactory.getBean(Environment.class));
        }
        this.defaultListableBeanFactory = defaultListableBeanFactory;
    }

    public void autoProxy() {
        try {
            createProxy();
            register();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    void createProxy() throws IOException, ClassNotFoundException {
        for (Class<?> apiClass : apiScanner.scan()) {
            proxy.put(apiClass, apiProxy.create(apiClass));
        }
    }

    void register() {
        for (Map.Entry<Class<?>, Object> entry : proxy.entrySet()) {
            logger.info("register api service {}", entry.getKey().getName());
            defaultListableBeanFactory.registerSingleton(entry.getKey().getName(), entry.getValue());
        }
    }

    private RESTServiceClient createRESTServiceClient(HTTPClient httpClient) {
        RESTServiceClient serviceClient = new RESTServiceClient();
        serviceClient.setHTTPClient(httpClient);
        serviceClient.setValidateStatusCode(settings.isValidateStatusCode());
        return serviceClient;
    }

}
