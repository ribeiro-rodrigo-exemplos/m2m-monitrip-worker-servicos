package br.com.m2msolutions.monitriip.workerservicos.routes;

import br.com.m2msolutions.monitriip.workerservicos.aggregation.ServicoAggregationStrategy;
import br.com.m2msolutions.monitriip.workerservicos.dto.PontoDTO;
import br.com.m2msolutions.monitriip.workerservicos.dto.ServicoDTO;
import br.com.m2msolutions.monitriip.workerservicos.properties.MongoProperties;
import br.com.m2msolutions.monitriip.workerservicos.properties.RjConsultoresProperties;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Rodrigo Ribeiro on 18/02/17.
 */
@Component
public class ServicosRoute extends RouteBuilder {

    @Value("${url-zona}")
    private String urlZona;
    @Autowired
    private RjConsultoresProperties rjConsultoresProps;
    @Autowired
    private ServicoAggregationStrategy servicoAggregationStrategy;
    @Autowired
    XStreamDataFormat xStreamDataFormat;
    @Autowired
    private MongoProperties mongoProperties;

    @Override
    public void configure() throws Exception {

        from(String.format("timer://wakeup?fixedRate=true&period=%s&delay=25s",rjConsultoresProps.getConsumerPeriod())).
            routeId("route-principal").
            setProperty("urlZona",constant(urlZona)).
            to("sql:classpath:sql/find-connection-info.sql?dataSource=mysql").
                split().
                    body().
                        setProperty("codConexao",simple("${body[cod_conexao]}")).
                        setProperty("codCliente",simple("${body[cod_cliente]}")).
                        setProperty("idCliente",simple("${body[id_cliente]}")).
                        process("generateDateProcess").
                        setProperty("id", simple("${property.idHoje}")).
                        to("direct:sendServices").
                        setProperty("id", simple("${property.idAmanha}")).
                        setProperty("dtSincronismo",simple("${property.dtSincronismo2}")).
                        to("direct:sendServices").
        end();

        from("direct:sendServices").
            routeId("route-send").
                to("direct:loadServices").
                    unmarshal().string().
                        choice().
                            when(xpath("/servicoes/servico[count(retorno)='0']")).
                                to("direct:mapPoints").
                                process("serviceValidFilter").
                                filter(body().isNotNull()).
                                    marshal().
                                        json(JsonLibrary.Jackson).
                                    unmarshal().
                                        string().
                                            to("velocity:templates/servico-persistencia.vm").
                                            to(String.format("mongodb:monitriipDb?database=%s&collection=ServicosMonitriip&operation=save",mongoProperties.getDatabase())).

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
                process("servicoTransformProcess").
                setProperty("idLocalidade",simple("${body.origem}")).
                to(String.format("sql:classpath:sql/find-ponto.sql?dataSource=h2&outputType=SelectOne&outputClass=%s",
                        PontoDTO.class.getName())).
                transform(simple("${property.originalPayload.setPontoOrigem(${body})}")).
                setProperty("idLocalidade",simple("${property.originalPayload.destino}")).
                to(String.format("sql:classpath:sql/find-ponto.sql?dataSource=h2&outputType=SelectOne&outputClass=%s",
                        PontoDTO.class.getName())).
                transform(simple("${property.originalPayload.setPontoDestino(${body})}")).
                setProperty("linha",simple("${property.originalPayload.linha}")).
                process( e -> e.setProperty("linha",((String)e.getProperty("linha")).replaceAll("-",""))).
                to(String.format("sql:classpath:sql/find-linha.sql?dataSource=h2&outputType=SelectOne")).
                transform(simple("${property.originalPayload.setLinha(${body[linha]})}")).
            aggregate(simple("${property.correlationId}"),servicoAggregationStrategy).
                completionSize(simple("${property.listSize}")).
                setBody(simple("${body}")).
        end();
    }
}
