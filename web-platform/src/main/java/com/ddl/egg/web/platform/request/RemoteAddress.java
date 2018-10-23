package com.ddl.egg.web.platform.request;


import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


public class RemoteAddress {
    static final String HTTP_HEADER_X_FORWARDED_FOR = "x-forwarded-for";
    static final String HTTP_HEADER_CLIENT_IP = "client-ip";
    static final String HTTP_HEADER_X_CLIENT_IP = "x-client-ip";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");

    public static RemoteAddress create(HttpServletRequest request) {
        String directRemoteAddress = request.getRemoteAddr();
        String clientIpHeader = getIp(request.getHeader(HTTP_HEADER_CLIENT_IP)); //  netscaler doesn't use x-forwarded-for
        String xForwardedFor = request.getHeader(HTTP_HEADER_X_FORWARDED_FOR);
        String xClientIp = getIp(request.getHeader(HTTP_HEADER_X_CLIENT_IP)); //蓝汛cdn真实用户ip头
        String forwardIPHeader = StringUtils.hasText(clientIpHeader) ? clientIpHeader : StringUtils.hasText(xClientIp) ? xClientIp : xForwardedFor;
        return new RemoteAddress(directRemoteAddress, forwardIPHeader);
    }

    static String getIp(String sourceString) {
        if (!StringUtils.hasText(sourceString)) {
            return null;
        }
        if (!IP_PATTERN.matcher(sourceString.trim()).matches()) {
            return null;
        }
        return sourceString.trim();
    }

    private final String remoteAddress;
    // for original ip if there is proxy
    private final String xForwardedFor;

    RemoteAddress(String remoteAddress, String xForwardedFor) {
        this.remoteAddress = remoteAddress;
        this.xForwardedFor = xForwardedFor;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getXForwardedFor() {
        return xForwardedFor;
    }

    /**
     * get actual client ip, being aware of proxy
     *
     * @return the ip of client from request
     */
    public String getClientIP() {
        if (!StringUtils.hasText(xForwardedFor))
            return remoteAddress;
        int index = xForwardedFor.indexOf(',');
        if (index > 0)
            return xForwardedFor.substring(0, index);
        return xForwardedFor;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(xForwardedFor)) {
            builder.append(xForwardedFor).append(", ");
        }
        builder.append(remoteAddress);
        return builder.toString();
    }
}
