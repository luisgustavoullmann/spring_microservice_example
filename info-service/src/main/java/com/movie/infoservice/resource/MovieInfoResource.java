package com.movie.infoservice.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.infoservice.model.Movie;

@RestController
@RequestMapping("/movies")
public class MovieInfoResource {
	
	/* Microserves precisam rodar em portas diferentes,
	 * não esqueça de configurar as portas no resources/application.properties*/
	
	@GetMapping("{movieId}")
	public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
		//Hardcode movie
		return new Movie(movieId, "name", "description");
	} //Part2 - Usando MovieResource para pegar dados do MovieId e evitar o hard code
	
	//Add the dependency to Eureka on pom.xml
	//Don't forget the version in dependencyManagement
	/*<properties>
		<java.version>11</java.version>
		<!-- <spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>  -->
		<spring-cloud.version>Hoxton.M2</spring-cloud.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
			<!-- <version>2.2.2.RELEASE</version> -->
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

	<repositories>
		<repository>
			<id>spring-milestones</id>
			<name>Spring Milestones</name>
			<url>https://repo.spring.io/milestone</url>
		</repository>
	</repositories>*/
}
