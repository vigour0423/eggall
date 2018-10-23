package com.ddl.egg.web.platform.web.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author mark
 */
@XmlRootElement(name = "baseResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseResponse {
    @XmlElement(name = "statusCode")
    private String statusCode = "200";
    @XmlElement(name = "msg")
    private String message = "success";
    @XmlElement(name = "stackTrace")
    private String stackTrace;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
