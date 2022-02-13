package com.superbazar.ui.ProductList.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.superbazar.R;
import com.superbazar.ui.ProductList.Model.ProductListModel;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    List<ProductListModel> modelList;
    Context context;

    public ProductListAdapter(List<ProductListModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(modelList.get(position).getProdName());
        holder.tvDetails.setText(modelList.get(position).getShortDesc());
        holder.tvOffPrice.setText("₹ "+modelList.get(position).getActualPrice());
        holder.tvPrice.setText("₹ "+modelList.get(position).getShopPrice());
        Glide.with(context).load(modelList.get(position).getProdImage()).into(holder.ivPPic);

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
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPPic,ivMinus,ivPlus;
        TextView tvTitle,tvDetails,tvOffPrice,tvPrice;
        EditText tvCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCount = itemView.findViewById(R.id.tvCount);
            ivPPic = itemView.findViewById(R.id.ivPPic);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvOffPrice = itemView.findViewById(R.id.tvOffPrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
