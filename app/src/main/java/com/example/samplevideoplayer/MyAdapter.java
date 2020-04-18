package com.example.samplevideoplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] mDataset;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public View view;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.item_title);
            view = v;

        }
    }

    public MyAdapter(Context context,String[] myDataset) {
        mDataset = myDataset;
        context = context;
    }


    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("VideoPlayer","ClickListener of the recyclerview" +position);
            }
        });
        holder.textView.setText(mDataset[position]);

    }


    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}