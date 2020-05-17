package com.movie.catalog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.movie.catalog.model.CatalogItem;
import com.movie.catalog.model.Movie;
import com.movie.catalog.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service //Bean for MovieCatalogResourse
public class MovieInfo {	
	
		@Autowired
		private RestTemplate restTemplate; //making API call
	
		//Creating granularity for Hystrix, for fallback per service
		@HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
				commandProperties = { //Search for Configuration Hystrix, because have a bunch of parameters
						@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"), //Timeout
						@HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="5"), //Last N request
						@HystrixProperty(name="circuitBreaker.errorThresholdPercentege", value="50"), //Percentage of error of last N request
						@HystrixProperty(name="circuitBreaker.spleepWindowInMilliseconds", value="5000"), //How long the circuit breaker it's going sleep
				})
		public CatalogItem getCatalogItem(Rating rating) {
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
		}
		
		//Fallback method
		public CatalogItem getFallbackCatalogItem(Rating rating) {
			//Hard code data for default response
			return new CatalogItem("Movie name not found", "", rating.getRating());		
		}

}
