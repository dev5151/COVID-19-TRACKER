package com.dev5151.covid_19info.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dev5151.covid_19info.R;

import java.util.List;

public class MeasureAdapter extends RecyclerView.Adapter<MeasureAdapter.MeasureViewHolder> {
    List<String> list;
    Context context;


    public MeasureAdapter(@NonNull List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MeasureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.measure_item, parent, false);
        return new MeasureAdapter.MeasureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasureViewHolder holder, int position) {
        String url = list.get(position);
        Glide.with(context).load("https://raw.githubusercontent.com/NovelCOVID/API/master/assets/flags/in.png").into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MeasureViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public MeasureViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.picture);
        }
    }
}
