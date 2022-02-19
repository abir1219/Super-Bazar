package com.superbazar.ui.Wishlist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superbazar.R;
import com.superbazar.ui.Cart.CartFragment;
import com.superbazar.ui.Wishlist.Model.WishlistModel;
import com.superbazar.ui.Wishlist.WishlistFragment;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    List<WishlistModel> modelList;
    Context context;
    public WishlistFragment.onDataRecived onDataRecived;

    public WishlistAdapter(List<WishlistModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wish,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(modelList.get(position).getProductName());
        holder.tvDetails.setText(modelList.get(position).getDesc());
        holder.tvOffPrice.setText(modelList.get(position).getOffPrice());
        holder.tvCount.setText(modelList.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDetails,tvOffPrice,tvCount;
        ImageView ivPPic,ivPlus,ivMinus;
        RelativeLayout rvRemoveWish,rlCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvOffPrice = itemView.findViewById(R.id.tvOffPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivPPic = itemView.findViewById(R.id.ivPPic);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            rvRemoveWish = itemView.findViewById(R.id.rvRemoveWish);
            rlCart = itemView.findViewById(R.id.rlCart);
        }
    }

    public void setListner(WishlistFragment.onDataRecived onDataRecived) {
        this.onDataRecived = onDataRecived;
    }
}
