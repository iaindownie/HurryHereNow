package com.HurryHereNow.HHN;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


/**
 * Created by iaindownie on 29/11/2015.
 */
public class MainActivity extends FragmentActivity {

    String starter = "MAP";
    ImageButton offersButton;
    ImageButton talkButton;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Getting status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // Showing status
        if (status == ConnectionResult.SUCCESS) {
            //myToast("Google Play Services are available", Toast.LENGTH_LONG);
            Log.i(TAG, "Google Play Services is installed on this device.");
        } else {
            //myToast("Google Play Services are not available", Toast.LENGTH_LONG);
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }/**/

        //Need to keep this in as it's used for the resizing large retailer image.
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Constants.setScreenWidth(width);
        Constants.setScreenHeight(height);
        //System.out.println(">>> MainActivity screensize from constants = " + Constants.SCREENWIDTH + "w x " + Constants.SCREENHEIGHT + "h");

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        starter = prefs.getString("ORIGINATOR", "MAP");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (starter.equals("MAP")) {
            ft.replace(R.id.theFragment, new FragmentOffer());
        } else {
            ft.replace(R.id.theFragment, new FragmentList());
        }
        ft.commit();

        offersButton = (ImageButton) findViewById(R.id.button1);
        offersButton.setImageDrawable(getResources().getDrawable(R.drawable.offersred100));
        offersButton.setEnabled(false);

    }

    public void selectFrag(View view) {
        Fragment fr;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        offersButton = (ImageButton) findViewById(R.id.button1);
        talkButton = (ImageButton) findViewById(R.id.button3);
        switch (view.getId()) {
            case R.id.button1:
                //System.out.println("Offer button used...");
                fr = new FragmentOffer();
                talkButton.setImageDrawable(getResources().getDrawable(R.drawable.chatgrey100));
                offersButton.setImageDrawable(getResources().getDrawable(R.drawable.offersred100));
                offersButton.setEnabled(false);
                talkButton.setEnabled(true);
                break;
            case R.id.button2:
                //System.out.println("Spot button used...");
                fr = new FragmentSpot();
                talkButton.setImageDrawable(getResources().getDrawable(R.drawable.chatgrey100));
                offersButton.setImageDrawable(getResources().getDrawable(R.drawable.offersgrey100));
                offersButton.setEnabled(true);
                talkButton.setEnabled(true);
                break;
            case R.id.button3:
                //System.out.println("Talk button used...");
                fr = new FragmentTalk();
                talkButton.setImageDrawable(getResources().getDrawable(R.drawable.chatred100));
                offersButton.setImageDrawable(getResources().getDrawable(R.drawable.offersgrey100));
                offersButton.setEnabled(true);
                talkButton.setEnabled(true);
                break;
            default:
                fr = new FragmentOffer();
                break;
        }

        ft.replace(R.id.theFragment, fr);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void updateOffersButton(boolean state) {
        offersButton.setImageDrawable(getResources().getDrawable(R.drawable.offersred100));
        offersButton.setEnabled(state);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        super.onStop();
        //Log.i(TAG, "Activity Life Cycle : onStop : Activity Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG, "Activity Life Cycle : onDestroy : Activity Destroyed");
    }


}