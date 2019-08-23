package com.ts.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/*
Clase encargada de iniciar el servidor,
esta clase es propia de Spring Boot
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ApigatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApigatewayApplication.class, args);
  }

}
