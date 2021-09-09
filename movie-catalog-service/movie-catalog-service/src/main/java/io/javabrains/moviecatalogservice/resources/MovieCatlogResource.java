package io.javabrains.moviecatalogservice.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.javabrains.moviecatalogservice.model.CatlogItem;
import io.javabrains.moviecatalogservice.model.Movie;
import io.javabrains.moviecatalogservice.model.Rating;
import io.javabrains.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catlog")
public class MovieCatlogResource {
	 
	@Autowired 
	 private RestTemplate restTemplate;
	 
	@Autowired
	private WebClient.Builder builder;
	
	@RequestMapping("/{userId}")
	public List<CatlogItem> getCatlog(@PathVariable("userId") String userId){
		
		
		List<Rating> ratings=restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/"+userId, UserRating.class).getRatings();
	
		return ratings.stream().map(rating -> {
			Movie mov=restTemplate.getForObject("http://movie-info-service:8083/movies/"+rating.getMovieId(),Movie.class);
			
			/*Movie mov=builder.build()
			.get().uri("http://localhost:8083/movies/"+rating.getMovieId())
			.retrieve()
			.bodyToMono(Movie.class).block();
			*/
			return new CatlogItem(mov.getName(),"test",rating.getRating());
		}).collect(Collectors.toList());
		
	}
}
