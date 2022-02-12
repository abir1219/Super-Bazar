package com.superbazar.ui.Home.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.superbazar.ui.Home.Model.BannerModel;
import com.superbazar.R;

import java.util.ArrayList;

public class BannerAdapter extends SliderViewAdapter<BannerAdapter.BannerViewHolder> {


    ArrayList<BannerModel> bannerModelList;

    public BannerAdapter(ArrayList<BannerModel> bannerModelList) {
        this.bannerModelList = bannerModelList;
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent) {
        return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(bannerModelList.get(position).getSlider())).into(holder.imageView);

    }

    @Override
    public int getCount() {
        return bannerModelList.size();
    }

    public class BannerViewHolder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;

        public BannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
