package com.HurryHereNow.HHN;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.HurryHereNow.HHN.adapters.TalkRecyclerCustomAdapter;

import java.util.ArrayList;

/**
 * Created by iaindownie on 29/11/2015.
 */
public class FragmentTalk extends Fragment {

    private SharedPreferences prefs;
    private String rawTalkJSON = "";
    private ArrayList allTalks;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TalkRecyclerCustomAdapter mTalkRecyclerCustomAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        allTalks = new ArrayList();

        prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        //long lastTime = prefs.getLong("TALKGRAB_TIME", System.currentTimeMillis());
        //long currentTime = System.currentTimeMillis();
        //long diffInTime = currentTime - lastTime;
        rawTalkJSON = prefs.getString("RAWTALKJSON", "");

        new DownloadTalksTask().execute();

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.getActivity().findViewById(R.id.talk_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) this.getActivity().findViewById(R.id.talk_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);


        try {
            allTalks = JSONUtilities.convertJSONTalksToArrayList(rawTalkJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupAdapter(allTalks);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new DownloadTalksTask().execute();
                        //setupAdapter(allTalks);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        /*lv = (ListView) getActivity().findViewById(R.id.talkList);

        // If no data or data is 5 minutes old
        if (rawTalkJSON.length() == 0 || (diffInTime > 300000)) {
            new DownloadTalksTask().execute();
        } else {
            try {
                allTalks = JSONUtilities.convertJSONTalksToArrayList(rawTalkJSON);
                talkCustomAdapter = new TalkCustomAdapterOLD(getActivity(), allTalks);
                lv.setAdapter(talkCustomAdapter);
                //setListAdapter(talkCustomAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }

    private void setupAdapter(ArrayList aList) {
        mTalkRecyclerCustomAdapter = new TalkRecyclerCustomAdapter(getActivity(), aList);
        mRecyclerView.setAdapter(mTalkRecyclerCustomAdapter);
    }

    private class DownloadTalksTask extends AsyncTask<String, Void, String> {

        // can use UI thread here
        protected void onPreExecute() {
        }

        // automatically done on worker thread (separate from UI thread)
        protected String doInBackground(final String... args) {
            try {
                String path = Constants.API_BASE_URL + "/api/talk";
                rawTalkJSON = JSONUtilities.downloadAllTalksFromURL(path);
                allTalks = JSONUtilities.convertJSONTalksToArrayList(rawTalkJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        // can use UI thread here
        protected void onPostExecute(final String result) {
            if (allTalks.size() == 0) {
                Utils.myToast(getActivity(), "No user comments in your area", Toast.LENGTH_LONG);
            }

            setupAdapter(allTalks);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("RAWTALKJSON", rawTalkJSON);
            editor.putLong("TALKGRAB_TIME", System.currentTimeMillis());
            editor.apply();

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
        setupAdapter(allTalks);
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