package com.superbazar.ui.Address.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superbazar.R;
import com.superbazar.ui.Address.Model.AddressModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    List<AddressModel> modelList;
    Context context;

    public AddressAdapter(List<AddressModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(modelList.get(position).getName());
        holder.tvLandmark.setText(modelList.get(position).getLandmark());
        holder.tvPhone.setText(modelList.get(position).getPhone());
        holder.tvAddPin.setText(modelList.get(position).getAddress()+" , "+modelList.get(position).getPincode());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvLandmark,tvAddPin,tvPhone;
        ImageView ivEdit,ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLandmark = itemView.findViewById(R.id.tvLandmark);
            tvAddPin = itemView.findViewById(R.id.tvAddPin);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
