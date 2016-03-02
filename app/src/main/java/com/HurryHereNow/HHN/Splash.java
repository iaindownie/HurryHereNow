package com.HurryHereNow.HHN;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.ref.WeakReference;

/**
 * Created by iaindownie on 19/10/2015.
 * Copyright @iaindownie
 * Simple spash screen activity. Runs a temp Runnable thread before redirects
 * to the main activity, closing itself. Only appears when app is launched on
 * device, not if the app is still running in background.
 */

public class Splash extends Activity {

    private GoogleApiClient mClient;
    private Uri mUrl;
    private String mTitle;
    private String mDescription;
    //private String mSchemaType;

    // 1. Create a static nested class that extends Runnable to start the main Activity
    private static class StartMainActivityRunnable implements Runnable {
        // 2. Make sure we keep the source Activity as a WeakReference (more on that later)
        private WeakReference mActivity;

        private StartMainActivityRunnable(Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void run() {
            // 3. Check that the reference is valid and execute the code
            if (mActivity.get() != null) {
                Activity activity = (Activity) mActivity.get();
                Intent mainIntent = new Intent(activity, MainActivity.class);
                activity.startActivity(mainIntent);
                activity.finish();
            }
        }
    }

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1200;

    // 4. Declare the Handler as a member variable
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mUrl = Uri.parse("http://www.hurryherenow.com");
        mTitle = "Hurry Here Now";
        mDescription = "Local offers. That's what we do.";
        //mSchemaType = "http://schema.org/Article";

        // 5. Pass a new instance of StartMainActivityRunnable with reference to 'this'.
        mHandler.postDelayed(new StartMainActivityRunnable(this), SPLASH_DISPLAY_LENGTH);
    }

    public Action getAction() {
        Thing object = new Thing.Builder()
                .setName(mTitle)
                .setDescription(mDescription)
                .setUrl(mUrl)
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    // 6. Override onDestroy()
    @Override
    public void onDestroy() {
        //Log.i(TAG, "Activity Life Cycle : onDestroy : Activity Destroyed");
        // 7. Remove any delayed Runnable(s) and prevent them from executing.
        mHandler.removeCallbacksAndMessages(null);

        // 8. Eagerly clear mHandler allocated memory
        mHandler = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getAction());
        //Log.i(TAG, "Activity Life Cycle : onStart : Activity Started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i(TAG, "Activity Life Cycle : onResume : Activity Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.i(TAG, "Activity Life Cycle : onPause : Activity Paused");
    }

    @Override
    protected void onStop() {
        AppIndex.AppIndexApi.end(mClient, getAction());
        mClient.disconnect();
        super.onStop();
        //Log.i(TAG, "Activity Life Cycle : onStop : Activity Stopped");
    }


}



