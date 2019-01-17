package com.example.sir.tinstapp;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class RecImageViewAdapter extends RecyclerView.Adapter<RecImageViewAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<String> imageThumbList;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public RecImageViewAdapter(Context context, ArrayList <String> imageThumbList) {
        this.imageThumbList = imageThumbList;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoader(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.media_list_inflater, null);
        RecImageViewAdapter.MyViewHolder holder = new RecImageViewAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        imageLoader.DisplayImage(imageThumbList.get(position), holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return imageThumbList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto=itemView.findViewById(R.id.ivImage);
        }
    }
}
