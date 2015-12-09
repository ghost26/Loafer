package com.android.ifmo_android_2015.loafer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by daniil on 05.12.15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

private String[] mDataset;

public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView address;
    public ImageView image;

    public ViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.recycler_item_name);
        address = (TextView) v.findViewById(R.id.recycler_item_address);
        image = (ImageView) v.findViewById(R.id.recycler_item_image);
    }
}

    public RecyclerViewAdapter(String[] dataset) {
        mDataset = dataset;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        // change layout's params. here

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}