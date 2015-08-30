package network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Akshay on 8/26/15.
 *
 * what: Volley Singleton to have one point to make non-image network calls
 * why: 1. Using a library for n/w call means less chance of errors in networking
 *      2. Using volley for image loading is cumbersome as code is not clean (as compared to Picasso)
 *       and L1 (memory) and L2(disk) cache are not present by default
 *
 */
public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    public static final String TAG = VolleySingleton.class.getSimpleName();


    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    //use tag while accepting requests
    // so that on activity destroy all pending requests with the tag can be cancelled at one go
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }


}
