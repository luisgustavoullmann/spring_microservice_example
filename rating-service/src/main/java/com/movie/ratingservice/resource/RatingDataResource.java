package com.movie.ratingservice.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.ratingservice.model.Rating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingDataResource {
	
	@GetMapping("/{movieId}")
	public Rating getRating(@PathVariable("movieId") String movieId) {
		//Hardcode, Id do filme e seu rating
		return new Rating(movieId, 4);
	}

}