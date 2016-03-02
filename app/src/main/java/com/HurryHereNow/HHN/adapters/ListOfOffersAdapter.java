package com.HurryHereNow.HHN.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.HurryHereNow.HHN.Constants;
import com.HurryHereNow.HHN.FragmentRetailerDetail;
import com.HurryHereNow.HHN.ImageLoader;
import com.HurryHereNow.HHN.R;
import com.HurryHereNow.HHN.data.Offer;
import com.HurryHereNow.HHN.data.Retailer;
import com.HurryHereNow.HHN.data.RetailerOfferForList;
import com.HurryHereNow.HHN.data.RetailerOffers;

import java.util.ArrayList;

/**
 * Created by iaindownie on 27/12/2015.
 */
public class ListOfOffersAdapter extends RecyclerView.Adapter<ListOfOffersAdapter.OfferListViewHolder>
{

    private Context mContext;
    private ArrayList aList;
    public ImageLoader imageLoader;


    public ListOfOffersAdapter(Context context, ArrayList aList) {
        mContext = context;
        this.aList = aList;
        imageLoader = new ImageLoader(context);
    }

    public class OfferListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image1;
        TextView txtComment;
        TextView txtName;

        public OfferListViewHolder(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.Image1);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    RetailerOfferForList ro = getItem(pos);
                    RetailerOffers unexpanded = ro.getRo();
                    Bundle bundle=new Bundle();
                    bundle.putString("ORIGIN", "list");
                    bundle.putInt("LISTPOSITION", pos);
                    bundle.putSerializable("RETAILEROFFERS", unexpanded);
                    FragmentRetailerDetail nextFrag = new FragmentRetailerDetail();
                    nextFrag.setArguments(bundle);
                    FragmentTransaction transaction = ((Activity) mContext).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.theFragment, nextFrag)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public OfferListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.item_list_offer, viewGroup, false);
        return new OfferListViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(OfferListViewHolder viewHolder, int i) {
        RetailerOfferForList ro = getItem(i);
        Offer o = ro.getAnOffer();
        Retailer r = ro.getRetailer();
        final RetailerOffers unexpanded = ro.getRo();
        //holder.image1.setImageResource(R.drawable.thumbsup);
        String imageUrl = Constants.WWW_BASE_URL + "/images/offers/" + o.getOfferId() + ".png";
        //imageLoader.DisplayImage(Constants.BASE_URL + r.getSmallImage(), holder.image1);
        imageLoader.DisplayImage(imageUrl, viewHolder.image1);
        viewHolder.txtComment.setText(o.getDescription());
        viewHolder.txtName.setText(ro.getName());
    }

    public RetailerOfferForList getItem(int position) {
        RetailerOfferForList ro = (RetailerOfferForList) aList.get(position);
        return ro;
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }


}