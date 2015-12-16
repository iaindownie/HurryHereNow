package com.HurryHereNow.HHN;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.HurryHereNow.HHN.data.RetailerOffers;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by iaindownie on 29/11/2015.
 */
public class FragmentOffer extends Fragment {

    SharedPreferences prefs;
    String rawOfferJSON = "";

    private String category;

    private MapView mapView;
    private GoogleMap map;
    private LocationManager locManager;
    private Location l;
    private LatLng position;

    private Button searchButton;
    private Button listButton;


    private HashMap allCategories;
    //ArrayList userSubmittedOffers;
    //ArrayList food;

    private HashMap<Marker, RetailerOffers> mSimpleMarkersHashMap;
    private ArrayList<RetailerOffers> mSimpleMyMarkersArray = new ArrayList<RetailerOffers>();


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        double tempLat = 0.0;
        double tempLon = 0.0;
        if(bundle!=null){
            tempLat = bundle.getDouble("LAT", 0.0);
            tempLon = bundle.getDouble("LON", 0.0);
        }

        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_offer, container, false);
        mapView = (MapView) v.findViewById(R.id.offerMapView);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Map Settings
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        MapsInitializer.initialize(getActivity());

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestprovider = locManager.getBestProvider(criteria, false);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            l = locManager.getLastKnownLocation(bestprovider);
        }

        if (tempLat != 0.0) {
            position = new LatLng(tempLat, tempLon);
        } else if (l == null) {
            position = new LatLng(52.2068236, 0.1187916);
        } else {
            position = new LatLng(l.getLatitude(), l.getLongitude());
        }

        //Enable GPS
        map.setMyLocationEnabled(true);

        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(52.2068236, 0.1187916))
                .title("Hello world"));*/

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 12);
        map.moveCamera(update);
        mapView.onResume();


        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize the HashMap for Markers and MyMarker object
        mSimpleMarkersHashMap = new HashMap<Marker, RetailerOffers>();


        prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        long lastTime = prefs.getLong("OFFERGRAB_TIME", System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        long diffInTime = currentTime - lastTime;
        rawOfferJSON = prefs.getString("RAWOFFERJSON", "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ORIGINATOR", "MAP");
        editor.apply();
        //category = prefs.getString("CATEGORY", "0");
        category = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("CATEGORY", "0");
        System.out.println("MAP Category: " + category);

        //new DownloadOffersTask().execute();
        if (rawOfferJSON.length() == 0 || (diffInTime > 600000)) {
            new DownloadOffersTask().execute();
        } else {
            try {
                mSimpleMyMarkersArray = JSONUtilities.convertJSONSpotAndSharePromotionsToArrayList(rawOfferJSON, category);
                setUpMap();
                plotSimpleMarkers(mSimpleMyMarkersArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        searchButton = (Button) this.getActivity().findViewById(R.id.imgBtnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ORIGINATOR", "MAP");
                editor.apply();
                Intent mapView = new Intent(getActivity(), Search.class);
                startActivity(mapView);
                getActivity().finish();
            }
        });

        listButton = (Button) this.getActivity().findViewById(R.id.imgBtnListView);
        listButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).updateOffersButton(true);
                FragmentList nextFrag = new FragmentList();
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
                String distance = "distance=" + Constants.PROMOTIONS_DISTANCE;
                String link = "&";
                String path = rootURL + lat + link + lon + link + distance;
                rawOfferJSON = JSONUtilities.downloadAllPromotionsFromURL(path);
                mSimpleMyMarkersArray = JSONUtilities.convertJSONSpotAndSharePromotionsToArrayList(rawOfferJSON, category);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        // can use UI thread here
        protected void onPostExecute(final String result) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("RAWOFFERJSON", rawOfferJSON);
            editor.putLong("OFFERGRAB_TIME", System.currentTimeMillis());
            editor.apply();
            if (this.asyncDialog.isShowing()) {
                this.asyncDialog.dismiss();
            }
            //showMarkers();
            setUpMap();
            plotSimpleMarkers(mSimpleMyMarkersArray);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private void plotSimpleMarkers(ArrayList<RetailerOffers> markers) {

        if (markers.size() > 0) {
            for (RetailerOffers myMarker : markers) {

                final int cat = myMarker.getCategory();

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getLatitude(), myMarker.getLongitude()));
                if (cat == 99) {
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.spotshare));
                } else {
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
                }

                Marker currentMarker = map.addMarker(markerOption);
                mSimpleMarkersHashMap.put(currentMarker, myMarker);

                map.setInfoWindowAdapter(new SimpleInfoWindowAdapter());

            }
        }
    }


    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {

            if (map != null) {
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });

            } else
                Toast.makeText(getActivity(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }


    public class SimpleInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public SimpleInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v;
            RetailerOffers myMarker = mSimpleMarkersHashMap.get(marker);

            final int cat = myMarker.getCategory();
            final String url = myMarker.getSite();

            if (cat == 99) {
                v = getActivity().getLayoutInflater().inflate(R.layout.spot_window_simple, null);
                TextView markerDescription = (TextView) v.findViewById(R.id.spotWindowSimpleDescription);
                TextView markerStoreName = (TextView) v.findViewById(R.id.spotWindowSimpleRetailer);
                markerDescription.setText(myMarker.getDescription());
                markerStoreName.setText(myMarker.getStoreName());
            } else {
                /*v = getActivity().getLayoutInflater().inflate(R.layout.spot_window_complex, null);

                TextView txtPve = (TextView) v.findViewById(R.id.txtPve);
                TextView txtNve = (TextView) v.findViewById(R.id.txtNve);
                txtPve.setText("0");
                txtNve.setText("0");*/

                /*ViewPager vPager = null;
                vPager = (ViewPager) v.findViewById(R.id.pager);
                vPager.setAdapter(new ViewPagerAdapter());*/
                v = getActivity().getLayoutInflater().inflate(R.layout.marker_filler, null);
                Toast.makeText(getActivity(), "Need a properly positioned overlay", Toast.LENGTH_SHORT).show();

            }

            /*map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if (cat == 99) {
                        System.out.println("In Spot & Share: " + url);
                        // Need to redirect to the Retailers Activity or Fragment here...
                    }
                }
            });*/


            return v;
        }
    }

    class ViewPagerAdapter extends PagerAdapter {
        LayoutInflater inflater;
        String[] country = new String[]{"China", "India", "United States", "Indonesia",
                "Brazil", "Pakistan", "Nigeria", "Bangladesh", "Russia", "Japan"};

        @Override
        public int getCount() {
            return country.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.viewpager_item, container,
                    false);

            TextView txtcountry = (TextView) itemView.findViewById(R.id.country);
            txtcountry.setText(country[position]);
            ((ViewPager) container).addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((RelativeLayout) object);
        }
    }

}