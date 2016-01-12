package com.HurryHereNow.HHN;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by iaindownie on 29/11/2015.
 * Empty Fragment to initiate the whole fragment palava. This is instantly
 * replaced programmatically in MainActivity
 */
public class FragmentFiller extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_filler, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("INFO", "FragmentFiller onActivityCreated");
    }


}