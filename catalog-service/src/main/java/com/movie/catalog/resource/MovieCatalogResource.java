package com.movie.catalog.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.movie.catalog.model.CatalogItem;
import com.movie.catalog.model.Movie;
import com.movie.catalog.model.Rating;

@RestController //API
@RequestMapping("/catalog") //URL
public class MovieCatalogResource {	

	@GetMapping("/{userId}") //Passando userId como variável
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		
		//Creating a instante for the RestTemplate make this calls
		RestTemplate restTemplate = new RestTemplate();
		//Function to make a rest call, parameters (URL and class)
		//gets back a string, so you provide a class with the same properties
		//as the JSON, RestTemplate is going to create the instance of the class
		//populate those forms for you and give you a fully object
		Movie movie = restTemplate.getForObject("http://localhost:8082/movies/foo", Movie.class);			
		
		
		//The communication happens through Rest template,
		//what you get back is a string,
		//but you need objects (for a particularly class) to detail with the data
		//and that's why we have to copy the models from APIs
		List<Rating> ratings = Arrays.asList(
				//harcode the response we got from data API
				new Rating("1234", 4), //movieId and rate
				new Rating("6789", 3)
		);
		
		//Using RestTemplate to make the API call
		//stream, group of data
		// map then and 
		return ratings.stream().map(rating -> {
			//new CatalogItem("Spider-Man", "About a guy", 4);
			return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
		
		})
		.collect(Collectors.toList()); //Give a list for who have been calling
		
		// --> 1 - Get all rated movies IDs
		
		// --> 2 - For each movieId, call MovieInfoService and get details
		
		// --> 3 - Final step, put then all together
		
	}
}
