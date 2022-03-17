package com.superbazar.ui.OrderList;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentOrderListBinding;
import com.superbazar.ui.OrderList.Adapter.OrderListAdapter;
import com.superbazar.ui.OrderList.Model.OrderListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListFragment extends Fragment implements View.OnClickListener{
    FragmentOrderListBinding binding;
    List<OrderListModel> modelList;

    @Override
    public void onResume() {
        super.onResume();
        try {
            BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
            navBar.setVisibility(View.GONE);
        } catch (NullPointerException e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderListBinding.inflate(inflater,container,false);
        try {
            BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
            navBar.setVisibility(View.GONE);
        } catch (NullPointerException e) {
        }
        setLayout();
        btnClick();
        loadCartCount();
        loadWishlistCount();
        loadOrderList();
        return binding.getRoot();
    }

    private void setLayout(){
        binding.rvOrder.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
    }

    private void loadCartCount() {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CART_COUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        if (Integer.parseInt(jsonObject.getString("count")) > 0) {
                            binding.tvcartBadge.setVisibility(View.VISIBLE);
                            binding.tvcartBadge.setText(jsonObject.getString("count"));
                        } else {
                            binding.tvcartBadge.setVisibility(View.GONE);
                        }
                    } else {
                        binding.tvcartBadge.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("id", YoDB.getPref().read(Constants.ID, ""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void loadWishlistCount() {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.WISHLIST_COUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        if (Integer.parseInt(jsonObject.getString("count")) > 0) {
                            binding.tvwishBadge.setVisibility(View.VISIBLE);
                            binding.tvwishBadge.setText(jsonObject.getString("count"));
                        } else {
                            binding.tvwishBadge.setVisibility(View.GONE);
                        }
                    } else {
                        binding.tvwishBadge.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("id", YoDB.getPref().read(Constants.ID, ""));
                body.put("type", "wishlist");
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void loadOrderList() {
        modelList = new ArrayList<>();
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ORDER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("OrderList_RES",response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        binding.nsv.setVisibility(View.VISIBLE);
                        binding.llDataNotFound.setVisibility(View.GONE);
                        JSONArray array = jsonObject.getJSONArray("results");
                        for(int i=0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            String orderId = object.getString("OrderId");
                            String OrderNumber = object.getString("OrderNumber");
                            String OrderDate = object.getString("OrderDate");
                            String OrderStatus = object.getString("OrderStatus");
                            String TotalAmmount = object.getString("TotalAmmount");

                            modelList.add(new OrderListModel(orderId,OrderNumber,OrderDate,TotalAmmount,OrderStatus));
                        }
                        OrderListAdapter adapter = new OrderListAdapter(modelList,getActivity());
                        binding.rvOrder.setAdapter(adapter);

                    }else{
                        binding.nsv.setVisibility(View.GONE);
                        binding.llDataNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void btnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.flWishlist.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.llSearch:
                ((MainActivity) getActivity()).searchProduct(R.id.nav_order_list_to_search);
                break;
            case R.id.llCart:
                Bundle bundle = new Bundle();
                bundle.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_order_list_to_cart, bundle);
                break;
            case R.id.flWishlist:
                Bundle bundle1 = new Bundle();
                bundle1.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_order_list_to_wishlist, bundle1);
                break;
        }
    }
}