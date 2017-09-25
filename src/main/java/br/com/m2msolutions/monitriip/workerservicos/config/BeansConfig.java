package br.com.m2msolutions.monitriip.workerservicos.config;

import br.com.m2msolutions.monitriip.workerservicos.dto.ServicoDTO;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.thoughtworks.xstream.XStream;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Rodrigo Ribeiro on 15/02/17.
 */
@Configuration
public class BeansConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource")
    MysqlConnectionPoolDataSource mysql(){
        return new MysqlConnectionPoolDataSource();
    }

   @Bean
   DataSource h2(){
       return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .addScript("sql/create-pontos-table.sql")
                    .addScript("sql/create-linhas-table.sql")
                    .build();
   }

    @Bean
    DataSourceTransactionManager txManager(){
        return new DataSourceTransactionManager(mysql());
    }

    @Bean
    CsvDataFormat csvPontoFormat(){
        CsvDataFormat csvFormat = new CsvDataFormat();
        csvFormat.setDelimiter(';');
        csvFormat.setHeader(new String[]{"id","localidade","municipio","uf"});
        csvFormat.setUseMaps(true);

        return csvFormat;
    }

    @Bean
    CsvDataFormat csvLinhaFormat(){
        CsvDataFormat csvDataFormat = new CsvDataFormat();
        csvDataFormat.setDelimiter(';');
        csvDataFormat.setHeader(new String[]{"linha","id_cliente"});
        csvDataFormat.setUseMaps(true);

        return csvDataFormat;
    }

    @Bean
    XStreamDataFormat xStreamDataFormat(){
        XStream xStream = new XStream();
        xStream.alias("servicoes",List.class);
        xStream.alias("servico", ServicoDTO.class);

        return new XStreamDataFormat(xStream);
    }
}
