package com.superbazar.ui.OrderDetails;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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
import com.superbazar.databinding.FragmentOrderDetailsBinding;
import com.superbazar.ui.OrderDetails.Adapter.OrderDetailsAdapter;
import com.superbazar.ui.OrderDetails.Model.OrderDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsFragment extends Fragment implements View.OnClickListener{
    FragmentOrderDetailsBinding binding;
    List<OrderDetailModel> modelList;

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
        binding = FragmentOrderDetailsBinding.inflate(inflater,container,false);

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

    private void loadOrderList(){
        modelList = new ArrayList<>();
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ORDER_DETAILS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("ORDER_DETAILS",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        binding.tvId.setText("Order Id: "+getArguments().getString("order_No"));
                        binding.tvDate.setText(getArguments().getString("dateTime"));
                        binding.tvPrice.setText("Total Amount: ₹ "+getArguments().getString("amount"));

                        String order_status = jsonObject.getString("orderStatus");
                        JSONArray array = jsonObject.getJSONArray("results");
                        for(int i=0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            String prodName = object.getString("ProductName");
                            String quantity = object.getString("Quantity");
                            String unit_price_with_tax = object.getString("SellingPrice");

                            //String image_1 = "https://smlawb.org/superbazaar/web/uploads/product/" + Html.fromHtml(object.getString("image_1"));
                            String prodId = object.getString("product_id");
                            String order_id = object.getString("OrderId");
                            String date_time = object.getString("order_date");
                            String product_description = object.getString("ProductShortDescription");

                            JSONArray imageArray = object.getJSONArray("ProductFiles");
                            JSONObject obj = imageArray.getJSONObject(0);
                            String image = "https://smlawb.org/superbazaar/web/uploads/product/" + obj.getString("ProductFileName");

                            modelList.add(new OrderDetailModel(prodId,order_id,prodName,product_description,quantity,unit_price_with_tax,image,order_status,date_time));
                        }

                        OrderDetailsAdapter adapter = new OrderDetailsAdapter(modelList,getActivity());
                        binding.rvProducts.setAdapter(adapter);
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
                body.put("order_id",getArguments().getString("order_id"));
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
                ((MainActivity) getActivity()).searchProduct(R.id.nav_order_details_to_search);
                break;
            case R.id.llCart:
                Bundle bundle = new Bundle();
                bundle.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_order_details_to_cart, bundle);
                break;
            case R.id.flWishlist:
                Bundle bundle1 = new Bundle();
                bundle1.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_order_details_to_wishlist, bundle1);
                break;
        }
    }

    private void setLayout(){
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
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

    public interface onDataRecived4 {
        void onCallBack4(String pos);
    }
}