package br.com.m2msolutions.monitriip.workerservicos.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Rodrigo Ribeiro on 16/02/17.
 */
@Component
@ConfigurationProperties(prefix = "rjconsultores")
public class RjConsultoresProperties {

    private String url;
    private String authorization;
    private String consumerPeriod;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getConsumerPeriod() {
        return consumerPeriod;
    }

    public void setConsumerPeriod(String consumerPeriod) {
        this.consumerPeriod = consumerPeriod;
    }
}
