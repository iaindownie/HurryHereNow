package com.HurryHereNow.HHN;


import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.HurryHereNow.HHN.data.MySimpleMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentList extends ListFragment {

    SharedPreferences prefs;
    String rawOfferJSON = "";
    // Set default distance for promotions API
    private int DISTANCE = 50;

    private String category;


    private Button searchButton;
    private Button mapButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        long lastTime = prefs.getLong("OFFERGRAB_TIME", System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        long diffInTime = currentTime - lastTime;
        rawOfferJSON = prefs.getString("RAWOFFERJSON", "");
        //category = prefs.getString("CATEGORY", "0");
        category = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("CATEGORY", "0");
        System.out.println("Category: " + category);

        //new DownloadOffersTask().execute();
        if (rawOfferJSON.length() == 0 || (diffInTime > 600000)) {
            new DownloadOffersTask().execute();
        } else {
            try {
                new DownloadOffersTask().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        searchButton = (Button) this.getActivity().findViewById(R.id.imgBtnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mapView = new Intent(getActivity(), Search.class);
                startActivity(mapView);
                getActivity().finish();
            }
        });

        mapButton = (Button) this.getActivity().findViewById(R.id.imgBtnFake);
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentOffer nextFrag= new FragmentOffer();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.theFragment, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private class DownloadOffersTask extends AsyncTask<String, Void, String> {
        private final ProgressDialog asyncDialog = new ProgressDialog(
                getActivity());

        // can use UI thread here
        protected void onPreExecute() {
            this.asyncDialog.setTitle("Grabbing offers");
            this.asyncDialog.setMessage("Please wait...");
            this.asyncDialog.show();
        }

        // automatically done on worker thread (separate from UI thread)
        protected String doInBackground(final String... args) {
            try {
                String rootURL = "http://api.hurryherenow.com/api/promotions?";
                String lat = "latitude=52.415127";
                String lon = "longitude=0.7504132";
                String distance = "distance=" + DISTANCE;
                String link = "&";
                String path = rootURL + lat + link + lon + link + distance;
                rawOfferJSON = JSONUtilities.downloadAllPromotionsFromURL(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        // can use UI thread here
        protected void onPostExecute(final String result) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(
                    "RAWOFFERJSON", rawOfferJSON);
            editor.putLong("`OFFERGRAB_TIME", System.currentTimeMillis());
            editor.apply();
            if (this.asyncDialog.isShowing()) {
                this.asyncDialog.dismiss();
            }

        }
    }
}
