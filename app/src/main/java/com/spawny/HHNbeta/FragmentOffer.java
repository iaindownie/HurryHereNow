package com.spawny.HHNbeta;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spawny.HHNbeta.data.Offer;
import com.spawny.HHNbeta.data.RetailerOffers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by iaindownie on 29/11/2015.
 */
public class FragmentOffer extends Fragment {

    private SharedPreferences prefs;
    private String rawOfferJSON = "";
    private String category;
    private MapView mapView;
    private GoogleMap gMap;
    private LocationManager locManager;
    private Location l;
    private LatLng position;
    private Button searchButton;
    private Button listButton;
    private HashMap allCategories;
    private HashMap<Marker, RetailerOffers> mSimpleMarkersHashMap;
    private ArrayList<RetailerOffers> mSimpleMyMarkersArray = new ArrayList<RetailerOffers>();

    private Projection projection;

    LinearLayout ll;
    TextView data;
    ImageView tHolder;

    private ViewPager vp;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_offer, container, false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Bundle for if arriving from the Retailers Extra page
        Bundle bundle = getArguments();
        double tempLat = 0.0;
        double tempLon = 0.0;
        if (bundle != null) {
            tempLat = bundle.getDouble("LAT", 0.0);
            tempLon = bundle.getDouble("LON", 0.0);
        }
        mapView = (MapView) getActivity().findViewById(R.id.offerMapView);
        mapView.onCreate(savedInstanceState);
        gMap = mapView.getMap();
        projection = gMap.getProjection();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Map Settings
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(false);

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
        gMap.setMyLocationEnabled(true);

