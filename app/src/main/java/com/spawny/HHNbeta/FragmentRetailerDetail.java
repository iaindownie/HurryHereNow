package com.spawny.HHNbeta;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spawny.HHNbeta.adapters.RetailerOfferListAdapter;
import com.spawny.HHNbeta.adapters.RetailerExtraAdapter;
import com.spawny.HHNbeta.data.Offer;
import com.spawny.HHNbeta.data.Retailer;
import com.spawny.HHNbeta.data.RetailerOffers;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentRetailerDetail extends ListFragment {

    private SharedPreferences prefs;
    private RetailerOffers ro;
    private String origin = "map";
    private int listPosition = 0;
    private ImageView imageView;
    private RetailerOfferListAdapter offerListCustomAdapter;
    private RetailerExtraAdapter retailerExtraAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_retailer_detail, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);

        final RelativeLayout ll = (RelativeLayout) getActivity().findViewById(R.id.ellipe_overlay);
        ll.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        ro = (RetailerOffers) bundle.getSerializable("RETAILEROFFERS");
        Retailer rT = ro.getRetailer();
        Offer[] o = ro.getOffers();

        origin = bundle.getString("ORIGIN", "map");

        listPosition = bundle.getInt("LISTPOSITION", 0);

        TextView back = (TextView) getActivity().findViewById(R.id.imgBtnBack);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (origin.equals("list")) {
                    Bundle bundle=new Bundle();
                    bundle.putInt("LISTPOSITION", listPosition);
                    FragmentList nextFrag = new FragmentList();
                    nextFrag.setArguments(bundle);
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

        TextView companyName = (TextView) getActivity().findViewById(R.id.txtCompanyName);
        companyName.setText(ro.getName());
        TextView address = (TextView) getActivity().findViewById(R.id.txtAddress);
        address.setText(formatAddress(ro));
        imageView = (ImageView) getActivity().findViewById(R.id.imgBigLogo);
        new ImageLoadTask(Constants.WWW_BASE_URL + rT.getLargeImage(), imageView).execute();

        offerListCustomAdapter = new RetailerOfferListAdapter(getActivity(), o);
        setListAdapter(offerListCustomAdapter);


        ListView listView = (ListView) getActivity().findViewById(R.id.list2);
        ArrayList extras = new ArrayList();
        extras.add(ro);
        extras.add(ro);
        retailerExtraAdapter = new RetailerExtraAdapter(getActivity(), extras);
        listView.setAdapter(retailerExtraAdapter);


        ImageButton ellipse = (ImageButton) getActivity().findViewById(R.id.ellipse);
        ellipse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ll.isShown()) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                }

            }
        });

        ImageView imgBigLogo = (ImageView) getActivity().findViewById(R.id.imgBigLogo);
        imgBigLogo.getLayoutParams().height = Constants.SCREENHEIGHT / 3;

    }

    public void myToast(String str, int len) {
        Toast.makeText(getActivity(), str, len).show();
    }

    private String formatAddress(RetailerOffers ro2) {
        String add1 = ro2.getAddress1();
        if (add1.endsWith(",")) {
            add1 = add1.substring(0, add1.lastIndexOf(","));
        }
        //String add2 = ro2.getAddress2();
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

    /**
     * The methods below are templates added to try to prevent the
     * fragments crashing on loading after sleep.
     */
    public static final String TAG = "FragmentRetailerDetail";

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