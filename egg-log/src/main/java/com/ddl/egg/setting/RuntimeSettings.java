package com.ddl.egg.setting;

public class RuntimeSettings {
    private static final RuntimeSettings INSTANCE = new RuntimeSettings();

    public static RuntimeSettings get() {
        return INSTANCE;
    }

    private RuntimeEnvironment environment = RuntimeEnvironment.rd;
    private String runtimeIp;

    public RuntimeEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(RuntimeEnvironment environment) {
        this.environment = environment;
    }

    public static RuntimeSettings getINSTANCE() {
        return INSTANCE;
    }

    public String getRuntimeIp() {
        return runtimeIp;
    }

    public void setRuntimeIp(String runtimeIp) {
        this.runtimeIp = runtimeIp;
    }
}
