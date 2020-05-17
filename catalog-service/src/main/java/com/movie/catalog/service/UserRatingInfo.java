package com.movie.catalog.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.movie.catalog.model.Rating;
import com.movie.catalog.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserRatingInfo {
		
		@Autowired
		private RestTemplate restTemplate; //making API call
	
		//Creating granularity for Hystrix, for fallback per service
		@HystrixCommand(fallbackMethod = "getFallbackUserRating")
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
