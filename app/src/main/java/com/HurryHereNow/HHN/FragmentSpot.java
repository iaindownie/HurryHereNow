package com.HurryHereNow.HHN;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
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
    private LocationManager locManager;
    private Location l;
    private LatLng position;
    String uploadStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_spot, container, false);
        mapView = (MapView) v.findViewById(R.id.spotMapView);
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

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestprovider = locManager.getBestProvider(criteria, false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            l = locManager.getLastKnownLocation(bestprovider);
        }

        if (l == null) {
            position = new LatLng(52.2068236, 0.1187916);
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


        TextView but1 = (TextView) v.findViewById(R.id.continueTitle);
        but1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectSpotDetails();
            }
        });

        uploadStatus = "Thanks for sharing your offer with the Hurry Here Now community! It will be activated soon...";

        return v;
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
        StringBuilder tandc = new StringBuilder();
        tandc.append("I agree to the <a href=\"http://www.google.co.uk\">Terms & Conditions</a>:");
        termsConditions.setText(Html.fromHtml(tandc.toString()));
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
                    myToast("Please enter both fields and agree to T&C", Toast.LENGTH_LONG);
                } else {
                    String[] s = new String[]{storeName.getText().toString(), desc.getText().toString(), "" + ll.latitude, "" + ll.longitude};
                    new UploadingSpotAndShare().execute(s);
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
            }
        });
    }


    public void myToast(String str, int len) {
        Toast.makeText(getActivity(), str, len).show();
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


}
