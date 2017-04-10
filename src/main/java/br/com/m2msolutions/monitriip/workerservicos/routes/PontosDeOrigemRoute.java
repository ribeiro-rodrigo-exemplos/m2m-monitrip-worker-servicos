package br.com.m2msolutions.monitriip.workerservicos.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.DefaultErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Rodrigo Ribeiro on 10/04/17.
 */
@Component
public class PontosDeOrigemRoute extends RouteBuilder{

    @Autowired
    private CsvDataFormat csvFormat;

    @Override
    public void configure() throws Exception {

        onException(Exception.class).process(e -> {}).handled(true);

        from("file:src/main/resources/files?noop=true&delay=5s").
            unmarshal(csvFormat).
                split().
                    body().
                        process("pontosDeOrigemCSVProcess").
                        to("sql:classpath:sql/insert-ponto-origem.sql?dataSource=h2").
                        to("sql:select * from PONTOS_DE_ORIGEM where id=:#${body[id]}?dataSource=h2").
                        //log("${body}").
        end();
    }
}
