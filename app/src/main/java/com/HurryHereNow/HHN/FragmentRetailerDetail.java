package com.HurryHereNow.HHN;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.HurryHereNow.HHN.adapters.OfferListCustomAdapter;
import com.HurryHereNow.HHN.adapters.RetailerExtraAdapter;
import com.HurryHereNow.HHN.data.Offer;
import com.HurryHereNow.HHN.data.Retailer;
import com.HurryHereNow.HHN.data.RetailerOffers;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentRetailerDetail extends ListFragment {

    SharedPreferences prefs;
    RetailerOffers ro;
    String origin = "map";
    ArrayList offers;

    ImageView imageView;
    OfferListCustomAdapter offerListCustomAdapter;
    private ArrayAdapter<String> listAdapter;
    RetailerExtraAdapter retailerExtraAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_retailer_detail, container, false);


        final LinearLayout ll = (LinearLayout) v.findViewById(R.id.ellipe_overlay);
        ll.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        ro = (RetailerOffers) bundle.getSerializable("RETAILEROFFERS");
        Retailer rT = ro.getRetailer();
        Offer[] o = ro.getOffers();

        origin = bundle.getString("ORIGIN");

        TextView back = (TextView) v.findViewById(R.id.imgBtnBack);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (origin.equals("list")) {
                    FragmentList nextFrag = new FragmentList();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.theFragment, nextFrag)
                            .addToBackStack(null)
                            .commit();
                } else {
                    FragmentOffer nextFrag = new FragmentOffer();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.theFragment, nextFrag)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        TextView companyName = (TextView) v.findViewById(R.id.txtCompanyName);
        companyName.setText(ro.getName());
        TextView address = (TextView) v.findViewById(R.id.txtAddress);
        address.setText(formatAddress(ro));
        imageView = (ImageView) v.findViewById(R.id.imgBigLogo);
        new ImageLoadTask(Constants.BASE_URL + rT.getLargeImage(), imageView).execute();

        offerListCustomAdapter = new OfferListCustomAdapter(getActivity(), o);
        setListAdapter(offerListCustomAdapter);


        ListView listView = (ListView) v.findViewById(R.id.list2);
        ArrayList extras = new ArrayList();
        extras.add(ro);
        extras.add(ro);
        retailerExtraAdapter = new RetailerExtraAdapter(getActivity(), extras);
        listView.setAdapter(retailerExtraAdapter);


        /*TextView ellipse = (TextView) v.findViewById(R.id.ellipse);
        ellipse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ll.isShown()) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                }

            }
        });*/

        ImageButton ellipse = (ImageButton) v.findViewById(R.id.ellipse);
        ellipse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ll.isShown()) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                }

            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);

    }

    public void myToast(String str, int len) {
        Toast.makeText(getActivity(), str, len).show();
    }

    private String formatAddress(RetailerOffers ro2) {
        String add1 = ro2.getAddress1();
        if (add1.endsWith(",")) {
            add1 = add1.substring(0, add1.lastIndexOf(","));
        }
        String add2 = ro2.getAddress2();
        String city = ro2.getCity();
        String pcode = ro2.getPostcode();
        return add1 + " " + city + " " + pcode;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;


        public ImageLoadTask(String url, ImageView imView) {
            this.url = url;
            imageView = imView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }


}