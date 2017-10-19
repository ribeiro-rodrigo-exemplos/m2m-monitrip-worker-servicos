package br.com.m2msolutions.monitriip.workerservicos.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by rodrigo on 25/09/17.
 */
@Component
public class LinhasRoute extends RouteBuilder{

    @Autowired
    @Qualifier("csvLinhaFormat")
    private CsvDataFormat csvDataFormat;

    @Value("${external-resources}")
    private String resourcesPath;

    @Override
    public void configure() throws Exception {
        from(String.format("file:%s?noop=true&fileName=linhas.csv",resourcesPath)).
            log("Carregando linhas...").
            unmarshal(csvDataFormat).
                split().
                    body().
                        to("sql:classpath:sql/insert-linha.sql?dataSource=h2").
                        log("Linha Carregada: ${body[id_cliente]} - ${body[linha]}").
        end();
    }
}
