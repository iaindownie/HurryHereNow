package com.spawny.HHNbeta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spawny.HHNbeta.Constants;
import com.spawny.HHNbeta.ImageLoader;
import com.spawny.HHNbeta.R;
import com.spawny.HHNbeta.Utils;
import com.spawny.HHNbeta.data.Offer;

/**
 * Created by iaindownie on 15/12/2015.
 */
public class OfferListCustomAdapter extends BaseAdapter {
    private Offer[] o;
    private LayoutInflater layoutInflater;
    final Context context;
    public ImageLoader imageLoader;

    public OfferListCustomAdapter(Context aContext, Offer[] o) {
        this.o = o;
        layoutInflater = LayoutInflater.from(aContext);
        context = aContext;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return o.length;
    }

    @Override
    public Object getItem(int position) {
        return o[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView image1;
        TextView txtComment;
        TextView txtDaysLeft;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int innerPosition = position;
        Offer anOffer = o[position];

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_retailer_offer_list, null);
            holder = new ViewHolder();
            holder.image1 = (ImageView) convertView.findViewById(R.id.Image1);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            holder.txtDaysLeft = (TextView) convertView.findViewById(R.id.txtDaysLeft);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imageUrl = Constants.WWW_BASE_URL + "/images/offers/" + anOffer.getOfferId() + ".png";
        imageLoader.DisplayImage(imageUrl, holder.image1);
        holder.txtComment.setText(anOffer.getDescription());
        holder.txtDaysLeft.setText(Utils.getDaysRemaining(anOffer.getEndDate()) + "d");
        notifyDataSetChanged();
        return convertView;
    }



}