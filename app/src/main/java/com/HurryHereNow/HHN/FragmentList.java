package com.HurryHereNow.HHN;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.HurryHereNow.HHN.adapters.ListOfOffersAdapter;
import com.HurryHereNow.HHN.data.RetailerOffers;

import java.util.ArrayList;

/**
 * Created by iaindownie on 29/11/2015.
 * The main Offers > List fragment
 */
public class FragmentList extends Fragment {

    private SharedPreferences prefs;
    private String rawOfferJSON = "";
    private Location l;
    private LatLng position;
    private String category;
    private ArrayList<RetailerOffers> offerArray = new ArrayList<RetailerOffers>();
    private int listPosition = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ListOfOffersAdapter mListOfOffersAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            listPosition = bundle.getInt("LISTPOSITION", 0);
        }

        prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        //long lastTime = prefs.getLong("OFFERGRAB_TIME", System.currentTimeMillis());
        //long currentTime = System.currentTimeMillis();
        //long diffInTime = currentTime - lastTime;
        rawOfferJSON = prefs.getString("RAWOFFERJSON", "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ORIGINATOR", "MAP");
        editor.apply();
        category = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("CATEGORY", "0");
        if (category.equals("99")) {
            category = "0";
        }

        /*
        // If no data or data is 5 minutes old
        if (rawOfferJSON.length() == 0 || (diffInTime > 300000)) {
            new DownloadOffersTask().execute();
        } else {
            try {
                offerArray = JSONUtilities.expandPromotionsArrayList(JSONUtilities.convertJSONSpotAndSharePromotionsToArrayList(rawOfferJSON, category));
                offerCustomAdapter = new OfferCustomAdapterOLD(getActivity(), offerArray);
                //setListAdapter(offerCustomAdapter);
                lv.setAdapter(offerCustomAdapter);
                lv.setSelection(listPosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestprovider = locManager.getBestProvider(criteria, false);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            l = locManager.getLastKnownLocation(bestprovider);
        }

        if (l == null) {
            position = new LatLng(Constants.CAMBRIDGE_LAT, Constants.CAMBRIDGE_LON);
        } else {
            position = new LatLng(l.getLatitude(), l.getLongitude());
        }

        if (Constants.IS_DEBUG) {
            position = new LatLng(Constants.CAMBRIDGE_LAT, Constants.CAMBRIDGE_LON);
        }

        new DownloadOffersTask().execute();


        mSwipeRefreshLayout = (SwipeRefreshLayout) this.getActivity().findViewById(R.id.offer_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) this.getActivity().findViewById(R.id.offer_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);


        try {
            offerArray = JSONUtilities.expandPromotionsArrayList(JSONUtilities.convertJSONSpotAndSharePromotionsToArrayList(rawOfferJSON, category));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupAdapter(offerArray);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new DownloadOffersTask().execute();
                        //setupAdapter(allTalks);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        /**
         * The Offers > Search button. It creates a new Activity not a fragment, and
         * closes the existing Main Activity and Fragments
         */
        Button searchButton = (Button) this.getActivity().findViewById(R.id.imgBtnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ORIGINATOR", "LIST");
                editor.apply();
                Intent mapView = new Intent(getActivity(), Search.class);
                startActivity(mapView);
                getActivity().finish();
            }
        });

        /**
         * The Offers > Maps button, replacing the fragment from List
         */
        Button mapButton = (Button) this.getActivity().findViewById(R.id.offer_inner_imgBtnFake);
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentOffer nextFrag = new FragmentOffer();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.theFragment, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void setupAdapter(ArrayList aList) {
        mListOfOffersAdapter = new ListOfOffersAdapter(getActivity(), aList);
        mRecyclerView.setAdapter(mListOfOffersAdapter);
        mRecyclerView.scrollToPosition(listPosition);
    }


    private class DownloadOffersTask extends AsyncTask<String, Void, String> {

        // can use UI thread here
        protected void onPreExecute() {
        }

        // automatically done on worker thread (separate from UI thread)
        protected String doInBackground(final String... args) {
            try {
                String rootURL = Constants.API_BASE_URL + "/api/promotions?";
                String lat = "latitude=" + position.latitude;
                String lon = "longitude=" + position.longitude;
                String distance = "distance=" + Constants.PROMOTIONS_DISTANCE;
                if (Constants.IS_DEBUG) distance = "distance=120";
                String link = "&";
                String path = rootURL + lat + link + lon + link + distance;
                rawOfferJSON = JSONUtilities.downloadAllPromotionsFromURL(path);
                offerArray = JSONUtilities.expandPromotionsArrayList(JSONUtilities.convertJSONSpotAndSharePromotionsToArrayList(rawOfferJSON, category));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        // can use UI thread here
        protected void onPostExecute(final String result) {
            if (offerArray.size() == 0) {
                // Need a dialog box here with text saying "No offers in this category"
                // and possibly options to go to spot and share?
                zeroOffersDialog(Utils.getCategoryDescription(category));
            }

            setupAdapter(offerArray);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("RAWOFFERJSON", rawOfferJSON);
            editor.putLong("OFFERGRAB_TIME", System.currentTimeMillis());
            editor.apply();

        }
    }

    private void zeroOffersDialog(String cat) {
        final String aCat = cat;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.zero_dialog);
        Button dismiss = (Button) dialog.findViewById(R.id.zeroDismiss);
        TextView zeroMessage = (TextView) dialog.findViewById(R.id.txtZeroMessage);
        String aMessage = "Sorry, there are currently no \"" + aCat + "\" offers in your area. Why not share one via Spot & Share?";
        zeroMessage.setText(aMessage);
        dialog.show();

        dismiss.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * The methods below are templates added to try to prevent the
     * fragments crashing on loading after sleep.
     */
    public static final String TAG = "FragmentList";

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAdapter(offerArray);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tag", TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
