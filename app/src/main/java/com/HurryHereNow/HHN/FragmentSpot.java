package com.HurryHereNow.HHN;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by iaindownie on 29/11/2015.
 */
public class FragmentSpot extends Fragment {

    private MapView mapView;
    private GoogleMap map;
    private Location l;
    String uploadStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_spot, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapView = (MapView) getActivity().findViewById(R.id.spotMapView);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Map Settings
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);

        MapsInitializer.initialize(getActivity());

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestprovider = locManager.getBestProvider(criteria, false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            l = locManager.getLastKnownLocation(bestprovider);
        }

        LatLng position;
        if (l == null) {
            position = new LatLng(Constants.CAMBRIDGE_LAT, Constants.CAMBRIDGE_LON);
        } else {
            position = new LatLng(l.getLatitude(), l.getLongitude());
        }

        //Enable GPS
        map.setMyLocationEnabled(true);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 13);
        map.moveCamera(update);
        mapView.onResume();
        //Set the map to current location
        /*map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location location) {
                position = new LatLng(location.getLatitude(), location.getLongitude());

                //Zoom parameter is set to 14
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 12);
                //Use map.animateCamera(update) if you want moving effect
                map.moveCamera(update);
                mapView.onResume();
            }
        });*/


        TextView but1 = (TextView) getActivity().findViewById(R.id.continueTitle);
        but1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectSpotDetails();
            }
        });

        uploadStatus = "Thanks for sharing your offer with the Hurry Here Now community! It will be activated soon...";


    }

    private void collectSpotDetails() {
        final Dialog dialog = new Dialog(getActivity());
        final LatLng ll = map.getCameraPosition().target;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.spot_offer_dialog);
        Button submit = (Button) dialog.findViewById(R.id.spotSubmit);
        Button dismiss = (Button) dialog.findViewById(R.id.spotDismiss);
        TextView latLon = (TextView) dialog.findViewById(R.id.txtOfferLatLon);
        latLon.setText("Lat: " + String.format("%.5f", ll.latitude) + "\nLon: " + String.format("%.5f", ll.longitude));
        dialog.show();
        final EditText storeName = (EditText) dialog.findViewById(R.id.editStore);
        final EditText desc = (EditText) dialog.findViewById(R.id.editOffer);
        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);
        final boolean[] checked = {false};

        TextView termsConditions = (TextView) dialog.findViewById(R.id.txtTandC);
        String tandc = "I agree to the <a href=\"http://www.hurryherenow.com/legal-user-terms.pdf\">Terms & Conditions</a>:";
        termsConditions.setText(Html.fromHtml(tandc));
        termsConditions.setMovementMethod(LinkMovementMethod.getInstance());

        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checked[0] = true;
                } else {
                    checked[0] = false;
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                if (storeName.getText().toString().length() == 0 || desc.getText().toString().length() == 0 || checked[0] == false) {
                    Utils.myToast(getActivity(), "Please enter both fields and agree to T&C", Toast.LENGTH_LONG);
                } else {
                    String[] s = new String[]{storeName.getText().toString(), desc.getText().toString(), "" + ll.latitude, "" + ll.longitude};
                    if (Constants.IS_DEBUG) {
                        Utils.myToast(getActivity(), "DEBUG UploadingSpotAndShare(): Worked, but upload disabled by ISD", Toast.LENGTH_LONG);
                    } else {
                        new UploadingSpotAndShare().execute(s);
                    }
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mapView.getWindowToken(), 0);
                    spotDetailsResult();
                    // Also code to close keyboard if possible
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


    private void spotDetailsResult() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.spot_offer_dialog_result);
        Button dismiss = (Button) dialog.findViewById(R.id.spotDismissResult);
        TextView result = (TextView) dialog.findViewById(R.id.uploadResult);
        result.setText(uploadStatus);
        dialog.show();
        dismiss.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((MainActivity) getActivity()).updateOffersButton(true);
                FragmentOffer nextFrag = new FragmentOffer();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.theFragment, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
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
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private class UploadingSpotAndShare extends AsyncTask<String, Void, Void> {


        // can use UI thread here
        protected void onPreExecute() {

        }

        // automatically done on worker thread (separate from UI thread)
        @Override
        protected Void doInBackground(String... params) {

            try {
                uploadStatus = JSONUtilities.uploadSpotAndShare(params[0], params[1], params[2], params[3]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final String result) {
        }
    }

    /**
     * The methods below are templates added to try to prevent the
     * fragments crashing on loading after sleep.
     */
    public static final String TAG = "FragmentSpot";

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
