package moviez.example.com.movieApp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import network.PicassoSingleton;


public class DetailedMovie extends Activity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);

        //changing the status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //API available only for lollipop and above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }

        String imageUrl = null;
        String title = null;
        String year = null;
        String rating = null;

        Bundle movieInfo = this.getIntent().getExtras();

        if(movieInfo != null) {

            imageUrl = movieInfo.getString("ImageUrl");
            title = movieInfo.getString("Title");
            rating = movieInfo.getString("Rating");
            year = movieInfo.getString("Year");

        }

        ImageView imageView = (ImageView)findViewById(R.id.detailed_image);
        TextView titleView = (TextView)findViewById(R.id.detailed_title);
        TextView yearView = (TextView)findViewById(R.id.detailed_year);
        TextView ratingView = (TextView)findViewById(R.id.detailed_rating);

        //Picasso library to display the image in detail
        //Why? As Picasso has L1 and L2 caching so avoids duplicate loading of the same resource
        //resource fetched from cache if hit
        PicassoSingleton.getPicassoInstance(this).load(imageUrl).into(imageView);
        titleView.setText(title);
        yearView.setText("Year: " + year);
        ratingView.setText("Rating: " + rating);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
