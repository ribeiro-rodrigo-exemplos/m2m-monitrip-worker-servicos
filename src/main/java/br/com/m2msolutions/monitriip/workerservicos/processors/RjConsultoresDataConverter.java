package br.com.m2msolutions.monitriip.workerservicos.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * Created by Rodrigo Ribeiro on 16/02/17.
 */
@Component
public class RjConsultoresDataConverter implements Processor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {

        Date data = (Date) Optional
                                .ofNullable(
                                             exchange.getIn().getExchange().getProperty("dtSincronismo")
                                           ).orElse(new Date());

        logger.info(String.format("%s - %s",data,exchange.getProperty("idCliente")));
        exchange.setProperty("dtSincronismo",new SimpleDateFormat("yyyy-MM-dd").format(data));
        exchange.setProperty("dataEnvioFormatada",new SimpleDateFormat("yyMMdd").format(data));

    }
}
