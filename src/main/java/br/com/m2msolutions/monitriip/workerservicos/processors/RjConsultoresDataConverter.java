package br.com.m2msolutions.monitriip.workerservicos.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * Created by Rodrigo Ribeiro on 16/02/17.
 */
@Component
public class RjConsultoresDataConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        Date data = (Date) Optional
                                .ofNullable(
                                             exchange.getIn().getExchange().getProperty("dtSincronismo")
                                           ).orElse(new Date());

        exchange.setProperty("dtSincronismo",new SimpleDateFormat("YYYY-MM-dd").format(data));
        exchange.setProperty("dataEnvioFormatada",new SimpleDateFormat("YYMMdd").format(data));
    }
}
