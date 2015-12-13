package com.android.ifmo_android_2015.loafer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import model.EasyEvent;

/**
 * Created by daniil on 05.12.15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<EasyEvent> mDataset;

    public RecyclerViewAdapter(List<EasyEvent> dataset) {
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
        holder.name.setText(mDataset.get(position).getName());
        holder.address.setText(mDataset.get(position).getAddress());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(mDataset.get(position).getDefaultImageUrl(), holder.image);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

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
}