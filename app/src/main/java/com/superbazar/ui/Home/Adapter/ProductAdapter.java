package com.superbazar.ui.Home.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.superbazar.R;
import com.superbazar.ui.Home.Model.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<ProductModel> modelList;
    Context context;

    public ProductAdapter(List<ProductModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(modelList.get(position).getImage()).into(holder.productImage);
        holder.productName.setText(modelList.get(position).getName());
        holder.shpPrice.setText("₹ "+modelList.get(position).getShopPrice());
        holder.actPrice.setText("₹ "+modelList.get(position).getActualPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id",modelList.get(position).getId());
                Navigation.findNavController(view).navigate(R.id.nav_home_to_product_details,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
        //return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName,shpPrice,actPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            shpPrice = itemView.findViewById(R.id.shpPrice);
            actPrice = itemView.findViewById(R.id.actPrice);
        }
    }
}
