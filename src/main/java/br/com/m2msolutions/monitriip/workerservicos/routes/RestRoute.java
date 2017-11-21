package br.com.m2msolutions.monitriip.workerservicos.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by rodrigo on 24/10/17.
 */
@Component
public class RestRoute extends RouteBuilder {

    @Value("${server.port}")
    private Integer port;
    @Value("${server.max-threads}")
    private String maxThreads;

    @Override
    public void configure() throws Exception {
        restConfiguration().
                component("jetty").
                    host("0.0.0.0").
                    port(port).
                    bindingMode(RestBindingMode.auto).
                    contextPath("/api").
                    endpointProperty("maxThreads",maxThreads);

        rest("/v1/pontos").
                get().
                to("direct:obter-pontos-route");

        rest("/v1/clientes").
                get("/{idCliente}/prefixosLinha").
                to("direct:obter-prefixos-route");


    }
}
