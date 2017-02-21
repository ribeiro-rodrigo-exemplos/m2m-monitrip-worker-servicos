package br.com.m2msolutions.monitriip.workerservicos.config;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
/**
 * Created by Rodrigo Ribeiro on 15/02/17.
 */
@Configuration
public class DatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource")
    MysqlConnectionPoolDataSource mysql(){
        return new MysqlConnectionPoolDataSource();
    }

    @Bean
    DataSourceTransactionManager txManager(){
        return new DataSourceTransactionManager(mysql());
    }
}
