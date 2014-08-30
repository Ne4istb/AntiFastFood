package com.hackaton.ne4istb.antifastfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SuggestionAdapter extends BaseAdapter {

    private ArrayList<SuggestionRecord> list = new ArrayList<SuggestionRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public SuggestionAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
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
        ViewHolder holder;

        SuggestionRecord curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.suggestion_item, null);
            holder.name = (TextView) newView.findViewById(R.id.suggestion_name);
            holder.address = (TextView) newView.findViewById(R.id.suggestion_address);
            holder.site = (TextView) newView.findViewById(R.id.suggestion_site);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.name.setText("Name: " + curr.getName());
        holder.address.setText("Address: " + curr.getAddress());
        holder.site.setText("Site: " + curr.getSite());

        return newView;
    }

    static class ViewHolder {
        TextView name;
        TextView address;
        TextView site;
    }

    public void add(SuggestionRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<SuggestionRecord> getList(){
        return list;
    }

    public void removeAllViews(){
        list.clear();
        this.notifyDataSetChanged();
    }
}