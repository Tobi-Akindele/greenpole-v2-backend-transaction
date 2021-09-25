package com.ap.greenpole.transactioncomponent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 8/13/2020 2:09 AM
 */
@Configuration
@ConfigurationProperties("greenpole.transaction.component.service")
public class ApplicationPropertiesConfig {

    private String filePath;

    private String notificationServiceBaseUrl;

    private String httpTimeOut;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getNotificationServiceBaseUrl() {
        return notificationServiceBaseUrl;
    }

    public void setNotificationServiceBaseUrl(String notificationServiceBaseUrl) {
        this.notificationServiceBaseUrl = notificationServiceBaseUrl;
    }

    public String getHttpTimeOut() {
        return httpTimeOut;
    }

    public void setHttpTimeOut(String httpTimeOut) {
        this.httpTimeOut = httpTimeOut;
    }
}
