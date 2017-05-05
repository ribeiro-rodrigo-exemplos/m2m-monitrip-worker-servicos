package br.com.m2msolutions.monitriip.workerservicos.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Rodrigo Ribeiro on 10/04/17.
 */
@Component
public class PontosCSVProcess implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        Map<String,String> body = exchange.getIn().getBody(Map.class);

        body.put("uf",body.get("uf").trim());
    }
}
