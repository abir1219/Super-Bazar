package com.superbazar.ui.OrderDetails.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.superbazar.R;
import com.superbazar.ui.OrderDetails.Model.OrderDetailModel;
import com.superbazar.ui.OrderDetails.OrderDetailsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    List<OrderDetailModel> modelList;
    Context context;
    public OrderDetailsFragment.onDataRecived4 onDataRecived4;
    Calendar calendar;

    long totalDayTimeInSecond = 24 * 60 * 60;
    int orderTimeInSecond = 0;

    public OrderDetailsAdapter(List<OrderDetailModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_order_details_product, parent
                , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPName.setText(modelList.get(position).getProdName());
        holder.tvPDetails.setText(modelList.get(position).getProdDetails());
        holder.tvQty.setText("Qty: " + modelList.get(position).getQuantity());
        holder.tvAmount.setText("Rs. " + modelList.get(position).getPrice());

        /*if(!modelList.get(position).getCancelDateTime().equals("null")){
            //holder.tvCancelDateTime.setText("Cancelled Date & Time: "+modelList.get(position).getCancelDateTime());
            holder.tvCancelDateTime.setText(modelList.get(position).getCancelDateTime());
        }else{
            holder.tvCancelDateTime.setVisibility(View.GONE);
        }

        calendar = Calendar.getInstance();
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        int currentTimeInSecond = ((hr * 60 * 60) + (min * 60) + sec);

        String dateTime = modelList.get(position).getDate_time();
        String[] dateTimeSplit = dateTime.split(" ");

        String time = dateTimeSplit[1];
        String[] timeSplit = time.split(":");

        for (int i = 0; i < timeSplit.length; i++) {
            if (i == 0) {
                orderTimeInSecond += Integer.parseInt(timeSplit[0]) * 60 * 60;
            } else if (i == 1) {
                orderTimeInSecond += Integer.parseInt(timeSplit[1]) * 60;
            } else if (i == 2) {
                orderTimeInSecond += Integer.parseInt(timeSplit[1]);
            }
        }

        *//*int diff = currentTimeInSecond - orderTimeInSecond;
        if( diff > totalDayTimeInSecond){
            holder.llCancel.setVisibility(View.GONE);
        }else{
            //holder.llCancel.setVisibility(View.VISIBLE);
        }*//*

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String date = dateTimeSplit[0];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());


        Date startDate = null,endDate = null;
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = outputFormat.parse(currentDateandTime);
            Log.d("startDate", String.valueOf(startDate));
            //endDate = outputFormat.parse(date);
            endDate = outputFormat.parse(modelList.get(position).getDate_time());
            Log.d("endDate", String.valueOf(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long diffInMs = startDate.getTime() - endDate.getTime();
        Log.d("diffInMs", String.valueOf(diffInMs));
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
        Log.d("diffInSec", String.valueOf(diffInSec));

        if(diffInMs > 86400000){
            //Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
            holder.img.setVisibility(View.GONE);
            holder.tvCancelProduct.setVisibility(View.GONE);
            //holder.tvCancelDateTime.setVisibility(View.GONE);
            holder.llCancel.setVisibility(View.GONE);
        }else{
            //Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
            holder.img.setVisibility(View.VISIBLE);
            holder.tvCancelProduct.setVisibility(View.VISIBLE);
            //holder.tvCancelDateTime.setVisibility(View.VISIBLE);
            //holder.tvCancelDateTime.setText(modelList.get(position).getCancelDateTime());
            //holder.llCancel.setVisibility(View.VISIBLE);
        }
*/

        Glide.with(context).load(modelList.get(position).getProdImage()).placeholder(R.drawable.loading).into(holder.ivPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bookBundle = new Bundle();
                bookBundle.putString("id", modelList.get(position).getProdId());
                Navigation.findNavController(view).navigate(R.id.nav_order_list_to_product_details, bookBundle);
            }
        });

        String status = modelList.get(position).getPrdStatus();
        if (status.equalsIgnoreCase("cancel")) {
            holder.tvCancel.setVisibility(View.VISIBLE);
            holder.tvCancelDateTime.setVisibility(View.VISIBLE);
            holder.llCancel.setVisibility(View.GONE);
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_z_cross));//ic_z_cross
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_z_cross));//ic_zchecked3
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_z_cross));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_z_cross));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (status.equalsIgnoreCase("placed")) {
            holder.tvCancel.setVisibility(View.GONE);
            holder.tvCancelDateTime.setVisibility(View.GONE);
                ////holder.llCancel.setVisibility(View.VISIBLE);
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked2));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked2));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked2));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        } else if (status.equalsIgnoreCase("dispatch")) {
            holder.tvCancel.setVisibility(View.GONE);
            holder.tvCancelDateTime.setVisibility(View.GONE);
            //holder.llCancel.setVisibility(View.VISIBLE);
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked2));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        } else if (status.equalsIgnoreCase("delivered")) {
            holder.tvCancel.setVisibility(View.GONE);
            holder.tvCancelDateTime.setVisibility(View.GONE);
            //holder.llCancel.setVisibility(View.VISIBLE);
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.green5));
        } else if (status.equalsIgnoreCase("proccess")) {
            holder.tvCancel.setVisibility(View.GONE);
            holder.tvCancelDateTime.setVisibility(View.GONE);
            holder.ivO_P.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_OP.setBackground(context.getDrawable(R.drawable.ic_zchecked));
            holder.ivO_D.setBackground(context.getDrawable(R.drawable.ic_zchecked2));
            holder.ivO_O.setBackground(context.getDrawable(R.drawable.ic_zchecked2));

            holder.vO_P.setBackgroundColor(context.getResources().getColor(R.color.green5));
            holder.vO_OP.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.vO_D.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPName, tvPDetails, tvQty, tvAmount, tvCancel,tvCancelProduct,tvCancelDateTime;
        ImageView ivPic, ivO_P, ivO_OP, ivO_D, ivO_O,img;
        LinearLayout llCancel;
        View vO_P, vO_OP, vO_D;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPName = itemView.findViewById(R.id.tvPName);
            //tvCancelProduct = itemView.findViewById(R.id.tvCancelProduct);
            tvCancelDateTime = itemView.findViewById(R.id.tvCancelDateTime);
            //img = itemView.findViewById(R.id.img);
            tvPDetails = itemView.findViewById(R.id.tvPDetails);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvCancel = itemView.findViewById(R.id.tvCancel);
            //llCancel = itemView.findViewById(R.id.llCancels);

            ivO_P = itemView.findViewById(R.id.ivO_P);
            ivO_OP = itemView.findViewById(R.id.ivO_OP);
            ivO_D = itemView.findViewById(R.id.ivO_D);
            ivO_O = itemView.findViewById(R.id.ivO_O);
            vO_P = itemView.findViewById(R.id.vO_P);
            vO_OP = itemView.findViewById(R.id.vO_OP);
            vO_D = itemView.findViewById(R.id.vO_D);


        }
    }

    public void setListner2(OrderDetailsFragment.onDataRecived4 onDataRecived4) {
        this.onDataRecived4 = onDataRecived4;
    }
}
