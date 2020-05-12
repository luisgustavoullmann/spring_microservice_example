package com.movie.catalog.model;

//You need the APIs model, to store the infos
//Don't use JPA here
public class Rating {

	private String movieId;
	private int rating;
	
	public Rating() {
	}

	public Rating(String movieId, int rating) {
		this.movieId = movieId;
		this.rating = rating;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
