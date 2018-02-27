package br.com.m2msolutions.monitriip.workerservicos.processors;

import br.com.m2msolutions.monitriip.workerservicos.dto.ServicoDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ServicoTransformProcess implements Processor {

    @Override
    public void process(Exchange exchange) {

        ServicoDTO dto = (ServicoDTO) exchange.getProperty("originalPayload");
        dto.setOrigem(removeZeros(dto.getOrigem()));
        dto.setDestino(removeZeros(dto.getDestino()));
    }

    private String removeZeros(String valor){
        if(valor != null && !valor.isEmpty())
            return Integer.valueOf(valor).toString();
        else
            return "";
    }
}
