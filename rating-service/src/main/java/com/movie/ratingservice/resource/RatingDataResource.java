package com.movie.ratingservice.resource;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.ratingservice.model.Rating;
import com.movie.ratingservice.model.UserRating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingDataResource {
	/* Microserves precisam rodar em portas diferentes,
	 * não esqueça de configurar as portas no resources/application.properties*/
	
	@GetMapping("/{movieId}")
	public Rating getRating(@PathVariable("movieId") String movieId) {
		//Hardcode, Id do filme e seu rating
		return new Rating(movieId, 4);
	}
	
	@GetMapping("users/{userId}")//calling the list of ratings by userId
	//Instead return type List<>, we created this list in UserRating model
	public UserRating getUserRating(@PathVariable("userId") String userId) {
		//You can't return a list, change to object
		List<Rating> ratings = Arrays.asList(
				//hard code the response we got from data API
				new Rating("1234", 4), //movieId and rate
				new Rating("6789", 3)
		);
		//Returning a object of rating - after create UseRating model
		UserRating userRating = new UserRating();
		userRating.setUserRating(ratings);
		return userRating;
	}

}
