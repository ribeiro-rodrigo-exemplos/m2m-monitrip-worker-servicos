package br.com.m2msolutions.monitriip.workerservicos.routes;

import br.com.m2msolutions.monitriip.workerservicos.aggregation.ServicoAggregationStrategy;
import br.com.m2msolutions.monitriip.workerservicos.dto.PontoDTO;
import br.com.m2msolutions.monitriip.workerservicos.properties.RjConsultoresProperties;
import br.com.m2msolutions.monitriip.workerservicos.properties.ServicoPersistenciaProperties;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Rodrigo Ribeiro on 18/02/17.
 */
@Component
public class ServicosRoute extends RouteBuilder {

    @Autowired
    private RjConsultoresProperties rjConsultoresProps;
    @Autowired
    private ServicoPersistenciaProperties servicoPersistenciaProps;
    @Autowired
    private ServicoAggregationStrategy servicoAggregationStrategy;
    @Autowired
    XStreamDataFormat xStreamDataFormat;

    @Override
    public void configure() throws Exception {

        from(String.format("timer://wakeup?fixedRate=true&period=%s&delay=10s",rjConsultoresProps.getConsumerPeriod())).
            to("sql:classpath:sql/find-connection-info.sql?dataSource=mysql").
                routeId("route-principal").
                    split().
                        body().
                            parallelProcessing().
                                setProperty("codConexao",simple("${body[cod_conexao]}")).
                                setProperty("codCliente",simple("${body[cod_cliente]}")).
                                setProperty("dtSincronismo",simple("${body[dt_sincronismo_servicos]}")).
                                setProperty("idCliente",simple("${body[id_cliente]}")).
                                to("direct:sendServices").
        end();

        from("direct:sendServices").
            routeId("route-send").
                transacted().
                    setProperty("collection",constant(servicoPersistenciaProps.getCollection())).
                    setProperty("action",constant(servicoPersistenciaProps.getAction())).
                    to("direct:loadServices").
                        unmarshal().string().
                            choice().
                                when(xpath("/servicoes/servico[count(retorno)='0']")).
                                    to("direct:mapPoints").
                                        marshal().
                                            json(JsonLibrary.Jackson).
                                        unmarshal().
                                            string().
                                                to("velocity:templates/servico-persistencia.vm").
                                                to("sql:classpath:sql/update-load-date.sql?dataSource=mysql").
                                                to(String.format("rabbitmq://%s&durable=true&autoDelete=false",
                                                    servicoPersistenciaProps.getUrlRabbitmq())).
                            endChoice().
        end();

        from("direct:loadServices").
            routeId("route-load").
                process("rjConsultoresDataConverter").
                setHeader("Authorization",constant(rjConsultoresProps.getAuthorization())).
                setHeader(Exchange.HTTP_METHOD, HttpMethods.GET).
                setHeader(Exchange.HTTP_PATH,simple("${property.codConexao}/${property.dataEnvioFormatada}/${property.codCliente}")).
                setBody(constant("")).
                to(String.format("http4://%s",rjConsultoresProps.getUrl())).
        end();

        from("direct:mapPoints").
            routeId("route-map-points").
            unmarshal(xStreamDataFormat).
            setProperty("listSize",simple("${body.size}")).
            setProperty("correlationId",simple("${exchangeId}")).
            split().
                body().
                setProperty("originalPayload",simple("${body}")).
                setProperty("idLocalidade",simple("${body.codOrigem}")).
                to(String.format("sql:classpath:sql/find-ponto.sql?dataSource=h2&outputType=SelectOne&outputClass=%s",
                        PontoDTO.class.getName())).
                transform(simple("${property.originalPayload.setPontoOrigem(${body})}")).
                setProperty("idLocalidade",simple("${property.originalPayload.codDestino}")).
                to(String.format("sql:classpath:sql/find-ponto.sql?dataSource=h2&outputType=SelectOne&outputClass=%s",
                        PontoDTO.class.getName())).
                transform(simple("${property.originalPayload.setPontoDestino(${body})}")).
            aggregate(simple("${property.correlationId}"),servicoAggregationStrategy).
                completionSize(simple("${property.listSize}")).
                setBody(simple("${body}")).
        end();
    }
}
