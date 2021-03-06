package com.superbazar.ui.Cart;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

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

public class CartFragment extends Fragment implements View.OnClickListener {
    FragmentCartBinding binding;
    ProgressDialog progressDialog;
    List<CartModel> modelList;
    String totalCost = "";
    String totalCostExcludingTax = "";
    String deliveryCharge = "";
    String totalDiscount = "";
    Double totalTax = 0.0;
    boolean pincodeChecked = false;

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
        binding = FragmentCartBinding.inflate(inflater, container, false);
        try {
            BottomNavigationView btnNav = getActivity().findViewById(R.id.bottom_nav);
            btnNav.setVisibility(View.GONE);
        } catch (Exception e) {
        }

        progressDialog = new ProgressDialog(getActivity());
        setLayout();
        btnClick();
        loadCartCount();
        loadWishlistCount();

        if (YoDB.getPref().read(Constants.ID, "").isEmpty()) {
            binding.llBeforeLogin.setVisibility(View.VISIBLE);
            binding.llAfterLogin.setVisibility(View.GONE);
        } else {
            binding.llAfterLogin.setVisibility(View.VISIBLE);
            binding.llBeforeLogin.setVisibility(View.GONE);

            progressDialog.setMessage("Loading...");
            progressDialog.show();
            loadCart();
        }
        return binding.getRoot();
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


    private void btnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.llWisth.setOnClickListener(this);
        binding.llLogin.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.llWisth.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);
        binding.llContinueShopping.setOnClickListener(this);
        binding.btnCheck.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
    }

    private void setLayout() {
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
    }

    private void loadCart() {
        modelList = new ArrayList<>();
        modelList.clear();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CART_RES",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        binding.tvTotal.setText("??? " + jsonObject.getString("alltotal"));
                        totalCost = jsonObject.getString("alltotal");
                        totalDiscount = jsonObject.getString("totaldiscount");
                        deliveryCharge = jsonObject.getString("deliveryCharge");
                        totalCostExcludingTax = jsonObject.getString("totalexclusivetaxanddiscount");
                        //Toast.makeText(getActivity(), "have", Toast.LENGTH_SHORT).show();
                        binding.llDataNotFound.setVisibility(View.GONE);
                        binding.llAfterLogin.setVisibility(View.VISIBLE);
                        JSONArray array = jsonObject.getJSONArray("results");
                        Log.d("ARRAY_RES", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String cartId = object.getString("CartId");
                            String prodId = object.getString("ProductId");
                            String ProductName = object.getString("ProductName");
                            String ProductShortDescription = object.getString("CategoryName");
                            String Quantity = object.getString("Quantity");
                            String ProductRating = object.getString("ProductRating");

                            totalTax += Double.parseDouble(object.getString("TotalTax"));

                            Double marketPrice = Double.parseDouble(object.getString("ProductMarketPrice")) * Double.parseDouble(Quantity);
                            Double sellingPrice = Double.parseDouble(object.getString("NetAmount")) * Double.parseDouble(Quantity);
                            Double total = Double.parseDouble(object.getString("NetAmount")) * Double.parseDouble(Quantity);

                            /*String ProductMarketPrice = String.format("%.2f",marketPrice);
                            String ProductSellingPrice = String.format("%.2f",sellingPrice);*/

                            String ProductMarketPrice = String.format("%.2f", Double.parseDouble(object.getString("ProductMarketPrice")));
                            String ProductSellingPrice = String.format("%.2f", Double.parseDouble(object.getString("NetAmount")));
                            String totalPrice = String.format("%.2f", total);

                            JSONArray jsonArray = object.getJSONArray("ProductFiles");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String image = "https://smlawb.org/superbazaar/web/uploads/product/" + obj.getString("ProductFileName");
                            modelList.add(new CartModel(cartId, prodId, ProductName, ProductShortDescription, Quantity, image, ProductMarketPrice, ProductSellingPrice, totalPrice,ProductRating));
                        }
                        CartAdapter adapter = new CartAdapter(modelList, getActivity());
                        binding.rvCart.setAdapter(adapter);

                        adapter.setListner(new onDataRecived() {
                            @Override
                            public void onCallBack(String pos) {
                                loadCartCount();
                                loadWishlistCount();
                                loadCart();
                            }
                        });
                    } else {
                        //Toast.makeText(getActivity(), "don't have", Toast.LENGTH_SHORT).show();
                        binding.llDataNotFound.setVisibility(View.VISIBLE);
                        binding.llAfterLogin.setVisibility(View.GONE);
                        CartAdapter adapter = new CartAdapter(modelList, getActivity());
                        binding.rvCart.setAdapter(adapter);
                        //binding.llDataNotFound.setVisibility(View.VISIBLE);
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("RES_ID", YoDB.getPref().read(Constants.ID, ""));
                Map<String, String> body = new HashMap<>();
                body.put("id", YoDB.getPref().read(Constants.ID, ""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.llSearch:
                ((MainActivity) getActivity()).searchProduct(R.id.nav_cart_to_search);
                break;
            case R.id.llLogin:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                getActivity().finish();
                break;
            case R.id.llContinueShopping:
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                getActivity().finish();
                break;
            case R.id.llWisth:
                Navigation.findNavController(v).navigate(R.id.nav_cart_to_wishlist);
                break;
            case R.id.tvContinue:
                if(pincodeChecked == true){
                    Bundle bundle = new Bundle();
                    bundle.putString("total", totalCost);
                    bundle.putString("deliveryCharge", deliveryCharge);
                    bundle.putString("totalCostExcludingTax", totalCostExcludingTax);
                    bundle.putString("totalDiscount", totalDiscount);
                    bundle.putString("totalTax", String.format("%.2f",totalTax));
                    Navigation.findNavController(v).navigate(R.id.nav_cart_to_address_list, bundle);
                }else{
                    Toast.makeText(getActivity(), "Please check pincode first", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCheck:
                pincodeCheck();
                break;
        }
    }

    private void pincodeCheck() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.FETCH_PINCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        pincodeChecked = true;
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }else{
                        pincodeChecked = false;
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                body.put("pin_code",binding.etPincode.getText().toString());
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    public interface onDataRecived {
        void onCallBack(String pos);
    }
}