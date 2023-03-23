package com.ediweb.interview.documentconversion.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {
    private DataSource dataSource;
    public LiquibaseConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.yaml");

        return liquibase;
    }
}
