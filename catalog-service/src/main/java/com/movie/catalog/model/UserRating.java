package com.movie.catalog.model;

import java.util.List;

//List of ratings
public class UserRating {
	//With this class, you have a list
	//that have been calling in MovieCatalogResources
	//and will return an object in the 
	//List<Rating> object
	private List<Rating> userRating;
	
	public UserRating() {
	}
	public UserRating(List<Rating> userRating) {
		this.userRating = userRating;
	}

	public List<Rating> getUserRating() {
		return userRating;
	}

	public void setUserRating(List<Rating> userRating) {
		this.userRating = userRating;
	}
		
}
