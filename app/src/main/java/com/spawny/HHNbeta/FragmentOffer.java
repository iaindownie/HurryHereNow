package com.spawny.HHNbeta;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
    private Location l;
    private LatLng position;
    //private HashMap allCategories;
    private HashMap<Marker, RetailerOffers> mSimpleMarkersHashMap;
    private ArrayList<RetailerOffers> mSimpleMyMarkersArray = new ArrayList<RetailerOffers>();
    private int numOffers = 1;

    private Projection projection;

    private LinearLayout ll, opaqueLayer;
    private ImageView tHolder;
    private ImageView dot1, dot2, dot3, pveImage, nveImage;
    private TextView clock, pve, nve;

    private ViewPager vp;

    private RetailerOffers ros;
    private Offer[] outerOffers;
    private int zoomLevel = 12;


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
            zoomLevel = 14;
        }
        mapView = (MapView) getActivity().findViewById(R.id.offer_inner_MapView);
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

        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = locManager.getBestProvider(criteria, false);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            l = locManager.getLastKnownLocation(bestProvider);
        }

        if (tempLat != 0.0) {
            position = new LatLng(tempLat, tempLon);
        } else if (l == null) {
            position = new LatLng(Constants.CAMBRIDGE_LAT, Constants.CAMBRIDGE_LON);
        } else {
            position = new LatLng(l.getLatitude(), l.getLongitude());
        }

        if (Constants.IS_DEBUG) {
            if (tempLat != 0.0) {
                position = new LatLng(tempLat, tempLon);
            } else {
                position = new LatLng(Constants.CAMBRIDGE_LAT, Constants.CAMBRIDGE_LON);
            }
        }

        //Enable GPS
        gMap.setMyLocationEnabled(true);

        // This moves the camera to the position and zoom level
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, zoomLevel);
        gMap.moveCamera(update);


        ll = (LinearLayout) getActivity().findViewById(R.id.offer_inner_mapPopover);
        opaqueLayer = (LinearLayout) getActivity().findViewById(R.id.offer_inner_opaqueLayer);
        tHolder = (ImageView) getActivity().findViewById(R.id.offer_inner_triangleDown);

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
        params.width = boxWidth;
        //params.leftMargin = sideMargin;
        //params.rightMargin = sideMargin;
        //params.topMargin = topMargin;
        //params.bottomMargin = bottomMargin;*/

        ll.setVisibility(View.GONE);
        opaqueLayer.setVisibility(View.GONE);
        tHolder.setVisibility(View.GONE);

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                opaqueLayer.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                tHolder.setVisibility(View.GONE);
            }
        });

        opaqueLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    opaqueLayer.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                    tHolder.setVisibility(View.GONE);
                    // Do what you want
                    return true;
                }
                return false;
            }
        });

        /*gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                LatLng position2 = marker.getPosition();
                double newLat = position2.latitude + 0.005;
                double newLon = position2.longitude;
                CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(newLat, newLon));
                gMap.moveCamera(update);
                marker.showInfoWindow();

                return true;
            }
        });*/

        // Initialize the HashMap for Markers and MyMarker object
        mSimpleMarkersHashMap = new HashMap<Marker, RetailerOffers>();

        prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        //long lastTime = prefs.getLong("OFFERGRAB_TIME", System.currentTimeMillis());
        //long currentTime = System.currentTimeMillis();
        //long diffInTime = currentTime - lastTime;
        rawOfferJSON = prefs.getString("RAWOFFERJSON", "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ORIGINATOR", "MAP");
        editor.apply();
        //category = prefs.getString("CATEGORY", "0");
        category = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("CATEGORY", "0");

        // If no data or data is 5 minutes old
        /*if (rawOfferJSON.length() == 0 || (diffInTime > 300000)) {
            new DownloadOffersTask().execute();
        } else {
            try {
                mSimpleMyMarkersArray = JSONUtilities.convertJSONSpotAndSharePromotionsToArrayList(rawOfferJSON, category);
                setUpMap();
                plotSimpleMarkers(mSimpleMyMarkersArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        new DownloadOffersTask().execute();

        /**
         * The Offers > Search button. It creates a new Activity not a fragment, and
         * closes the existing Main Activity and Fragments
         */
        Button searchButton = (Button) this.getActivity().findViewById(R.id.offer_inner_imgBtnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ORIGINATOR", "MAP");
                editor.apply();
                Intent searchView = new Intent(getActivity(), Search.class);
                startActivity(searchView);
                getActivity().finish();
            }
        });

        /**
         * The Offers > Lists button, replacing the fragment from Map
         */
        Button listButton = (Button) this.getActivity().findViewById(R.id.offer_inner_imgBtnListView);
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

        dot1 = (ImageView) this.getActivity().findViewById(R.id.offer_inner_dot11);
        dot2 = (ImageView) this.getActivity().findViewById(R.id.offer_inner_dot22);
        dot3 = (ImageView) this.getActivity().findViewById(R.id.offer_inner_dot33);

        vp = (ViewPager) this.getActivity().findViewById(R.id.offer_inner_pager);

        clock = (TextView) this.getActivity().findViewById(R.id.offer_inner_txtOfferEnds);
        pveImage = (ImageView) this.getActivity().findViewById(R.id.offer_inner_imgThumbsUp);
        nveImage = (ImageView) this.getActivity().findViewById(R.id.offer_inner_imgThumbsDown);
        pve = (TextView) this.getActivity().findViewById(R.id.offer_inner_txtPve);
        nve = (TextView) this.getActivity().findViewById(R.id.offer_inner_txtNve);

        /**
         * Get the current vPager position from the ViewPager by
         * extending SimpleOnPageChangeListener class and updating the TextView
         */
        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int currentPage;

            @Override
            public void onPageSelected(int pos) {
                currentPage = pos;
                clock.setText(Utils.getDaysRemaining(outerOffers[currentPage].getEndDate()) + "d");
                pve.setText("" + outerOffers[currentPage].getPve());
                nve.setText("" + outerOffers[currentPage].getNve());
                pve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + outerOffers[currentPage].getOfferId(), "1");
                    }
                });
                nve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + outerOffers[currentPage].getOfferId(), "2");
                    }
                });
                pveImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + outerOffers[currentPage].getOfferId(), "1");
                    }
                });
                nveImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + outerOffers[currentPage].getOfferId(), "2");
                    }
                });

                if (currentPage == 0) {
                    dot1.setImageResource(R.drawable.doton);
                    dot2.setImageResource(R.drawable.dotoff);
                    dot3.setImageResource(R.drawable.dotoff);

                } else if (currentPage == 1) {
                    dot1.setImageResource(R.drawable.dotoff);
                    dot2.setImageResource(R.drawable.doton);
                    dot3.setImageResource(R.drawable.dotoff);

                } else {
                    dot1.setImageResource(R.drawable.dotoff);
                    dot2.setImageResource(R.drawable.dotoff);
                    dot3.setImageResource(R.drawable.doton);

                }
            }

            public final int getCurrentPage() {
                return currentPage;
            }
        });


        //mapView.onResume();
    }


    private void collectRateDetails(String offerId, String mType) {
        final String offer_id = offerId;
        final Dialog dialog = new Dialog(getActivity());
        final String type = mType;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.rate_dialog);
        Button submit = (Button) dialog.findViewById(R.id.rateSubmit);
        Button dismiss = (Button) dialog.findViewById(R.id.rateDismiss);
        dialog.show();
        final EditText txtRateOffer = (EditText) dialog.findViewById(R.id.txtRateOffer);

        submit.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                if (txtRateOffer.getText().toString().length() == 0) {
                    myToast("Please enter a comment before you submit", Toast.LENGTH_LONG);
                } else {
                    String[] s = new String[]{txtRateOffer.getText().toString(), offer_id, type};
                    if (Constants.IS_DEBUG) {
                        myToast("DEBUG UploadingRating(): Worked, but upload disabled by ISD", Toast.LENGTH_LONG);
                    } else {
                        new UploadingRating().execute(s);
                    }
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mapView.getWindowToken(), 0);
                    rateDetailsResult();
                    dialog.dismiss();
                }
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void rateDetailsResult() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.rate_dialog_result);
        Button dismiss = (Button) dialog.findViewById(R.id.spotDismissResult);
        dialog.show();
        dismiss.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class UploadingRating extends AsyncTask<String, Void, Void> {

        // can use UI thread here
        protected void onPreExecute() {

        }

        // automatically done on worker thread (separate from UI thread)
        @Override
        protected Void doInBackground(String... params) {

            try {
                String success = JSONUtilities.uploadRating(params[0], params[1], params[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final String result) {
        }
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
                String rootURL = Constants.API_BASE_URL + "/api/promotions?";
                String lat = "latitude=" + position.latitude;
                String lon = "longitude=" + position.longitude;
                String distance = "distance=" + Constants.PROMOTIONS_DISTANCE;
                if (Constants.IS_DEBUG) distance = "distance=120";
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
            if (mSimpleMyMarkersArray.size() == 0) {
                myToast("No offers in your area", Toast.LENGTH_LONG);
            }
            if (this.asyncDialog.isShowing()) {
                this.asyncDialog.dismiss();
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("RAWOFFERJSON", rawOfferJSON);
            editor.putLong("OFFERGRAB_TIME", System.currentTimeMillis());
            editor.apply();
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
        new DownloadOffersTask().execute();
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
                        marker.showInfoWindow();

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
            opaqueLayer.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            tHolder.setVisibility(View.GONE);
            outerOffers = null;

            //final Marker tempMarker = marker;
            final RetailerOffers ro = mSimpleMarkersHashMap.get(marker);
            final Offer[] offers = ro.getOffers();

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
                numOffers = offers.length;

                vp.setAdapter(new ViewPagerAdapter(((Activity) context), ro));

                ros = ro;
                outerOffers = offers;

                opaqueLayer.setVisibility(View.VISIBLE);
                ll.setVisibility(View.VISIBLE);
                clock.setText(Utils.getDaysRemaining(offers[0].getEndDate()) + "d");
                pve.setText("" + offers[0].getPve());
                nve.setText("" + offers[0].getNve());
                pve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + offers[0].getOfferId(), "1");
                    }
                });
                nve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + offers[0].getOfferId(), "2");
                    }
                });
                pveImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + offers[0].getOfferId(), "1");
                    }
                });
                nveImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectRateDetails("" + offers[0].getOfferId(), "2");
                    }
                });
                if (numOffers == 1) {
                    dot1.setVisibility(View.VISIBLE);
                    dot1.setImageResource(R.drawable.doton);
                    dot2.setVisibility(View.GONE);
                    dot3.setVisibility(View.GONE);
                } else if (numOffers == 2) {
                    dot1.setVisibility(View.VISIBLE);
                    dot1.setImageResource(R.drawable.doton);
                    dot2.setVisibility(View.VISIBLE);
                    dot3.setVisibility(View.GONE);
                } else {
                    dot1.setVisibility(View.VISIBLE);
                    dot1.setImageResource(R.drawable.doton);
                    dot2.setVisibility(View.VISIBLE);
                    dot3.setVisibility(View.VISIBLE);
                }


                tHolder.setVisibility(View.VISIBLE);
                return null;

            }
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        LayoutInflater inflater;

        Offer[] offers;
        public ImageLoader imageLoader;
        Context context;
        RetailerOffers retailerOffers;

        ViewPagerAdapter(Context c, RetailerOffers ro) {
            retailerOffers = ro;
            offers = ro.getOffers();
            context = c;
            imageLoader = new ImageLoader(context);
        }

        @Override
        public int getCount() {
            numOffers = offers.length;
            return offers.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Offer o = offers[position];
            inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_viewpager, container,
                    false);

            ImageView offerImage = (ImageView) itemView.findViewById(R.id.offerImage);
            String imageUrl = Constants.WWW_BASE_URL + "/images/offers/" + o.getOfferId() + ".png";
            //imageLoader.DisplayImage(Constants.BASE_URL + r.getSmallImage(), holder.image1);
            imageLoader.DisplayImage(imageUrl, offerImage);
            TextView txtOfferDesc = (TextView) itemView.findViewById(R.id.txtOfferDesc);
            txtOfferDesc.setText(o.getDescription());
            TextView txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            txtStoreName.setText(retailerOffers.getName());
            ((ViewPager) container).addView(itemView);

            txtOfferDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ORIGIN", "map");
                    bundle.putSerializable("RETAILEROFFERS", retailerOffers);
                    FragmentRetailerDetail nextFrag = new FragmentRetailerDetail();
                    nextFrag.setArguments(bundle);
                    FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.theFragment, nextFrag)
                            .addToBackStack(null)
                            .commit();
                }
            });
            txtStoreName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ORIGIN", "map");
                    bundle.putSerializable("RETAILEROFFERS", retailerOffers);
                    FragmentRetailerDetail nextFrag = new FragmentRetailerDetail();
                    nextFrag.setArguments(bundle);
                    FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.theFragment, nextFrag)
                            .addToBackStack(null)
                            .commit();
                }
            });
            offerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ORIGIN", "map");
                    bundle.putSerializable("RETAILEROFFERS", retailerOffers);
                    FragmentRetailerDetail nextFrag = new FragmentRetailerDetail();
                    nextFrag.setArguments(bundle);
                    FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.theFragment, nextFrag)
                            .addToBackStack(null)
                            .commit();
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove item_viewpager.xml from ViewPager
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