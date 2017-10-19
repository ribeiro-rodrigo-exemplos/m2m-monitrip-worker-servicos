package br.com.m2msolutions.monitriip.workerservicos.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Rodrigo Ribeiro on 10/04/17.
 */
@Component
public class PontosRoute extends RouteBuilder{

    @Autowired
    @Qualifier("csvPontoFormat")
    private CsvDataFormat csvFormat;

    @Value("${external-resources}")
    private String resourcesPath;

    @Override
    public void configure() throws Exception {

        onException(Exception.class).process(e -> {}).handled(true);

        from(String.format("file:%s?noop=true&fileName=pontos.csv",resourcesPath)).
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
