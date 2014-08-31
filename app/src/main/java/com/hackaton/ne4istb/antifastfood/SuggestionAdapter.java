package com.hackaton.ne4istb.antifastfood;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class SuggestionAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private ArrayList<SuggestionRecord> list = new ArrayList<SuggestionRecord>();
    private Context mContext;
    private Location location;

    private ImageThreadLoader imageLoader;

    public SuggestionAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        imageLoader = new ImageThreadLoader(mContext);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        final ViewHolder holder;

        final SuggestionRecord curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.suggestion_item, null);
            holder.image = (ImageView) newView.findViewById(R.id.leftside);
            holder.googleWay = (ImageView) newView.findViewById(R.id.googleway);
            holder.name = (TextView) newView.findViewById(R.id.suggestion_name);
            holder.address = (TextView) newView.findViewById(R.id.suggestion_address);
            holder.site = (TextView) newView.findViewById(R.id.suggestion_site);
            holder.distance = (TextView) newView.findViewById(R.id.suggestion_distance);
            newView.setTag(holder);
        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.name.setText(mContext.getString(R.string.name) + " " + curr.getName());

        if (curr.getAddress().trim() != "")
            holder.address.setText(mContext.getString(R.string.address) + " " + curr.getAddress());
        else
            holder.address.setVisibility(View.GONE);

        if (curr.getSite().trim() != "")
            holder.site.setText(mContext.getString(R.string.site) + " " + curr.getSite());
        else
            holder.address.setVisibility(View.GONE);

        holder.distance.setText(mContext.getString(R.string.distance) + " " + curr.getDistance() + " метрів");


        AssetManager assetManager = mContext.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("way_2.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bMap = BitmapFactory.decodeStream(inputStream);
        holder.googleWay.setImageBitmap(bMap);


        Bitmap cachedImage = null;
        try {
            cachedImage = imageLoader.loadImage(curr.getThumbnail(), new ImageThreadLoader.ImageLoadedListener() {
                public void imageLoaded(Bitmap imageBitmap) {
                    holder.image.setImageBitmap(imageBitmap);

                    notifyDataSetChanged();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (cachedImage != null) {
            holder.image.setImageBitmap(cachedImage);
        }

        holder.googleWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uriString = "http://maps.google.com/maps?"
                        + "saddr=" + Double.toString(location.getLatitude())
                        + "," + Double.toString(location.getLongitude())
                        + "&daddr=" + Double.toString(curr.getCoordinate().getLatitude())
                        + "," + Double.toString(curr.getCoordinate().getLongtitude());

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uriString));
                mContext.startActivity(intent);
            }
        });

        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uriString = "https://foursquare.com/venue/" + curr.getId();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uriString));
                mContext.startActivity(intent);
            }
        });

        return newView;
    }

    public void setCurrentLocation(Location currentLocation) {
        location = currentLocation;
    }

    public void add(SuggestionRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<SuggestionRecord> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView name;
        TextView address;
        TextView site;
        TextView distance;
        ImageView image;
        ImageView googleWay;
    }
}