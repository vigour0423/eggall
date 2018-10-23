package com.ddl.egg.web.platform.web.request;

import com.ddl.egg.log.util.ReadOnly;
import com.ddl.egg.web.platform.request.RemoteAddress;
import com.ddl.egg.web.platform.web.DeploymentSettings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@SuppressWarnings("unchecked")
public class RequestContextImpl implements RequestContext {
    private final ReadOnly<HttpServletRequest> request = new ReadOnly();
    private final ReadOnly<String> clientId = new ReadOnly();
    private final ReadOnly<String> requestId = new ReadOnly();
    private final ReadOnly<Date> requestDate = new ReadOnly();
    private final ReadOnly<String> action = new ReadOnly(); // the action method, controllerClass-method

    private DeploymentSettings deploymentSettings;
    private RequestURLHelper requestURLHelper;

    public String getClientRequestedFullURLWithQueryString() {
        return requestURLHelper.getClientRequestedFullURLWithQueryString();
    }

    public String getClientRequestedRelativeURLWithQueryString() {
        return requestURLHelper.getClientRequestedRelativeURLWithQueryString();
    }

    public String getClientRequestedRelativeURL() {
        return requestURLHelper.getClientRequestedRelativeURL();
    }

    public String getClientRequestServerName() {
        return request.value().getServerName();
    }

    public boolean isSecure() {
        return request.value().isSecure();
    }

    public RemoteAddress getRemoteAddress() {
        return RemoteAddress.create(request.value());
    }

    public String getClientId() {
        return clientId.value();
    }

    public String getRequestId() {
        return requestId.value();
    }

    public Date getRequestDate() {
        return requestDate.value();
    }

    public String getAction() {
        return action.value();
    }

    public String getRequestHeader(String name) {
        return request.value().getHeader(name);
    }

    public void setClientId(String clientId) {
        this.clientId.set(clientId);
    }

    public void setRequestId(String requestId) {
        this.requestId.set(requestId);
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate.set(requestDate);
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public void setHTTPRequest(HttpServletRequest request) {
        this.request.set(request);
        requestURLHelper = new RequestURLHelper(request, deploymentSettings);
    }

    @Autowired(required = false)
    public void setDeploymentSettings(DeploymentSettings deploymentSettings) {
        this.deploymentSettings = deploymentSettings;
    }

    public String getQueryString() {
        String query = requestURLHelper.getClientRequestedQueryString();
        return query == null ? "" : query;
    }


}
