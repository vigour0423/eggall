package com.ddl.egg.common.util;

import com.alibaba.fastjson.JSON;
import com.ddl.egg.common.dto.BackendResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 响应输出工具类
 *
 * @author LP
 */
public abstract class ResponseTypeOutUtils {
    // -- header 常量定义 --//
    /**
     * The Constant DEFAULT_ENCODING.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * The Constant HEADER_ENCODING.
     */
    private static final String HEADER_ENCODING = "encoding";
    /**
     * The Constant HEADER_NOCACHE.
     */
    private static final String HEADER_NOCACHE = "no-cache";
    /**
     * The Constant DEFAULT_NOCACHE.
     */
    private static final boolean DEFAULT_NOCACHE = true;

    // -- 绕过jsp/freemaker直接输出文本的函数 --//

    /**
     * 直接输出内容的简便函数.
     * <p>
     * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
     * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
     * "no-cache:false");
     *
     * @param contentType the content type
     * @param content     the content
     * @param response    the response
     * @param headers     可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
     */
    public static void render(final String contentType, final String content, HttpServletResponse response, final String... headers) {
        response = initResponseHeader(contentType, response, headers);
        try {
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 直接输出文本.
     *
     * @param response the response
     * @param text     the text
     * @param headers  the headers
     */
    public static void renderText(final HttpServletResponse response, final String text, final String... headers) {
        render(HeaderUtils.TEXT_TYPE, text, response, headers);
    }

    /**
     * 直接输出HTML.
     *
     * @param response the response
     * @param html     the html
     * @param headers  the headers
     */
    public static void renderHtml(final HttpServletResponse response, final String html, final String... headers) {
        render(HeaderUtils.HTML_TYPE, html, response, headers);
    }

    /**
     * 直接输出XML.
     *
     * @param response the response
     * @param xml      the xml
     * @param headers  the headers
     */
    public static void renderXml(final HttpServletResponse response, final String xml, final String... headers) {
        render(HeaderUtils.XML_TYPE, xml, response, headers);
    }

    /**
     * 直接输出JSON.
     *
     * @param response   the response
     * @param jsonString json字符串.
     * @param headers    the headers
     */
    public static void renderJson(final HttpServletResponse response, final String jsonString, final String... headers) {
        render(HeaderUtils.JSON_TYPE, jsonString, response, headers);
    }

    /**
     * 自定义JSON
     *
     * @param response
     * @param code
     * @param message
     * @param headers
     */
    public static void renderJsonTemplate(final HttpServletResponse response, String code, String message, final String... headers) {
        String jsonString = BackendResult.success(code, message);
        render(HeaderUtils.JSON_TYPE, jsonString, response, headers);
    }

    /**
     * 自定义JSON：成功
     *
     * @param response
     */
    public static void renderJsonSuccess(final HttpServletResponse response) {
        renderJsonSuccess(response, "");
    }

    /**
     * 自定义JSON：成功
     *
     * @param response
     * @param message
     * @param headers
     */
    public static void renderJsonSuccess(final HttpServletResponse response, final String message, final String... headers) {
        String jsonString = BackendResult.success(message);
        render(HeaderUtils.JSON_TYPE, jsonString, response, headers);
    }

    /**
     * 自定义JSON：成功 带返回数据data
     *
     * @param response
     * @param message
     * @param headers
     */
    public static void renderJsonSuccessData(final HttpServletResponse response, final String message, final Object data, final String... headers) {
        String jsonString = BackendResult.success(message, data);
        render(HeaderUtils.JSON_TYPE, jsonString, response, headers);
    }

    /**
     * 自定义JSON：错误
     *
     * @param response
     */
    public static void renderJsonError(final HttpServletResponse response) {
        renderJsonError(response, "");
    }

    /**
     * 自定义JSON：错误
     *
     * @param response
     * @param message
     * @param headers
     */
    public static void renderJsonError(final HttpServletResponse response, final String message, final String... headers) {
        String jsonString = BackendResult.error(message);
        render(HeaderUtils.JSON_TYPE, jsonString, response, headers);
    }

    /**
     * 直接输出JSON，使用Jackson转换Java对象.
     *
     * @param response the response
     * @param data     可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
     * @param headers  the headers
     */
    public static void renderJson(HttpServletResponse response, final Object data, final String... headers) {
        String jsonString = JSON.toJSONString(data);
        render(HeaderUtils.JSON_TYPE, jsonString, response, headers);
    }

    /**
     * 直接输出支持跨域Mashup的JSONP.
     *
     * @param response     the response
     * @param callbackName callback函数名.
     * @param object       Java对象,可以是List<POJO>, POJO[], POJO ,也可以Map名值对, 将被转化为json字符串.
     * @param headers      the headers
     */
    public static void renderJsonp(final HttpServletResponse response, final String callbackName, final Object object, final String... headers) {
        String jsonString = JSON.toJSONString(object);
        String result = new StringBuilder().append(callbackName).append("(").append(jsonString).append(");").toString();
        // 渲染Content-Type为javascript的返回内容,输出结果为javascript语句,
        // 如callback197("{html:'Hello World!!!'}");
        render(HeaderUtils.JS_TYPE, result, response, headers);
    }

    /**
     * 分析并设置contentType与headers.
     *
     * @param contentType the content type
     * @param response    the response
     * @param headers     the headers
     * @return the http servlet response
     */
    private static HttpServletResponse initResponseHeader(final String contentType, final HttpServletResponse response, final String... headers) {
        // 分析headers参数
        String encoding = DEFAULT_ENCODING;
        boolean noCache = DEFAULT_NOCACHE;
        for (String header : headers) {
            String headerName = StringUtils.substringBefore(header, ":");
            String headerValue = StringUtils.substringAfter(header, ":");

            if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
                encoding = headerValue;
            } else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
                noCache = Boolean.parseBoolean(headerValue);
            } else {
                throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
            }
        }

        // 设置headers参数
        String fullContentType = contentType + ";charset=" + encoding;
        response.setContentType(fullContentType);
        if (noCache) {
            HeaderUtils.setNoCacheHeader(response);
        }

        return response;
    }
}
