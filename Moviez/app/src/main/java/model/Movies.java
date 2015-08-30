package model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 8/26/15.
 *
 * Model class to store the movie feed
 */
public class Movies {

    private List<Movie> movies = new ArrayList<Movie>();


    public List<Movie> getMovies() {
        return movies;
    }


    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public class Movie {

        private Integer id;

        @SerializedName("medium_cover_image")
        private String imageUrl;

        @SerializedName("title")
        private String movieName;

        @SerializedName("year")
        private int releaseYear;

        private double rating;

        private double runtime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUrl() {
            return imageUrl;
        }

        public void setUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return movieName;
        }

        public void setTitle(String Title) {
            this.movieName = movieName;
        }

        public int getYear() {
            return releaseYear;
        }

        public void setYear(int year) {
            this.releaseYear = year;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public double getRuntime() {
            return runtime;
        }

        public void setRuntime(double runtime) {
            this.runtime = runtime;
        }

    }



    public Movies() {

    }


}
