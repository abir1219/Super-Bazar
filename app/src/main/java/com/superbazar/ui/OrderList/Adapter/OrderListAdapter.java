package com.superbazar.ui.OrderList.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.superbazar.R;
import com.superbazar.ui.OrderList.Model.OrderListModel;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    List<OrderListModel> modelList;
    Context context;

    public OrderListAdapter(List<OrderListModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_pre,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvId.setText("Order Id : "+modelList.get(position).getOrderId());
        holder.tvDate.setText(modelList.get(position).getDateTime());
        holder.tvPrice.setText("Rs."+modelList.get(position).getTotalAmount());


        holder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id",modelList.get(position).getOrderId());
                bundle.putString("dateTime",modelList.get(position).getDateTime());
                bundle.putString("amount",modelList.get(position).getTotalAmount());
                //Navigation.findNavController(view).navigate(R.id.action_nav_order_list_to_order_details,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvId,tvDate,tvViewDetails,tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
