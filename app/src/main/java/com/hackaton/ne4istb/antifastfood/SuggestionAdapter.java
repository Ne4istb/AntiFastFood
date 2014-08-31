package com.hackaton.ne4istb.antifastfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SuggestionAdapter extends BaseAdapter {

    private ArrayList<SuggestionRecord> list = new ArrayList<SuggestionRecord>();
    private static LayoutInflater inflater = null;
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
            holder.image = (ImageView) newView.findViewById(R.id.flag);
            holder.name = (TextView) newView.findViewById(R.id.suggestion_name);
            holder.address = (TextView) newView.findViewById(R.id.suggestion_address);
            holder.site = (TextView) newView.findViewById(R.id.suggestion_site);
            newView.setTag(holder);
        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.name.setText(mContext.getString(R.string.name) + curr.getName());
        holder.address.setText(mContext.getString(R.string.address) + curr.getAddress());
        holder.site.setText(mContext.getString(R.string.site) + curr.getSite());

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

        newView.setOnClickListener(new View.OnClickListener() {
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

        return newView;
    }

    public void setCurrentLocation(Location currentLocation) {
        location = currentLocation;
    }

    static class ViewHolder {
        TextView name;
        TextView address;
        TextView site;
        ImageView image;
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
}