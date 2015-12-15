package com.HurryHereNow.HHN.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.HurryHereNow.HHN.Constants;
import com.HurryHereNow.HHN.FragmentOffer;
import com.HurryHereNow.HHN.FragmentRetailerDetail;
import com.HurryHereNow.HHN.ImageLoader;
import com.HurryHereNow.HHN.R;
import com.HurryHereNow.HHN.data.Offer;
import com.HurryHereNow.HHN.data.Retailer;
import com.HurryHereNow.HHN.data.RetailerOfferForList;
import com.HurryHereNow.HHN.data.RetailerOffers;

import java.util.ArrayList;

/**
 * Created by iaindownie on 14/12/2015.
 */
public class OfferCustomAdapter extends BaseAdapter {
    private ArrayList aList;
    private LayoutInflater layoutInflater;
    final Context context;
    public ImageLoader imageLoader;

    public OfferCustomAdapter(Context aContext, ArrayList aList) {
        // TODO Auto-generated constructor stub
        this.aList = aList;
        layoutInflater = LayoutInflater.from(aContext);
        context = aContext;
        imageLoader=new ImageLoader(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return aList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return aList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    static class ViewHolder {
        ImageView image1;
        TextView txtComment;
        TextView txtName;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int innerPosition = position;
        RetailerOfferForList ro = (RetailerOfferForList) aList.get(position);
        Offer o = ro.getAnOffer();
        Retailer r = ro.getRetailer();
        final RetailerOffers unexpanded = ro.getRo();

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.customoffer_item, null);
            holder = new ViewHolder();
            holder.image1 = (ImageView) convertView.findViewById(R.id.Image1);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.image1.setImageResource(R.drawable.thumbsup);
        imageLoader.DisplayImage(Constants.BASE_URL + r.getSmallImage(), holder.image1);
        holder.txtComment.setText(o.getDescription());
        holder.txtName.setText(ro.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("ORIGIN", "list");
                bundle.putSerializable("RETAILEROFFERS", unexpanded);
                FragmentRetailerDetail nextFrag = new FragmentRetailerDetail();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
                transaction.replace(R.id.theFragment, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return convertView;
    }

}