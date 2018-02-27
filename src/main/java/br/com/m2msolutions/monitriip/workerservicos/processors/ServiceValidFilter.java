package br.com.m2msolutions.monitriip.workerservicos.processors;

import br.com.m2msolutions.monitriip.workerservicos.dto.ServicoDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rodrigo on 28/09/17.
 */
@Component
public class ServiceValidFilter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        List<ServicoDTO> servicos = (List) exchange.getIn().getBody();
        servicos = servicos
                        .stream()
                        .filter(s -> s.getLinha() != null)
                        .collect(Collectors.toList());

        exchange.getIn().setBody(servicos.size() > 0 ? servicos : null );
    }
}

