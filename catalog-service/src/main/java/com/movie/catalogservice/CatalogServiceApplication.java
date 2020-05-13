package com.movie.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class CatalogServiceApplication {

	//CORE APP
	
	//You need the APIs model, to store the infos
	
	@Bean //Bean is a producer, so save this instance somewhere
	//RestTemplate will be deprecated, use the alternative WebClient
	@LoadBalanced //Needs Eureka (pom.xml) - Telling don't go directly to the URL because it's a hint, 
	//first discover. And now the RestTemplate will discover the hint in the URL
	//because Eureka knows spring.application.name not the URL name
	//so change the URL for the spring.application.name in MovieCatalogResources
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	//Just create this Bean with in your projet you are using reactive programmimg
	/*@Bean
	public WebClient.Builder getWebClientBuilder(){
		return WebClient.builder();
	}*/
	
	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}
	
	//Step 4 -> After created Microservices and core, implement Eureka
	//Eureka Client is for APIs and Core, add in each pom.xml
	//Eureka Server, discovery-server, it's just to know APIs for the Core

}
