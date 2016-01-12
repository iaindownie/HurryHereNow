package com.HurryHereNow.HHN.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.HurryHereNow.HHN.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iaindownie on 27/12/2015.
 */
public class TalkRecyclerCustomAdapter extends RecyclerView.Adapter<TalkRecyclerCustomAdapter.TalkViewHolder> {

    private Context mContext;
    List<HashMap> mTalks;

    public TalkRecyclerCustomAdapter(Context context, ArrayList aList) {
        mContext = context;
        mTalks = aList;
    }

    public class TalkViewHolder extends RecyclerView.ViewHolder {
        ImageView likeBooleanImage;
        TextView txtComment;
        TextView txtDescription;
        TextView txtName;
        //ImageView clock;
        TextView txtOfferDate;

        public TalkViewHolder(View itemView) {
            super(itemView);
            likeBooleanImage = (ImageView) itemView.findViewById(R.id.likeBooleanImage);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            //clock = (ImageView) itemView.findViewById(R.id.clock);
            txtOfferDate = (TextView) itemView.findViewById(R.id.txtOfferDate);
        }
    }


    @Override
    public TalkViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.item_talk, viewGroup, false);
        return new TalkViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(TalkViewHolder viewHolder, int i) {
        HashMap aTalk = getItem(i);
        if (((String) aTalk.get("type")).equals("1")) {
            viewHolder.likeBooleanImage.setImageResource(R.drawable.thumbsup);
        }
        viewHolder.txtComment.setText((String) aTalk.get("comment"));
        viewHolder.txtDescription.setText((String) aTalk.get("description"));
        viewHolder.txtName.setText((String) aTalk.get("name"));
        viewHolder.txtOfferDate.setText((String) aTalk.get("date"));
    }

    public HashMap getItem(int position) {
        HashMap hm = (HashMap)mTalks.get(position);
        return hm;
    }

    @Override
    public int getItemCount() {
        return mTalks.size();
    }


}