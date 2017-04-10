package br.com.m2msolutions.monitriip.workerservicos.config;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

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
                    .addScript("sql/create-pontos-de-origem-table.sql")
                    .build();
   }

    @Bean
    DataSourceTransactionManager txManager(){
        return new DataSourceTransactionManager(mysql());
    }

    @Bean
    CsvDataFormat csvFormat(){
        CsvDataFormat csvFormat = new CsvDataFormat();
        csvFormat.setDelimiter(';');
        csvFormat.setHeader(new String[]{"id","localidade","municipio","uf"});
        csvFormat.setUseMaps(true);

        return csvFormat;
    }
}
