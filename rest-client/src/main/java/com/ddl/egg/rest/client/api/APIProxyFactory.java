package com.ddl.egg.rest.client.api;

import com.ddl.egg.rest.client.RESTServiceClient;
import com.ddl.egg.rest.client.http.HTTPConstants;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

public class APIProxyFactory implements APIProxy {
	private final Map<String, String> serverURLMapping;
	private final RESTServiceClient restServiceClient;
	private final Environment env;

	public APIProxyFactory(Map<String, String> serverURLMapping, RESTServiceClient restServiceClient, Environment env) {
		this.serverURLMapping = serverURLMapping;
		this.restServiceClient = restServiceClient;
		this.env = env;
	}

	public Object create(Class<?> apiClass) {
		return Enhancer.create(apiClass, new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				return doCreate(method, args);
			}
		});
	}

	Object doCreate(Method method, Object[] args) {

		RequestMapping annotatedAtClass = method.getDeclaringClass().getAnnotation(RequestMapping.class);
		RequestMapping annotatedAtMethod = method.getAnnotation(RequestMapping.class);

		Assert.notNull(annotatedAtMethod, " request mapping is required.");
		Assert.isTrue(annotatedAtMethod.method().length > 0, " request method is required.");

		API apiAnnotation = method.getDeclaringClass().getAnnotation(API.class);
		String serverURL = getServiceURL(apiAnnotation);
		Assert.hasText(serverURL, "serverURL is required.");
		String encoding = apiAnnotation.charsetEncoding();

		Class<? extends APIResponseProcessor> apiResponseProcessor = null;
		APIResponseProcess responseProcess = method.getAnnotation(APIResponseProcess.class);
		if (responseProcess != null) {
			Assert.isTrue(APIResponseProcessor.class.isAssignableFrom(responseProcess.name()), "processor must extends APIResponseProcessor.");
			apiResponseProcessor = responseProcess.name();
		}

		int timeout = 0;
		APIMethodConfig methodConfig = method.getAnnotation(APIMethodConfig.class);
		if (methodConfig != null) {
			timeout = methodConfig.timeout();

			if (StringUtils.hasText(methodConfig.charsetEncoding())) {
				encoding = methodConfig.charsetEncoding();
			}
		}

		String action = getAction(annotatedAtClass, annotatedAtMethod);
		String consumer = getConsumer(annotatedAtClass, annotatedAtMethod);
		checkConsumer(method.getReturnType(), consumer);

		try {
			switch (annotatedAtMethod.method()[0]) {
			case GET:
				return restServiceClient.get(new APIUriBuilder().withServerURL(serverURL).withAction(action).withEncoding(encoding)
				                                                .withAnnotations(method.getParameterAnnotations(), args).build(), method.getReturnType(), apiResponseProcessor,
				                             timeout, consumer);
			case POST:
				checkConsumer(args[0], consumer);
				return restServiceClient.post(new APIUriBuilder().withServerURL(serverURL).withAction(action).withEncoding(encoding)
				                                                 .withAnnotations(method.getParameterAnnotations(), args).build(), method.getReturnType(), args[1],
				                              apiResponseProcessor,
				                              timeout, consumer);
			case PUT:
				checkConsumer(args[0], consumer);
				return restServiceClient.put(new APIUriBuilder().withServerURL(serverURL).withAction(action).build(), method.getReturnType(), args[0], apiResponseProcessor,
				                             timeout, consumer);
			case DELETE:
				return restServiceClient.delete(new APIUriBuilder().withServerURL(serverURL).withAction(action).withAnnotations(method.getParameterAnnotations(), args).build(),
				                                method.getReturnType(), apiResponseProcessor, timeout, consumer);
			case OPTIONS:
				checkConsumer(args[0], consumer);

				String uri = null;
				if (args.length > 1) {
					Annotation[][] urlParamAnnotation = ArrayUtils.remove(method.getParameterAnnotations(), 0);
					Object[] urlParamArgs = ArrayUtils.remove(args, 0);
					uri = new APIUriBuilder().withServerURL(serverURL).withAction(action).withAnnotations(urlParamAnnotation, urlParamArgs).build();
				}
				return restServiceClient.post(uri, method.getReturnType(), args[0], apiResponseProcessor, timeout, consumer);
			default:
				throw new IllegalStateException("the method:" + annotatedAtMethod.method()[0].toString() + " does not support.");
			}
		} catch (RuntimeException e) {
			throw e;
		}
	}

	private String getServiceURL(API apiAnnotation) {
		Assert.notNull(apiAnnotation, " request mapping is required.");

		String serverUrl = apiAnnotation.serverUrl();
		if (StringUtils.hasText(serverUrl)) {
			return serverUrl;
		}

		String serverKey = apiAnnotation.serverName();

		String serverURL = env.getProperty(serverKey);
		if (StringUtils.isEmpty(serverURL)) {
			serverURL = serverURLMapping.get(serverKey);
		}

		return serverURL;
	}

	private String getAction(RequestMapping annotatedAtClass, RequestMapping annotatedAtMethod) {
		String action = "";
		if (annotatedAtClass != null && annotatedAtClass.value().length > 0)
			action = annotatedAtClass.value()[0];
		if (annotatedAtMethod.value().length > 0)
			action = action + annotatedAtMethod.value()[0];

		return action;
	}

	private String getConsumer(RequestMapping annotatedAtClass, RequestMapping annotatedAtMethod) {
		String consumer = HTTPConstants.CONTENT_TYPE_JSON;
		if (annotatedAtClass != null && annotatedAtClass.value().length > 0)
			consumer = annotatedAtClass.consumes()[0];
		if (annotatedAtMethod.consumes().length > 0)
			consumer = annotatedAtMethod.consumes()[0];

		return consumer;
	}

	private void checkConsumer(Class responseClass, String consumer) {
		if (HTTPConstants.CONTENT_TYPE_FORM.equals(consumer) && responseClass != String.class)
			throw new RuntimeException(HTTPConstants.CONTENT_TYPE_FORM + " only support returnType String");
	}

	private void checkConsumer(Object request, String consumer) {
		if (HTTPConstants.CONTENT_TYPE_FORM.equals(consumer) && !(request instanceof String))
			throw new RuntimeException(HTTPConstants.CONTENT_TYPE_FORM + " only support arg[0] type String");
	}
}
