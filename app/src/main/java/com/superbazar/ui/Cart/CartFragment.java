package com.superbazar.ui.Cart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.superbazar.Activity.LoginActivity;
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentCartBinding;
import com.superbazar.ui.Cart.Adapter.CartAdapter;
import com.superbazar.ui.Cart.Model.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment implements View.OnClickListener{
    FragmentCartBinding binding;
    ProgressDialog progressDialog;
    List<CartModel> modelList;

    @Override
    public void onResume() {
        super.onResume();
        try {
            BottomNavigationView btnNav = getActivity().findViewById(R.id.bottom_nav);
            btnNav.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater,container,false);
        try {
            BottomNavigationView btnNav = getActivity().findViewById(R.id.bottom_nav);
            btnNav.setVisibility(View.GONE);
        } catch (Exception e) {
        }

        progressDialog = new ProgressDialog(getActivity());
        setLayout();
        btnClick();
        if(YoDB.getPref().read(Constants.ID,"").isEmpty()){
            binding.llBeforeLogin.setVisibility(View.VISIBLE);
            binding.llAfterLogin.setVisibility(View.GONE);
        }else{
            binding.llAfterLogin.setVisibility(View.VISIBLE);
            binding.llBeforeLogin.setVisibility(View.GONE);
            loadCart();
        }
        return binding.getRoot();
    }

    private void btnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.llWisth.setOnClickListener(this);
        binding.llLogin.setOnClickListener(this);
    }

    private void setLayout() {
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
    }

    private void loadCart() {
        modelList = new ArrayList<>();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        JSONArray array = jsonObject.getJSONArray("results");
                        for(int i=0;i< array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            String cartId = object.getString("CartId");
                            String prodId = object.getString("ProductId");
                            String ProductName = object.getString("ProductName");
                            String ProductShortDescription = "";//object.getString("ProductShortDescription");
                            String ProductMarketPrice = object.getString("ProductMarketPrice");
                            String ProductSellingPrice = object.getString("ProductSellingPrice");
                            String Quantity = object.getString("Quantity");

                            JSONArray jsonArray = object.getJSONArray("ProductFiles");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String image = "https://smlawb.org/superbazaar/web/uploads/product/"+obj.getString("ProductFileName");

                            modelList.add(new CartModel(cartId,prodId,ProductName,ProductShortDescription,Quantity,image,ProductMarketPrice,ProductSellingPrice));
                        }
                        CartAdapter adapter = new CartAdapter(modelList,getActivity());
                        binding.rvCart.setAdapter(adapter);
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
                body.put("id", YoDB.getPref().read(Constants.ID,""));
                body.put("type","cart");
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;

            case R.id.llLogin:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                getActivity().finish();
                break;
        }
    }

    public interface onDataRecived {
        void onCallBack(String pos);
    }
}