package com.movie.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //Tells Spring this is an Eureka Server
public class DiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
	}
	//Step 4 -> After created Microservices and core, implement Eureka
	//Eureka Client is for APIs and Core, add in each pom.xml
	//Eureka Server, discovery-server, it's just to know APIs for the Core
	
	/* 	If you're using STS, you have to add
		server.port=8761
		eureka.client.register-with-eureka=false
		eureka.client.fetch-registry=false*/
	
	//https://mvnrepository.com/
	//You have to add some libs in pom.xml
	//javax.xml.bind - jaxb-api - 2.3.0
	//com.sun.xml.bind - jaxb-impl - 2.3.0
	//org.glassfish.jaxb - jaxb-runtime - 2.3.0
	//javax.activation - activation - 1.1.1
	
	//If you run Java 11 without those dependencies
	//you'll have an error!
	/*<dependency>
			<groupId>javax.xml.bind</groupId>
			<artifactId>jaxb-api</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>com.sun.xml.bind</groupId>
			<artifactId>jaxb-impl</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish.jaxb</groupId>
			<artifactId>jaxb-runtime</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>javax.activation</groupId>
			<artifactId>activation</artifactId>
			<version>1.1.1</version>
		</dependency>*/
}
