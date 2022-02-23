package com.superbazar.ui.Wishlist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.superbazar.Helper.YoDB;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.ui.Cart.CartFragment;
import com.superbazar.ui.Wishlist.Model.WishlistModel;
import com.superbazar.ui.Wishlist.WishlistFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.tvOffPrice.setText("₹ "+modelList.get(position).getOffPrice());

        Glide.with(context).load(modelList.get(position).getProdImage()).into(holder.ivPPic);

        holder.rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(position);
            }
        });

        holder.rvRemoveWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeWishlist(position);
            }
        });
    }

    private void addToCart(int position) {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("1")){
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                        removeWishlist(position);
                        onDataRecived.onCallBack(String.valueOf(position));
                    }else{
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("id", YoDB.getPref().read(Constants.ID,""));
                body.put("ProductId", modelList.get(position).getProductId());
                body.put("Quantity", "1");
                return body;
            }
        };
        Volley.newRequestQueue(context).add(sr);
    }

    public void removeWishlist(int position){
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.REMOVE_WISHLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("1")){
                        onDataRecived.onCallBack(String.valueOf(position));
                        Toast.makeText(context, "Wishlist Removed Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("id",modelList.get(position).getCartId());
                return body;
            }
        };
        Volley.newRequestQueue(context).add(sr);
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
