package com.superbazar.ui.Cart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.superbazar.R;
import com.superbazar.ui.Cart.CartFragment;
import com.superbazar.ui.Cart.Model.CartModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<CartModel> modelList;
    Context context;
    public CartFragment.onDataRecived onDataRecived;

    public CartAdapter(List<CartModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPName.setText(modelList.get(position).getProductName());
        holder.tvPDesc.setText(modelList.get(position).getDesc());
        holder.tvAmount.setText("₹ "+modelList.get(position).getOffPrice());
        holder.tvS_Amount.setText("₹ "+modelList.get(position).getActualPrice());
        holder.tvCount.setText(modelList.get(position).getQuantity());

        Glide.with(context).load(modelList.get(position).getProdImage()).into(holder.ivPPic);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPName, tvPDesc, tvAmount, tvS_Amount, tvPRemove, tvCount;
        ImageView ivMinus, ivPlus, ivPPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPName = itemView.findViewById(R.id.tvPName);
            tvPDesc = itemView.findViewById(R.id.tvPDesc);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvS_Amount = itemView.findViewById(R.id.tvS_Amount);
            tvPRemove = itemView.findViewById(R.id.tvPRemove);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            ivPPic = itemView.findViewById(R.id.ivPPic);
        }
    }

    public void setListner(CartFragment.onDataRecived onDataRecived) {
        this.onDataRecived = onDataRecived;
    }
}
