package br.com.m2msolutions.monitriip.workerservicos.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Created by Rodrigo Ribeiro on 10/04/17.
 */
@Component
public class PontosRoute extends RouteBuilder{

    @Autowired
    private CsvDataFormat csvFormat;

    @Override
    public void configure() throws Exception {

        onException(Exception.class).process(e -> {}).handled(true);

        from("file:src/main/resources/files?noop=true").
            log("Carregando pontos...").
            unmarshal(csvFormat).
                split().
                    body().
                        process("pontosCSVProcess").
                        to("sql:classpath:sql/insert-ponto.sql?dataSource=h2").
                        log("Ponto Carregado: ${body[localidade]} - ${body[municipio]}").
        end();
    }
}
