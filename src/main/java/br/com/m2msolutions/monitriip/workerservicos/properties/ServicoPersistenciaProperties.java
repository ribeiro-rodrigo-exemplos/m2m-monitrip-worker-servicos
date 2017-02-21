package br.com.m2msolutions.monitriip.workerservicos.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Rodrigo Ribeiro on 16/02/17.
 */
@Component
@ConfigurationProperties(prefix = "servicoPersistencia")
public class ServicoPersistenciaProperties {

    @Value("${servicoPersistencia.rabbitmq.url}")
    private String urlRabbitmq;
    private String collection;
    private String action;

    public String getUrlRabbitmq() {
        return urlRabbitmq;
    }

    public void setUrlRabbitmq(String urlRabbitmq) {
        this.urlRabbitmq = urlRabbitmq;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
