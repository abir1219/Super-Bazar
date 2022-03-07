package com.superbazar.ui.Category;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.superbazar.databinding.FragmentCategoryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CategoryFragment extends Fragment implements View.OnClickListener {
    FragmentCategoryBinding binding;

    @Override
    public void onResume() {
        super.onResume();
        try {
            BottomNavigationView navView = getActivity().findViewById(R.id.bottom_nav);
            navView.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        try {
            BottomNavigationView navView = getActivity().findViewById(R.id.bottom_nav);
            navView.setVisibility(View.GONE);
        } catch (Exception e) {
        }
        btnClick();
        loadCartCount();
        loadWishlistCount();
        binding.tvName.setText(YoDB.getPref().read(Constants.NAME, ""));
        binding.tvPhone.setText(YoDB.getPref().read(Constants.PHONE, ""));
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
                body.put("type", "cart");
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
        binding.llEditProfile.setOnClickListener(this);
        binding.llLogout.setOnClickListener(this);
        binding.llLogin.setOnClickListener(this);
        binding.llAddress.setOnClickListener(this);
        binding.llProifle.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.llWisth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMenu:
                //getActivity().onBackPressed();
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.llCart:
                Navigation.findNavController(v).navigate(R.id.nav_account_to_cart);
                break;
            case R.id.llWisth:
                Navigation.findNavController(v).navigate(R.id.nav_account_to_wishlist);
                break;
            case R.id.llAddress:
                Bundle bundle = new Bundle();
                bundle.putString("from", "account");
                Navigation.findNavController(v).navigate(R.id.nav_account_to_address_list, bundle);
                break;
            case R.id.llLogin:
                Intent mIntent = new Intent(getActivity(), LoginActivity.class);
                mIntent.putExtra("from", "account");
                startActivity(mIntent);
                getActivity().overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                getActivity().finish();
                break;
            case R.id.llEditProfile:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_account_to_profile);
                break;
            case R.id.llProifle:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_account_to_profile);
                break;
        }
    }
}