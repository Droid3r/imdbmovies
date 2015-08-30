package moviez.example.com.movieApp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomMovieListAdapter;
import model.Movies;
import network.VolleySingleton;
import util.Constants;


public class MainActivity extends Activity {

    //movieList bound to the Adapter
    private List<Movies.Movie> movieList = new ArrayList<Movies.Movie>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //List of Movies (Model) to be mapped by the Gson mapper
    private List<Movies.Movie> movies = new ArrayList<Movies.Movie>();

    //variable to calculate end of scrolling on items of recycler view
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    //handle to the progressbar
    private ProgressBar progBar;

    //variable to call more feed on user scroll action
    private int feedPage = 0;

    private final String VOLLEY_TAG = "tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reference to the progressbar
        progBar = (ProgressBar)findViewById(R.id.progressBar);

        //changing the StatusBar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //API available only for lollipop and above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }


        //check Internet connection
        if(!util.Util.isConnectedToInternet(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please connect to Internet and Restart the App", Toast.LENGTH_LONG).show();

        }

        //fetch the feed for first page (initial 30 items)
        feedPage++;

        //fetch initial movie feed
        getMovieFeed(feedPage);

        mRecyclerView = (RecyclerView)findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify a custom adapter
        mAdapter = new CustomMovieListAdapter(this, movieList);
        mRecyclerView.setAdapter(mAdapter);

        //adding a scroll listener to fetch further feed on user scroll
        addRecyclerViewScrollListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //cancel any pending requests to avoid using network resources after activity is destroyed
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*
    * Get the JSON feed for next 30 movies
    *
    * @param pageNumber page number of the paginated JSON feed
    *
    * */
    public void getMovieFeed(int pageNumber) {

        //start Progress bar before network call start
        progBar.setVisibility(View.VISIBLE);

        //format the URL string to add page to the URL
        String url = String.format(Constants.BASE_MOVIE_FEED_URL,pageNumber);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progBar.setVisibility(View.INVISIBLE);
                        movies.clear();
                        movies =  processVolleyResponse(response).getMovies();
                        if(movies == null) {
                           return;
                        }

                        //add fetched feed to the movieList bound to the adapter
                        for(int i = 0; i < movies.size(); i++) {
                            movieList.add(movies.get(i));
                        }
                        //notify the adapter that the data set has changed
                        mAdapter.notifyDataSetChanged();


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progBar.setVisibility(View.INVISIBLE);
                        Log.i("Debug: MainActivity" , error.toString());

                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest, VOLLEY_TAG);


    }

    /*
    * Listener for user scroll on recycler view
    * helps to fetch the next set of movie feed on user scroll action
    *
    * */
    public void addRecyclerViewScrollListener() {

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    //nearing the end of the total items
                    //need to fetch the next page

                    //increment the page count
                    feedPage++;

                    //fetch the next page
                    getMovieFeed(feedPage);

                    loading = true;
                }
            }
        });
    }
    /*
    * map the json response from the URL to the model class
    * */
    public Movies processVolleyResponse(JSONObject response) {
        Movies movies = null;
        Log.i("Processing", " Volley Response");
        Gson gson = new Gson();

        JSONObject jsonDataObj;
        if (response == null) {
            Log.i("Debug", "Volley Respone is null");
            return null;
        }
        try {
            if(response.has("data")) {
                jsonDataObj = response.getJSONObject("data");
                if (jsonDataObj.has("movies")) {

                    movies = gson.fromJson(jsonDataObj.toString(), Movies.class);

                }
            }

        } catch (Exception e) {
            Log.i("Debug", e.getMessage());
        } finally {
            return movies;
        }

    }



}
