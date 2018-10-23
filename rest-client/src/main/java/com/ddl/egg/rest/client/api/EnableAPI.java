package com.ddl.egg.rest.client.api;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables rest api auto proxy.This module will scan the rest interface jar,find out @API annotated interface
 * and create a proxy class that encapsulation the details of RESTServiceClient request rest api.
 * To be used as follows:
 * <pre class="code">
 * &#064;EnableAPI
 * &#064;Configuration
 * public class AppConfig{
 * &#064;Bean
 * public EnableAPISettingsCollection enableAPISettingsCollection()
 * {
 * EnableAPISettingsCollection enableAPISettingsCollection=new EnableAPISettingsCollection();
 * EnableAPISettings enableAPISettings=new EnableAPISettings();
 * enableAPISettings.setServerURL("your api server url");
 * enableAPISettings.setPackageName("your api package name");
 * enableAPISettings.setClientId("your api clientId");
 * enableAPISettings.setClientKey("your api clientKey");
 * enableAPISettingsCollection.add(enableAPISettings);
 * return enableAPISettingsCollection;
 * }
 * }
 * </pre>
 *
 * @author anonymous
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableAPISelector.class)
@Inherited
public @interface EnableAPI {
    AdviceMode mode() default AdviceMode.PROXY;
}
