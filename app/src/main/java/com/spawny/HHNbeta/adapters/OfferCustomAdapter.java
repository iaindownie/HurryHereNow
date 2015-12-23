package com.spawny.HHNbeta.adapters;

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

import com.spawny.HHNbeta.Constants;
import com.spawny.HHNbeta.FragmentRetailerDetail;
import com.spawny.HHNbeta.ImageLoader;
import com.spawny.HHNbeta.R;
import com.spawny.HHNbeta.data.Offer;
import com.spawny.HHNbeta.data.Retailer;
import com.spawny.HHNbeta.data.RetailerOfferForList;
import com.spawny.HHNbeta.data.RetailerOffers;

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
        this.aList = aList;
        layoutInflater = LayoutInflater.from(aContext);
        context = aContext;
        imageLoader=new ImageLoader(context);
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
            convertView = layoutInflater.inflate(R.layout.item_list_offer, null);
            holder = new ViewHolder();
            holder.image1 = (ImageView) convertView.findViewById(R.id.Image1);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.image1.setImageResource(R.drawable.thumbsup);
        String imageUrl = Constants.WWW_BASE_URL + "/images/offers/" + o.getOfferId() + ".png";
        //imageLoader.DisplayImage(Constants.BASE_URL + r.getSmallImage(), holder.image1);
        imageLoader.DisplayImage(imageUrl, holder.image1);
        holder.txtComment.setText(o.getDescription());
        holder.txtName.setText(ro.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("ORIGIN", "list");
                bundle.putInt("LISTPOSITION", innerPosition);
                bundle.putSerializable("RETAILEROFFERS", unexpanded);
                FragmentRetailerDetail nextFrag = new FragmentRetailerDetail();
                nextFrag.setArguments(bundle);
                FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
                transaction.replace(R.id.theFragment, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        notifyDataSetChanged();
        return convertView;
    }

}