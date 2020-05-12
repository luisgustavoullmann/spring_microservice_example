package com.movie.catalog.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	//Creating a instance for the RestTemplate make this calls
	//Using Bean to create a single instance
	@Autowired //Dependency injection, fetch bean annotation
	//RestTemplate will be deprecated, use the alternative WebClient
	private RestTemplate restTemplate; //the name doesn't matter, the type matters
	
	
	//@Autowired
	//private WebClient.Builder webClientBuilder;//Dependency injection for reactive program

	@GetMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){		
		
		//Using WebClient as a alternative for the RestTemplate
		//WebClient doesn't exist in the class path, 
		//is based on reacting programming space or asynchronous programming
		//Spring Initializr - In pom.xml, import dependecy reactive web with Spring Webflux
		//WebClient.Builder builder = WebClient.builder(); create bean for this	
		
		//The communication happens through Rest template,
		//what you get back is a string,
		//but you need objects (for a particularly class) to detail with the data
		//and that's why we have to copy the models from APIs
		List<Rating> ratings = Arrays.asList(
				//hard code the response we got from data API
				new Rating("1234", 4), //movieId and rate
				new Rating("6789", 3)
		);
		
		//Using RestTemplate to make the API call
		//stream, group of data
		// map then and 
		return ratings.stream().map(rating -> {
			//hard coded new CatalogItem("Spider-Man", "About a guy", 4);
			
			//Function to make a rest call, parameters (URL and class)
			//gets back a string, so you provide a class with the same properties
			//as the JSON, RestTemplate is going to create the instance of the class
			//populate those forms for you and give you a fully object
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
			
			//Asynchronous - Reacting programmimg - Webflux
			/*Instance in reactive programming, the line above will be replaced by this:
			Movie movie = webClientBuilder.build() //creating new instance
				.get() //calling the method (Get or Post..)
				.uri("http://localhost:8082/movies/" + rating.getMovieId())
				.retrieve() //Fetch the data, giving you what the method is, so go do it.
				//bodyToMono - Something like a promise (JS), will get to you what you want in a future time
				//creating an asynchronous object. Or is like you're holding an empty container to use it in a future moment.
				.bodyToMono(Movie.class) //elementClass - Get whatever you calling and give me the instance of Movie.class
				.block(); //block when you already have the execution/object
			*/
			
			return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
		
		})
		.collect(Collectors.toList()); //Give a list for who have been calling
		
		// --> 1 - Get all rated movies IDs
		
		// --> 2 - For each movieId, call MovieInfoService and get details
		
		// --> 3 - Final step, put then all together
		
	}
}
