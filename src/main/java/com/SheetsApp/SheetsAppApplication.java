package com.SheetsApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

//@EnableAutoConfiguration
@ComponentScan(basePackages={"com.SheetsApp"})
//@EnableJpaRepositories(basePackages="com.SheetsApp.repository")
@EnableTransactionManagement
@EntityScan(basePackages="com.SheetsApp.model")
public class SheetsAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(SheetsAppApplication.class, args);
	}

	@Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
