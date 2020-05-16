package com.movie.catalog.resource;

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
import com.movie.catalog.model.UserRating;
import com.movie.infoservice.resource.Arrays;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@RestController //API
@RequestMapping("/catalog") //URL
public class MovieCatalogResource {	
	
	//Creating a instance for the RestTemplate make this calls
	//Using Bean to create a single instance
	@Autowired //Dependency injection, fetch bean annotation
	//RestTemplate will be deprecated, use the alternative WebClient
	private RestTemplate restTemplate; //the name doesn't matter, the type matters
	
	//If you have multiple services doing the same service (Like Stock information Yahoo/IEX)
	//You can access port, IDs etc
	//private DiscoveryClient discoveryClient;
	
	
	//@Autowired
	//private WebClient.Builder webClientBuilder;//Dependency injection for reactive program

	@GetMapping("/{userId}")
	@HystrixCommand(fallbackMethod = "getFallbackCatalog") //version 2.2.2 - I had some problems without mention the version
	//This method makes every call, so it will need a circuit breaker
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){		
		
		//Using WebClient as a alternative for the RestTemplate
		//WebClient doesn't exist in the class path, 
		//is based on reacting programming space or asynchronous programming
		//Spring Initializr - In pom.xml, import dependency reactive web with Spring Webflux
		//WebClient.Builder builder = WebClient.builder(); create bean for this	
		
		// --> 1 - Get all rated movies IDs
		//The communication happens through Rest template,
		//what you get back is a string,
		//but you need objects (for a particularly class) to detail with the data
		//and that's why we have to copy the models from APIs
		//With you want a list of objects, see UserRating model/API
		//URL Before @LoadBalance - http://localhost:8083/ratingsdata/users/ 
		// After - name is in application.properties | see @LoadBalance and needs Eureka Client/Spring Cloud
		UserRating ratings = restTemplate.getForObject("http://movie-rating-service/ratingsdata/users/" + userId, UserRating.class); //see UserRating (API and copy the model to Core)
		
		//Using RestTemplate to make the API call
		//stream, group of data
		// map then and 
		return ratings.getUserRating().stream().map(rating -> {
			//hard coded new CatalogItem("Spider-Man", "About a guy", 4);
			
			// --> 2 - For each movieId, call MovieInfoService and get details			
			
			//Function to make a rest call, parameters (URL and class)
			//gets back a string, so you provide a class with the same properties
			//as the JSON, RestTemplate is going to create the instance of the class
			//populate those forms for you and give you a fully object
			//URL before @LoadBalance - http://localhost:8082/movies/
			//// After - name is in application.properties | see @LoadBalance and needs Eureka Client/Spring Cloud
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			
			//Asynchronous - Reacting programming - Webflux
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
			// --> 3 - Final step, put then all together
			return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
		
		})
		.collect(Collectors.toList()); //Give a list for who have been calling
				
	}
	
	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
		return new Arrays.asList(new CatalogItem("No movie", "", 0));
	}
}
