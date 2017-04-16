package br.com.m2msolutions.monitriip.workerservicos.aggregation;

import br.com.m2msolutions.monitriip.workerservicos.dto.ServicoDTO;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * Created by Rodrigo Ribeiro on 15/04/17.
 */
@Component
public class ServicoAggregationStrategy extends AbstractListAggregationStrategy<ServicoDTO> {

    @Override
    public ServicoDTO getValue(Exchange exchange) {
        return exchange.getProperty("originalPayload",ServicoDTO.class);
    }
}
