package br.com.m2msolutions.monitriip.workerservicos.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class GenerateDateProcess implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        Calendar calendar = Calendar.getInstance();
        Date hoje = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date amanha = calendar.getTime();

        SimpleDateFormat dataFormato = new SimpleDateFormat("yyyyMMdd");

        exchange.setProperty("dtSincronismo",hoje);
        exchange.setProperty("dtSincronismo2",amanha);
        exchange.setProperty("idHoje", exchange.getProperty("idCliente") + dataFormato.format(hoje));
        exchange.setProperty("idAmanha", exchange.getProperty("idCliente") + dataFormato.format(amanha));
    }
}
