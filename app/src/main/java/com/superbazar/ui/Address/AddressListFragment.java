package com.superbazar.ui.Address;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentAddressListBinding;
import com.superbazar.ui.Address.Adapter.AddressAdapter;
import com.superbazar.ui.Address.Model.AddressModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressListFragment extends Fragment implements View.OnClickListener {
    FragmentAddressListBinding binding;
    ProgressDialog dialog;
    List<AddressModel> modelList;
    public static String address_id = "";

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        try {
            nav.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddressListBinding.inflate(inflater, container, false);
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        try {
            nav.setVisibility(View.GONE);
        } catch (Exception e) {
        }

        dialog = new ProgressDialog(getActivity());
        btnClick();
        loadCartCount();
        loadWishlistCount();
        setLayout();
        dialog.setMessage("Loading...");
        dialog.show();
        loadAddress();
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

    private void loadAddress() {
        binding.tvTotal.setText("₹ "+getArguments().getString("total"));

        modelList = new ArrayList<>();

        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ADDRESS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ADDRESS_RES",response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        binding.llAddressFound.setVisibility(View.VISIBLE);
                        binding.noAddress.setVisibility(View.GONE);
                        JSONArray array = jsonObject.getJSONArray("results");
                        for(int i=0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            String AddressId = object.getString("AddressId");
                            String Name = object.getString("Name");
                            String Phone = object.getString("Phone");
                            String PinCode = object.getString("PinCode");
                            String Address = object.getString("Address");
                            String Landmark = object.getString("Landmark");

                            modelList.add(new AddressModel(AddressId,Name,Phone,Address,Landmark,PinCode));
                        }

                        AddressAdapter adapter = new AddressAdapter(modelList,getActivity());
                        binding.rvAddress.setAdapter(adapter);

                        adapter.setListner(new onDataReceived() {
                            @Override
                            public void onCallBack(String pos) {
                                loadAddress();
                            }
                        });

                    }else{
                        binding.noAddress.setVisibility(View.VISIBLE);
                        binding.llAddressFound.setVisibility(View.GONE);
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void setLayout() {
        binding.rvAddress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void btnClick() {
        binding.fbNewAddress.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.flWishlist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;
            case R.id.llCart:
                Bundle bundle2 = new Bundle();
                bundle2.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_address_list_to_cart, bundle2);
                break;
            case R.id.flWishlist:
                Bundle bundle3 = new Bundle();
                bundle3.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_address_list_to_wishlist, bundle3);
                break;
            case R.id.fbNewAddress:
                Bundle bundle = new Bundle();
                bundle.putString("total",getArguments().getString("total"));
                bundle.putString("totalTax", getArguments().getString("totalTax"));
                Navigation.findNavController(v).navigate(R.id.navigation_address_list_to_address,bundle);
                break;
            case R.id.tvContinue:
                Log.d("address_id",address_id);
                if(address_id.equals("")|| address_id.isEmpty() ){
                    Toast.makeText(getActivity(), "Select an address", Toast.LENGTH_SHORT).show();
                }else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("total", getArguments().getString("total"));
                    bundle1.putString("totalTax", getArguments().getString("totalTax"));
                    bundle1.putString("addressId", address_id);
                    Navigation.findNavController(v).navigate(R.id.navigation_address_list_to_place_order, bundle1);
                    break;
                }
        }
    }

    public interface onDataReceived{
        void onCallBack(String pos);
    }
}