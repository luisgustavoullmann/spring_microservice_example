package com.movie.catalog.resource;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.model.CatalogItem;

@RestController //API
@RequestMapping("/catalog") //URL
public class MovieCatalogResource {

	@GetMapping("/{userId}") //Passando userId como variável
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
			
		return Collections.singletonList(
			//Hard Code for a dummie API
			new CatalogItem("Spider-Man", "About a guy", 4)
		);
	}
}
