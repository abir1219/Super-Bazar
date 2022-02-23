package com.superbazar.ui.Cart.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.ui.Cart.CartFragment;
import com.superbazar.ui.Cart.Model.CartModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");

        holder.tvPName.setText(modelList.get(position).getProductName());
        holder.tvPDesc.setText(modelList.get(position).getDesc());
        holder.tvAmount.setText("₹ "+modelList.get(position).getOffPrice());
        holder.tvS_Amount.setText("₹ "+modelList.get(position).getActualPrice());
        holder.tvCount.setText(modelList.get(position).getQuantity());

        Glide.with(context).load(modelList.get(position).getProdImage()).into(holder.ivPPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id",modelList.get(position).getProductId());
                Navigation.findNavController(v).navigate(R.id.nav_cart_to_product_details,bundle);
            }
        });


        holder.tvPRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                StringRequest sr = new StringRequest(Request.Method.POST, Urls.REMOVE_CART, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getString("status").equals("1")){
                                Toast.makeText(context, "Remove Successfully", Toast.LENGTH_SHORT).show();
                                onDataRecived.onCallBack(String.valueOf(position));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> body = new HashMap<>();
                        body.put("id",modelList.get(position).getCartId());
                        return body;
                    }
                };
                Volley.newRequestQueue(context).add(sr);
            }
        });
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
