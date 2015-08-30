package util;

/**
 * Created by Akshay on 8/27/15.
 */
public class Constants {

    //Base url to fetch movie feed sorted by rating, 30 items per page
    public static final String BASE_MOVIE_FEED_URL =
            "https://yts.to/api/v2/list_movies.json?sort_by=rating&page=%s&limit=30";

}
