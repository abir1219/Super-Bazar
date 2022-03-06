package com.superbazar.ui.Address.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.ui.Address.AddressListFragment;
import com.superbazar.ui.Address.Model.AddressModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    List<AddressModel> modelList;
    Context context;
    public static int selected_postion = -1;
    public AddressListFragment.onDataReceived onDataReceived;

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

        int gray = Color.parseColor("#11808080");
        int whiteColor = Color.parseColor("#FFFFFF");

        //holder.cvLayout.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        //holder.itemView.setBackground(AddressAdapter.selected_postion == position ? context.getResources().getColor(R.color.white) : ColorStateList.valueOf(gray));
        //holder.itemView.setBackgroundColor(selected_postion == position ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.gray1));

        if(selected_postion == position){
            AddressListFragment.address_id = modelList.get(position).getId();
            holder.ivSelect.setVisibility(View.GONE);
            holder.ivSelect1.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else{
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.gray1));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selected_postion);
                //selected_postion = holder.getLayoutPosition();
                selected_postion = holder.getAdapterPosition();
                notifyItemChanged(selected_postion);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type","edit");
                bundle.putString("addressId",modelList.get(position).getId());
                Navigation.findNavController(v).navigate(R.id.navigation_address_list_to_address, bundle);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("Do you want to delete this address?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAddress(position);
                            }
                        })
                .setNegativeButton("Cancel",null)
                .show();
            }
        });

    }

    private void deleteAddress(int position) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ADDRESS_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("1")){
                        onDataReceived.onCallBack(String.valueOf(position));
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put(" address_id",modelList.get(position).getId());
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
        TextView tvName,tvLandmark,tvAddPin,tvPhone;
        ImageView ivSelect,ivSelect1,ivEdit,ivDelete;
        CardView cvLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLandmark = itemView.findViewById(R.id.tvLandmark);
            tvAddPin = itemView.findViewById(R.id.tvAddPin);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            ivSelect1 = itemView.findViewById(R.id.ivSelect1);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            cvLayout = itemView.findViewById(R.id.cvLayout);
        }
    }

    public void setListner(AddressListFragment.onDataReceived onDataReceived){
        this.onDataReceived = onDataReceived;
    }
}
