package com.movie.catalog.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.movie.catalog.model.Rating;
import com.movie.catalog.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class UserRatingInfo {
		
		@Autowired
		private RestTemplate restTemplate; //making API call
	
		//Creating granularity for Hystrix, for fallback per service
		@HystrixCommand(fallbackMethod = "getFallbackUserRating",
				commandProperties = { //Search for Configuration Hystrix, because have a bunch of parameters
						@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"), //Timeout
						@HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="5"), //Last N request
						@HystrixProperty(name="circuitBreaker.errorThresholdPercentege", value="50"), //Percentage of error of last N request
						@HystrixProperty(name="circuitBreaker.spleepWindowInMilliseconds", value="5000"), //How long the circuit breaker it's going sleep
				})
		public UserRating getUserRating(@PathVariable("userId") String userId) {
			return restTemplate.getForObject("http://movie-rating-service/ratingsdata/users/" + userId, UserRating.class); //see UserRating (API and copy the model to Core)
		}
		
		//Fallback method
		public UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
			//Hard code data for default response
			UserRating userRating = new UserRating();
			userRating.setUserId(userId);
			userRating.setUserRating(Arrays.asList(
					new Rating("0", 0)
					));
			return userRating;
		}
}
