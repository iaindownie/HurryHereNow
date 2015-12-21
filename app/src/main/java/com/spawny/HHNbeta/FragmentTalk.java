package com.spawny.HHNbeta;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spawny.HHNbeta.adapters.TalkCustomAdapter;

import java.util.ArrayList;

/**
 * Created by iaindownie on 29/11/2015.
 */
public class FragmentTalk extends ListFragment {

    SharedPreferences prefs;
    String rawTalkJSON = "";
    ArrayList allTalks;
    TalkCustomAdapter talkCustomAdapter;

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
        long lastTime = prefs.getLong("TALKGRAB_TIME", System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        long diffInTime = currentTime - lastTime;
        rawTalkJSON = prefs.getString("RAWTALKJSON", "");

        // If no data or data is 5 minutes old
        if (rawTalkJSON.length() == 0 || (diffInTime > 300000)) {
            new DownloadTalksTask().execute();
        } else {
            try {
                allTalks = JSONUtilities.convertJSONTalksToArrayList(rawTalkJSON);
                talkCustomAdapter = new TalkCustomAdapter(getActivity(), allTalks);
                setListAdapter(talkCustomAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class DownloadTalksTask extends AsyncTask<String, Void, String> {
        private final ProgressDialog asyncDialog = new ProgressDialog(
                getActivity());

        // can use UI thread here
        protected void onPreExecute() {
            this.asyncDialog.setTitle("Grabbing talk data");
            this.asyncDialog.setMessage("Please wait...");
            this.asyncDialog.show();
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
            talkCustomAdapter = new TalkCustomAdapter(getActivity(), allTalks);
            setListAdapter(talkCustomAdapter);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("RAWTALKJSON", rawTalkJSON);
            editor.putLong("TALKGRAB_TIME", System.currentTimeMillis());
            editor.apply();
            if (this.asyncDialog.isShowing()) {
                this.asyncDialog.dismiss();
            }
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