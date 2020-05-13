package com.movie.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class CatalogServiceApplication {

	//CORE APP
	
	//You need the APIs model, to store the infos
	
	@Bean //Bean is a producer, so save this instance somewhere
	//RestTemplate will be deprecated, use the alternative WebClient
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	//Just create this Bean with in your projet you are using reactive programmimg
	@Bean
	public WebClient.Builder getWebClientBuilder(){
		return WebClient.builder();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}
	
	//Step 4 -> After created Microservices and core, implement Eureka
	//Eureka Client is for APIs and Core, add in each pom.xml
	//Eureka Server, discovery-server, it's just to know APIs for the Core

}
