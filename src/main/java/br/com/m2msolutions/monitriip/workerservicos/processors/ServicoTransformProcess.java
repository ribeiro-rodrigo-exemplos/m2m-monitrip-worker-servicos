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
        dto.setHorarioSaida(validaHorario(dto.getHorarioSaida(), dto));
    }

    private String removeZeros(String valor){
        if(valor != null && !valor.isEmpty())
            return Integer.valueOf(valor).toString();
        else
            return "";
    }

    private String validaHorario(String horario, ServicoDTO dto){
        if(horario.length() < 3){
            return "00" + horario;
        }else if(horario.length() < 4){
            return "0" + horario;
        }else{
            return dto.getHorarioSaida();
        }
    }

}
