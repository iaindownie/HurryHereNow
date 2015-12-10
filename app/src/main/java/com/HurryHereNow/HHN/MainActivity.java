package com.HurryHereNow.HHN;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


/**
 * Created by iaindownie on 29/11/2015.
 */
public class MainActivity extends Activity {

    String currentTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.theFragment, new FragmentOffer());
        ft.commit();

        ImageButton but1 = (ImageButton) findViewById(R.id.button1);
        but1.setImageDrawable(getResources().getDrawable(R.drawable.offersred100));
    }

    public void selectFrag(View view) {
        Fragment fr;

        ImageButton but1 = (ImageButton) findViewById(R.id.button1);
        ImageButton but3 = (ImageButton) findViewById(R.id.button3);
        switch (view.getId()) {
            case R.id.button1:
                fr = new FragmentOffer();
                but3.setImageDrawable(getResources().getDrawable(R.drawable.chatgrey100));
                but1.setImageDrawable(getResources().getDrawable(R.drawable.offersred100));
                System.out.println("Offer button used...");
                break;
            case R.id.button2:
                fr = new FragmentSpot();
                but3.setImageDrawable(getResources().getDrawable(R.drawable.chatgrey100));
                but1.setImageDrawable(getResources().getDrawable(R.drawable.offersgrey100));
                System.out.println("Spot button used...");
                break;
            case R.id.button3:
                fr = new FragmentTalk();
                but3.setImageDrawable(getResources().getDrawable(R.drawable.chatred100));
                but1.setImageDrawable(getResources().getDrawable(R.drawable.offersgrey100));
                System.out.println("Talk button used...");
                break;
            default:
                fr = new FragmentOffer();
                break;
        }




        /*if (view == findViewById(R.id.button2)) {
            fr = new FragmentSpot();

        } else {
            fr = new FragmentOffer();
        }*/

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.theFragment, fr);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.i(TAG, "Activity Life Cycle : onStart : Activity Started");
    }

    // The expandableListView is set as focus to make sure keyboard doesn't appear onResume
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