package com.movie.catalog.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.model.CatalogItem;
import com.movie.catalog.model.UserRating;
import com.movie.catalog.service.MovieInfo;
import com.movie.catalog.service.UserRatingInfo;

@RestController //API
@RequestMapping("/catalog") //URL
public class MovieCatalogResource {	
	
	//Creating a instance for the RestTemplate make this calls -> Now we are using in Service (MovieInfo e UserRatingInfo)
	//Using Bean to create a single instance
	//@Autowired //Dependency injection, fetch bean annotation
	//RestTemplate will be deprecated, use the alternative WebClient
	//private RestTemplate restTemplate; //the name doesn't matter, the type matters
	
	//If you have multiple services doing the same service (Like Stock information Yahoo/IEX)
	//You can access port, IDs etc
	//private DiscoveryClient discoveryClient;
	
	
	//@Autowired
	//private WebClient.Builder webClientBuilder;//Dependency injection for reactive program
	
	@Autowired
	private MovieInfo movieInfo; //Creating a refactoring granular fallback, use bean and  create a proxy
	
	@Autowired
	private UserRatingInfo userRatingInfo; //Created for refactoring granular fallback, use bean and  create a proxy

	@GetMapping("/{userId}")
	//version 2.2.2 - I had some problems without mention the version
	//This method makes every call, so it will need a circuit breaker here.
	//This is a method which should not cause everything to go down 
	//Removed because we have fallback for service @HystrixCommand(fallbackMethod = "getFallbackCatalog") //instead break the circuit, call this method "getFallback"
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
		UserRating ratings = userRatingInfo.getUserRating(userId); //Creating granularity for Hystrix
		
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
			
			//Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class); Creating granularity for Hystrix, for fallback per service
			
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
			// --> 3 - Final step, put then all together - Put it in a method 
			//return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
			return movieInfo.getCatalogItem(rating);//Creating granularity for Hystrix, for fallback per service
		
		})
		.collect(Collectors.toList()); //Give a list for who have been calling
				
	}
	
	//Need to be simple and hard coded fallback. You don't want another call, that's why it's hard coded
	/*public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
		return Arrays.asList(new CatalogItem("No movie", "", 0)); //default response
	}*/
	
	
	/*Goes to service UserRatingInfo -> Injected as bean
	//Creating granularity for Hystrix, for fallback per service
	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
	private UserRating getUserRating(@PathVariable("userId") String userId) {
		return restTemplate.getForObject("http://movie-rating-service/ratingsdata/users/" + userId, UserRating.class); //see UserRating (API and copy the model to Core)
	}
	
	//Fallback method
	private UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
		//Hard code data for default response
		UserRating userRating = new UserRating();
		userRating.setUserId(userId);
		userRating.setUserRating(Arrays.asList(
				new Rating("0", 0)
				));
		return userRating;
	}*/
	
	/* Goes to MovieInfo -> Injected as bean
	//Creating granularity for Hystrix, for fallback per service
	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
	private CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}
	
	//Fallback method
	private CatalogItem getFallbackCatalogItem(Rating rating) {
		//Hard code data for default response
		return new CatalogItem("Movie name not found", "", rating.getRating());		
	}*/
}
