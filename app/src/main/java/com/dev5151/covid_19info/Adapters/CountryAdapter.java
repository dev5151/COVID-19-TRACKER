package com.dev5151.covid_19info.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dev5151.covid_19info.Models.Country;
import com.dev5151.covid_19info.R;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<Country> countryList;
    private Context context;


    private int lastPosition = -1;

    public CountryAdapter(List<Country> countryList, Context context) {
        this.countryList = countryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_item, parent, false);
        return new CountryAdapter.CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country countryItem = countryList.get(position);
        holder.country.setText(countryItem.getCountry());
        holder.cases.setText(String.valueOf(countryItem.getCases()));
        Glide.with(context).load(countryItem.getCountryFlag().getFlag()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.flag);
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        private ImageView flag;
        private TextView country, cases;
        ProgressBar progressBar;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            flag = itemView.findViewById(R.id.flag);
            country = itemView.findViewById(R.id.country);
            cases = itemView.findViewById(R.id.cases);
            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
