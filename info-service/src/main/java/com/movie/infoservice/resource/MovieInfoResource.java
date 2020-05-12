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
		return new Movie(movieId, "name");
	}
}
