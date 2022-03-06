package com.superbazar.ui.Address;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.superbazar.databinding.FragmentCreateAddressBinding;
import com.superbazar.ui.Address.Adapter.AddressAdapter;
import com.superbazar.ui.Address.Model.AddressModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateAddressFragment extends Fragment implements View.OnClickListener {
    FragmentCreateAddressBinding binding;
    ProgressDialog dialog;

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
        binding = FragmentCreateAddressBinding.inflate(inflater, container, false);
        dialog = new ProgressDialog(getActivity());
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        try {
            nav.setVisibility(View.GONE);
        } catch (Exception e) {
        }

        btnClick();
        loadCartCount();
        loadWishlistCount();
        if (getArguments().containsKey("type") && getArguments().getString("type").equals("edit")) {
            loadAddress();
        }
        return binding.getRoot();
    }

    private void loadAddress() {
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ADDRESS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ADDRESS_RES", response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray array = jsonObject.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String AddressId = object.getString("AddressId");
                            String Name = object.getString("Name");
                            String Phone = object.getString("Phone");
                            String PinCode = object.getString("PinCode");
                            String Address = object.getString("Address");
                            String Landmark = object.getString("Landmark");

                            binding.etName.setText(Name);
                            binding.etPhone.setText(Phone);
                            binding.etAddress.setText(Address);
                            binding.etLandmark.setText(Landmark);
                            binding.etPincode.setText(PinCode);
                        }

                    } else {

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
                body.put("user_id", getArguments().getString("addressId"));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
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
        binding.btSignUp.setOnClickListener(this);
        binding.llMenu.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.flWishlist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.btSignUp:
                checkData(v);
                break;
            case R.id.llCart:
                Bundle bundle2 = new Bundle();
                bundle2.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_address_to_cart, bundle2);
                break;
            case R.id.flWishlist:
                Bundle bundle3 = new Bundle();
                bundle3.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_address_to_wishlist, bundle3);
                break;
        }
    }

    private void checkData(View v) {
        if (binding.etName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter the name", Toast.LENGTH_SHORT).show();
            binding.etName.requestFocus();
        } else if (binding.etPhone.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            binding.etPhone.requestFocus();
        } else if (binding.etPhone.getText().length() != 10) {
            Toast.makeText(getActivity(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            binding.etPhone.requestFocus();
        } else if (binding.etAddress.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter address", Toast.LENGTH_SHORT).show();
            binding.etAddress.requestFocus();
        } else if (binding.etLandmark.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter the nearest landmark", Toast.LENGTH_SHORT).show();
            binding.etLandmark.requestFocus();
        } else if (binding.etPincode.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter pincode", Toast.LENGTH_SHORT).show();
            binding.etPincode.requestFocus();
        } else {
            saveAddress(v);
        }
    }

    private void saveAddress(View v) {
        dialog.setMessage("Saving...");
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("1")) {
                        Toast.makeText(getActivity(), "Address saved successful", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        if (getArguments().containsKey("from") && getArguments().getString("from").equals("account")) {
                            //Toast.makeText(getActivity(), "account", Toast.LENGTH_SHORT).show();
                            bundle.putString("from","account");
                        }else{
                            //Toast.makeText(getActivity(), "normal", Toast.LENGTH_SHORT).show();
                            bundle.putString("total", getArguments().getString("total"));
                            bundle.putString("totalTax", getArguments().getString("totalTax"));
                        }
                        Navigation.findNavController(v).navigate(R.id.navigation_address_to_address, bundle);
                    } else {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                if (getArguments().containsKey("type") && getArguments().getString("type").equals("edit")) {
                    body.put("address_id", getArguments().getString("addressId"));
                }
                body.put("name", binding.etName.getText().toString());
                body.put("phone", binding.etPhone.getText().toString());
                body.put("pincode", binding.etPincode.getText().toString());
                body.put("address", binding.etAddress.getText().toString());
                body.put("landmark", binding.etLandmark.getText().toString());
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }
}