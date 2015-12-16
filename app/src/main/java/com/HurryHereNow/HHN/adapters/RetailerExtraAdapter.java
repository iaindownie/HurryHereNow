package com.HurryHereNow.HHN.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.HurryHereNow.HHN.FragmentOffer;
import com.HurryHereNow.HHN.R;
import com.HurryHereNow.HHN.data.RetailerOffers;

import java.util.ArrayList;

/**
 * Created by iaindownie on 16/12/2015.
 */
public class RetailerExtraAdapter extends BaseAdapter {
    private ArrayList aList;
    private LayoutInflater layoutInflater;
    Context context;

    public RetailerExtraAdapter(Context aContext, ArrayList aList) {
        this.aList = aList;
        layoutInflater = LayoutInflater.from(aContext);
        context = aContext;
    }

    @Override
    public int getCount() {
        return aList.size();
    }

    @Override
    public Object getItem(int position) {
        return aList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView image1;
        TextView txtComment;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int innerPosition = position;
        final RetailerOffers ro = (RetailerOffers) aList.get(innerPosition);

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.customoffer_item, null);
            holder = new ViewHolder();
            holder.image1 = (ImageView) convertView.findViewById(R.id.Image1);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (innerPosition == 0) {
            holder.image1.setImageResource(R.drawable.chatgrey100);
            holder.txtComment.setText("View on map");
        } else {
            holder.image1.setImageResource(R.drawable.website);
            holder.txtComment.setText("Visit website");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (innerPosition == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("LAT", ro.getLatitude());
                    bundle.putDouble("LON", ro.getLongitude());
                    FragmentOffer nextFrag = new FragmentOffer();
                    nextFrag.setArguments(bundle);
                    FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.theFragment, nextFrag)
                            .addToBackStack(null)
                            .commit();
                } else {
                    String website = ro.getSite();
                    String urlStart = "http://";
                    if(!website.startsWith(urlStart)){
                        website = urlStart + website;
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(website));
                    ((Activity) context).startActivity(i);
                }

            }
        });
        return convertView;
    }

}
