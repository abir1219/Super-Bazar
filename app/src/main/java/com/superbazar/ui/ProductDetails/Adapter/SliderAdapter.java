package com.superbazar.ui.ProductDetails.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.superbazar.R;
import com.superbazar.ui.ProductDetails.Model.SliderModel;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder>{
    List<SliderModel> modelList;
    Context context;

    public SliderAdapter(List<SliderModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Glide.with(context).load(modelList.get(position).getSlider()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
