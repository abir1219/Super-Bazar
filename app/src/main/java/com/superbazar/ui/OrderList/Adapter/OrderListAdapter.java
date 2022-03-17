package com.superbazar.ui.OrderList.Adapter;

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

import com.superbazar.R;
import com.superbazar.ui.OrderList.Model.OrderListModel;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    List<OrderListModel> modelList;
    Context context;
    String deliveryStatus = "";

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
        holder.tvId.setText("Order Id : "+modelList.get(position).getOrder_number());
        holder.tvDate.setText(modelList.get(position).getDateTime());
        holder.tvPrice.setText("Rs."+modelList.get(position).getTotalAmount());

        for(int i=0;i<modelList.size();i++){
            if(!modelList.get(position).getOrderStatus().equalsIgnoreCase("cancel")){
                deliveryStatus = modelList.get(position).getOrderStatus();
                break;
            }else{
                deliveryStatus = modelList.get(position).getOrderStatus();
            }
        }

        if (deliveryStatus.equalsIgnoreCase("cancel")){
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_z_cross));//ic_z_cross
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_z_cross));//ic_zchecked3
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_z_cross));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_z_cross));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.red));
            //}else if (orederPreModels.get(position).getmListOrder().get(0).getDelivery_status().equalsIgnoreCase("order_placed")){
        }else if (deliveryStatus.equalsIgnoreCase("placed")){
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked2));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked2));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked2));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            //}else if (orederPreModels.get(position).getmListOrder().get(0).getDelivery_status().equalsIgnoreCase("on_process")){
        }else if (deliveryStatus.equalsIgnoreCase("proccess")){
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked2));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked2));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            //}else if (orederPreModels.get(position).getmListOrder().get(0).getDelivery_status().equalsIgnoreCase("despatch")){
        }else if (deliveryStatus.equalsIgnoreCase("dispatch")){
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked2));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            //}else if (orederPreModels.get(position).getmListOrder().get(0).getDelivery_status().equalsIgnoreCase("delivered")){
        }else if (deliveryStatus.equalsIgnoreCase("delivered")){
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.green5));
        }



        holder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id",modelList.get(position).getOrderId());
                bundle.putString("dateTime",modelList.get(position).getDateTime());
                bundle.putString("amount",modelList.get(position).getTotalAmount());
                Navigation.findNavController(view).navigate(R.id.nav_order_list_to_order_details,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvId,tvDate,tvViewDetails,tvPrice;
        private ImageView ivO_P,ivO_OP, ivO_D, ivO_O;
        private View vO_P, vO_OP, vO_D, vViewDispatchImages, vViewOnprocess, vViewDeliverd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivO_P = itemView.findViewById(R.id.ivO_P);
            ivO_OP = itemView.findViewById(R.id.ivO_OP);
            ivO_D = itemView.findViewById(R.id.ivO_D);
            ivO_O = itemView.findViewById(R.id.ivO_O);
            vO_P = itemView.findViewById(R.id.vO_P);
            vO_OP = itemView.findViewById(R.id.vO_OP);
            vO_D = itemView.findViewById(R.id.vO_D);
            vViewDispatchImages = itemView.findViewById(R.id.vViewDispatchImages);
            vViewOnprocess = itemView.findViewById(R.id.vViewOnprocess);
            vViewDeliverd = itemView.findViewById(R.id.vViewDeliverd);
        }
    }
}
