package com.spawny.HHNbeta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spawny.HHNbeta.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by iaindownie on 30/11/2015.
 */
public class TalkCustomAdapter extends BaseAdapter {
    ArrayList aList;
    private LayoutInflater layoutInflater;
    //Context context;

    public TalkCustomAdapter(Context aContext, ArrayList aList) {
        this.aList = aList;
        layoutInflater = LayoutInflater.from(aContext);
        //context = aContext;
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
        ImageView likeBooleanImage;
        TextView txtComment;
        TextView txtDescription;
        TextView txtName;
        ImageView clock;
        TextView txtOfferDate;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int innerPosition = position;
        HashMap aMap = (HashMap) aList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.talk_item, null);
            holder = new ViewHolder();
            holder.likeBooleanImage = (ImageView) convertView.findViewById(R.id.likeBooleanImage);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.clock = (ImageView) convertView.findViewById(R.id.clock);
            holder.txtOfferDate = (TextView) convertView.findViewById(R.id.txtOfferDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (((String) aMap.get("type")).equals("1")) {
            holder.likeBooleanImage.setImageResource(R.drawable.thumbsup);
        }
        holder.txtComment.setText((String) aMap.get("comment"));
        holder.txtDescription.setText((String) aMap.get("description"));
        holder.txtName.setText((String) aMap.get("name"));
        holder.txtOfferDate.setText((String) aMap.get("date"));
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item " + (innerPosition + 1) + ": Option to do more...", Toast.LENGTH_SHORT).show();
            }
        });*/
        return convertView;
    }

}