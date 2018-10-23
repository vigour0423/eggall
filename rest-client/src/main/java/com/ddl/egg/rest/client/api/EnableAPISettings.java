package com.ddl.egg.rest.client.api;

import java.util.List;
import java.util.Map;

public class EnableAPISettings {

    private Map<String, String> serverURLMapping;
    private List<String> packageNameList;
    private String clientId;
    private String clientKey;
    private boolean validateStatusCode;
    private boolean useMock = false;
    private boolean enableApiLog = false;

    public Map<String, String> getServerURLMapping() {
        return serverURLMapping;
    }

    public void setServerURLMapping(Map<String, String> serverURLMapping) {
        this.serverURLMapping = serverURLMapping;
    }

    public List<String> getPackageNameList() {
        return packageNameList;
    }

    public void setPackageNameList(List<String> packageNameList) {
        this.packageNameList = packageNameList;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public boolean isValidateStatusCode() {
        return validateStatusCode;
    }

    public void setValidateStatusCode(boolean validateStatusCode) {
        this.validateStatusCode = validateStatusCode;
    }

    public void enableMock() {
        useMock = true;
    }

    public boolean getUseMock() {
        return useMock;
    }

    public void enableApiLog() {
        enableApiLog = true;
    }

    public boolean isEnableApiLog() {
        return enableApiLog;
    }
}
