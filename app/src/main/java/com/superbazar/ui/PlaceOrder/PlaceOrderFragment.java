package com.superbazar.ui.PlaceOrder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.superbazar.databinding.FragmentPlaceOrderBinding;
import com.superbazar.databinding.OrderSuccessDialogBinding;
import com.superbazar.ui.RazorPay.RazorPayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlaceOrderFragment extends Fragment implements View.OnClickListener {
    FragmentPlaceOrderBinding binding;
    RadioButton radioButton;
    private String payment_mode = "";

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
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false);
        try {
            BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
            navBar.setVisibility(View.GONE);
        } catch (NullPointerException e) {
        }

        btnClick();
        loadCartCount();
        loadWishlistCount();

        binding.tvTotalPrice.setText(getArguments().getString("total"));
        binding.tvTax.setText("00.00");
        binding.tvAllTotal.setText(getArguments().getString("total"));
        binding.txt.setText("₹ " + getArguments().getString("total"));

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton rb = (RadioButton) binding.radioGroup.findViewById(checkedId);
                int selectedId = binding.radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) binding.radioGroup.findViewById(selectedId);
                //Toast.makeText(getActivity(), "rb: "+radioButton, Toast.LENGTH_SHORT).show();
                if (selectedId == -1) {
                    Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getActivity(),radioButton.getText(), Toast.LENGTH_SHORT).show();
                    if (radioButton.getText().toString().equalsIgnoreCase("CASH ON DELIVERY (COD)")) {
                        binding.btContinue.setText("PLACE ORDER");
                        payment_mode = "cod";
                    } else if (radioButton.getText().toString().equalsIgnoreCase("ONLINE PAYMENT")) {
                        binding.btContinue.setText("PAY NOW");
                        //binding.btContinue.setText("PLACE ORDER");
                        payment_mode = "online";
                    }
                }
            }
        });

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
        binding.btContinue.setOnClickListener(this);
        binding.llMenu.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.flWishlist.setOnClickListener(this);
        binding.cod.setOnClickListener(this);
        binding.online.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;
            case R.id.btContinue:
                if (!payment_mode.equals("")) {
                    //Toast.makeText(getActivity(), payment_mode, Toast.LENGTH_SHORT).show();
                    if (payment_mode.equalsIgnoreCase("cod")) {
                        placeOrder();
                    } else if (payment_mode.equalsIgnoreCase("online")) {
                        //Toast.makeText(getActivity(), "Working on it", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), RazorPayActivity.class);
                        intent.putExtra("addressId",getArguments().getString("addressId"));
                        intent.putExtra("total",getArguments().getString("total"));
                        intent.putExtra("payment_type",payment_mode);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                    }
                }else{
                    Toast.makeText(getActivity(), "Select a payment method", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cod:
                payment_mode = "cod";
                binding.iv1.setVisibility(View.GONE);
                binding.iv01.setVisibility(View.VISIBLE);
                binding.txt1.setTextColor(getResources().getColor(R.color.red));
                binding.btContinue.setText("PLACE ORDER");

                binding.iv2.setVisibility(View.VISIBLE);
                binding.iv02.setVisibility(View.GONE);
                binding.txt2.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.online:
                payment_mode = "online";
                binding.iv2.setVisibility(View.GONE);
                binding.iv02.setVisibility(View.VISIBLE);
                binding.txt2.setTextColor(getResources().getColor(R.color.red));
                binding.btContinue.setText("PAY NOW");

                binding.iv1.setVisibility(View.VISIBLE);
                binding.iv01.setVisibility(View.GONE);
                binding.txt1.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.llCart:
                Bundle bundle = new Bundle();
                bundle.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_place_order_to_cart, bundle);
                break;
            case R.id.flWishlist:
                Bundle bundle1 = new Bundle();
                bundle1.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_place_order_to_wishlist, bundle1);
                break;
        }
    }

    private void placeOrder() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.PLACE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        showSuccessDialog();
                    } else {
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
                Toast.makeText(getActivity(), "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                body.put("payment_type",payment_mode);
                body.put("address_id",getArguments().getString("addressId"));
                body.put("total",getArguments().getString("total"));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void showSuccessDialog() {
        final Dialog dialog = new Dialog(getActivity());
        OrderSuccessDialogBinding binding = OrderSuccessDialogBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //binding.tvDescription.setText("Your order with order id " + order_id + " has been successfully placed");
        dialog.show();
        dialog.setCancelable(false);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Go to order listing page from drawer menu", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                //Navigation.findNavController(view).navigate(R.id.nav_place_order_to_order_list);
                dialog.dismiss();
            }
        });
    }
}