        /*gMap.addMarker(new MarkerOptions()
                .position(new LatLng(52.2068236, 0.1187916))
                .title("Hello world"));*/

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 12);
        gMap.moveCamera(update);


        mapView.onResume();


        ll = (LinearLayout) getActivity().findViewById(R.id.mapPopover);
        tHolder = (ImageView) getActivity().findViewById(R.id.triangleDown);


        /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        // Changes the height and width and margins to the specified *pixels*
        int boxHeight = (new Double(Constants.SCREENHEIGHT * 0.5).intValue());
        int boxWidth = (new Double(Constants.SCREENWIDTH * 0.75).intValue());
        int sideMargin = (new Double(Constants.SCREENWIDTH * 0.15).intValue());
        int bottomMargin = (new Double(Constants.SCREENHEIGHT * 0.48).intValue());
        int topMargin = (new Double(Constants.SCREENHEIGHT * 0.1).intValue());
        int centerPointH = boxHeight/2;
        int centerPointW = boxWidth/2;

        System.out.println("Width = " + boxWidth + "w x Height = " + boxHeight + "h");
        System.out.println("BoxWidth = " + boxWidth + "w x BoxHeight = " + boxHeight + "h");
        System.out.println("SideMargin = " + sideMargin + "px : TopMargin = " + topMargin + "px");
        System.out.println("CenterPoint = " + centerPointW + " : " + centerPointH);


        //params.height = boxHeight;
        //params.width = boxWidth;
        //params.leftMargin = sideMargin;
        //params.rightMargin = sideMargin;
        //params.topMargin = topMargin;
        //params.bottomMargin = bottomMargin;


        data = (TextView) getActivity().findViewById(R.id.txtNve);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myToast("Touched!", Toast.LENGTH_SHORT);
            }
        });*/


        ll.setVisibility(View.GONE);
        tHolder.setVisibility(View.GONE);

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ll.setVisibility(View.GONE);
                tHolder.setVisibility(View.GONE);
            }
        });

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

        /**
         * The Offers > Search button. It creates a new Activity not a fragment, and
         * closes the existing Main Activity and Fragments
         */
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

        /**
         * The Offers > Lists button, replacing the fragment from Map
         */
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

        vp = (ViewPager) this.getActivity().findViewById(R.id.pager);

        /**
         * Get the current vPager position from the ViewPager by
         * extending SimpleOnPageChangeListener class and updating the TextView
         */
        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int currentPage;

            @Override
            public void onPageSelected(int pos) {
                currentPage = pos;
                System.out.println("VPager page:" + pos);
            }

            public final int getCurrentPage() {
                return currentPage;
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
            setUpMap();
            plotSimpleMarkers(mSimpleMyMarkersArray);
        }
    }


    private void plotSimpleMarkers(ArrayList<RetailerOffers> retailers) {

        if (retailers.size() > 0) {
            for (RetailerOffers ro : retailers) {

                final int cat = ro.getCategory();

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(ro.getLatitude(), ro.getLongitude()));
                if (cat == 99) {
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.spotshare));
                } else {
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
                }


                Marker currentMarker = gMap.addMarker(markerOption);
                mSimpleMarkersHashMap.put(currentMarker, ro);

                gMap.setInfoWindowAdapter(new SimpleInfoWindowAdapter(getActivity()));


            }
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

    public void myToast(String str, int len) {
        Toast.makeText(getActivity(), str, len).show();
    }


    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the gMap.
        if (gMap == null) {

            if (gMap != null) {
                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        System.out.println("MyMarker has been clicked!");
                        marker.showInfoWindow();
                        //marker.hideInfoWindow();
                        Projection projection = gMap.getProjection();
                        LatLng markerLocation = marker.getPosition();
                        Point screenPosition = projection.toScreenLocation(markerLocation);
                        System.out.println("OnMarkerClick! Position" + screenPosition.toString());
                        return true;
                    }
                });

            } else
                Toast.makeText(getActivity(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }


    public class SimpleInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        Context context;

        public SimpleInfoWindowAdapter(Context c) {
            context = c;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }


        @Override
        public View getInfoContents(Marker marker) {
            ll.setVisibility(View.GONE);
            tHolder.setVisibility(View.GONE);

            //final Marker tempMarker = marker;
            RetailerOffers ro = mSimpleMarkersHashMap.get(marker);

            final int cat = ro.getCategory();
            final String url = ro.getSite();

            if (cat == 99) {
                View v;
                v = getActivity().getLayoutInflater().inflate(R.layout.spot_window_simple, null);
                TextView markerDescription = (TextView) v.findViewById(R.id.spotWindowSimpleDescription);
                TextView markerStoreName = (TextView) v.findViewById(R.id.spotWindowSimpleRetailer);
                markerDescription.setText(ro.getDescription());
                markerStoreName.setText(ro.getStoreName());
                return v;
            } else {
                vp.setAdapter(new ViewPagerAdapter(((Activity) context), ro));
                ll.setVisibility(View.VISIBLE);
                tHolder.setVisibility(View.VISIBLE);
                return null;

            }
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        LayoutInflater inflater;
        String[] country = new String[]{"China", "India", "United States", "Indonesia",
                "Brazil", "Pakistan", "Nigeria", "Bangladesh", "Russia", "Japan"};

        Offer[] offers;
        public ImageLoader imageLoader;
        Context context;
        RetailerOffers retailerOffers;

        ViewPagerAdapter(Context c, RetailerOffers ro){
            retailerOffers = ro;
            offers = ro.getOffers();
            context = c;
            imageLoader = new ImageLoader(context);
        }

        @Override
        public int getCount() {
            return offers.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }



        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Offer o = offers[position];
            System.out.println(o.getDescription());
            inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.viewpager_item, container,
                    false);

            ImageView offerImage = (ImageView) itemView.findViewById(R.id.offerImage);
            String imageUrl = Constants.BASE_URL + "/images/offers/" + o.getOfferId() + ".png";
            //imageLoader.DisplayImage(Constants.BASE_URL + r.getSmallImage(), holder.image1);
            imageLoader.DisplayImage(imageUrl, offerImage);
            TextView txtcountry = (TextView) itemView.findViewById(R.id.txtOfferDesc);
            txtcountry.setText(o.getDescription());
            TextView txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            txtStoreName.setText(retailerOffers.getName());
            ((ViewPager) container).addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((RelativeLayout) object);
        }
    }

    /**
     * The methods below are templates added to try to prevent the
     * fragments crashing on loading after sleep.
     */
    public static final String TAG = "FragmentOffer";

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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