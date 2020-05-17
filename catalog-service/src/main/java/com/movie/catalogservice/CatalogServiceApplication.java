package com.movie.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker //Hystrix dependency
@EnableHystrixDashboard //Dashboard pom
public class CatalogServiceApplication { 

	//CORE APP
	
	//You need the APIs model, to store the information
	
	@Bean //Bean is a producer, so save this instance somewhere
	//RestTemplate will be deprecated, use the alternative WebClient
	@LoadBalanced //Needs Eureka (pom.xml) - Telling don't go directly to the URL because it's a hint, 
	//first discover. And now the RestTemplate will discover the hint in the URL
	//because Eureka knows spring.application.name not the URL name
	//so change the URL for the spring.application.name in MovieCatalogResources
	public RestTemplate getRestTemplate() {		
		// - The problem is, if you have one service consuming all resources
		//everything could be slow and you will have a break in the system
		//cause every service have to wait for the threads (stopped) happens
		
		/* - First solution/way to set the timeout (the second is better)
		//This class (Http) it's what allows to create the timeout
		//Because you still have threads after 3s in this case,
		//it's a problem for short-term and also long-term
		//cause if you receive a request for a second, the breaking still happens
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(3000); //block/set 3s for timeout
		return new RestTemplate(clientHttpRequestFactory); //this solution don't solve the problem (there's a second way to solve it)
		*/
		
		// - Solution, you have to give a time for the service to recover
		//and start to call again in a near future.
		//This is circuit breaker pattern for fault tolerance
		//technically you can implement a circuit breaker in every microservice you have been calling
		//1) detect something is wrong
		//2) take temporary steps to avoid the situation getting worse (stop to send request temporarily)
		//3) deactivate the problem component so that it doesn't affect downstream components
		//you can setup the circuit breaker to deal with timeout/error
		//but the logic is, what is the trigger for the circuit break to be smart enough to deal with a bunch of user cases
		//What is the parameters for the circuit breaker: (you have to give an answer this question) 
		//A) When does the circuit trip? Last N request to consider for the decision 
		//(in every fail, can check the last N request/looking the scope to see it's ok or not)
		//B) How many of those should fail?
		//C) Timeout duration itself - what point of time do you consider it's a fail?
		//When does the circuit get to normal? How long after a circuit trip to try again?
		//when everything is bad, the worse way it's keep sending requests!
		//For a complete list of config params (for circuit breaker like Hystrix) refer:
		//https://github.com/Netflix/Hystrix/wiki/Configuration
		//And how do I change the config dynamically? (search the answer) 		
		
		//We need a fallback - throw an error is not an option
		//return a fallback "default" response or
		//save previous responses (cache) and use that when possible
		
		//Hystrix:
		//Implements circuit breaker pattern so you don't have to
		//Give it the configuration params and it goes the work
		//Steps to add hystrix to a Spring Boot microservice:
		//1) Add the Maven spring-cloud-starter-netflix-hystrix dependency
		//2) Go to the main class an add this annotation @EnableCircuitBreaker
		//3) Add @HystrixCommand to methods that need circuit breakers
		//4) Configure Hystrix behavior, provide the parameters
		//How does Hystrix works? Wrapping your class in a proxy class
		//test the response.
		return new RestTemplate();
	}
	
	//Just create this Bean with in your project you are using reactive programming
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

// Fault Tolerance - if a service was down, that will break all system?
// Resilience - How many times that service should have to going up/down to be acceptable? 

/* Change because of some issues:
 * <!-- <spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>  -->
		<spring-cloud.version>Hoxton.M2</spring-cloud.version>
*/
