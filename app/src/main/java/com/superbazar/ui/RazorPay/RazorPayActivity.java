package com.superbazar.ui.RazorPay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.ActivityRazorPayBinding;
import com.superbazar.databinding.OrderSuccessDialogBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RazorPayActivity extends AppCompatActivity implements PaymentResultListener {
    ActivityRazorPayBinding binding;
    private static final String TAG = RazorPayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRazorPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Checkout.preload(getApplicationContext());

        startPayment();
    }


    private void startPayment() {
        String ttl = getIntent().getStringExtra("total").replace(",","");
        int amount = Math.round(Float.parseFloat(ttl)*100);

        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_test_TLpK8uOuNBaDSZ");
        //checkout.setImage(R.drawable.splash);
        checkout.setImage(R.drawable.super_bazar);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name","Super Bazar");
            jsonObject.put("description","Payment");
            jsonObject.put("theme.color","#8CC809");
            jsonObject.put("currency","INR");
            jsonObject.put("amount",amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", YoDB.getPref().read(Constants.EMAIL,""));
            preFill.put("contact", YoDB.getPref().read(Constants.PHONE,""));

            jsonObject.put("prefill", preFill);

            /*jsonObject.put("prefill.contact", YoDB.getPref().read(Constants.PHONE,""));
            jsonObject.put("prefill.email",YoDB.getPref().read(Constants.EMAIL,""));*/

            Log.e("json", String.valueOf(jsonObject));

            checkout.open(RazorPayActivity.this,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            placeOrder();
            //Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    private void placeOrder() {
        ProgressDialog dialog = new ProgressDialog(RazorPayActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.PLACE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ONLINE_RES",response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        showSuccessDialog();
                    } else {
                        Toast.makeText(RazorPayActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(RazorPayActivity.this, "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("user_id",YoDB.getPref().read(Constants.ID,""));
                Log.d("payment_type",getIntent().getStringExtra("payment_type"));
                Log.d("address_id",getIntent().getStringExtra("addressId"));
                Log.d("total",getIntent().getStringExtra("total").replace(",",""));

                Map<String, String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                body.put("payment_type",getIntent().getStringExtra("payment_type"));
                body.put("address_id",getIntent().getStringExtra("addressId"));
                body.put("total",getIntent().getStringExtra("total").replace(",",""));
                return body;
            }
        };
        Volley.newRequestQueue(RazorPayActivity.this).add(sr);
    }

    private void showSuccessDialog() {
        final Dialog dialog = new Dialog(RazorPayActivity.this);
        OrderSuccessDialogBinding binding = OrderSuccessDialogBinding.inflate(LayoutInflater.from(RazorPayActivity.this));
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //binding.tvDescription.setText("Your order with order id " + order_id + " has been successfully placed");
        dialog.show();
        dialog.setCancelable(false);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RazorPayActivity.this, "Go to order listing page from drawer menu", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RazorPayActivity.this, MainActivity.class));
                RazorPayActivity.this.finish();
                //Navigation.findNavController(view).navigate(R.id.nav_place_order_to_order_list);
                dialog.dismiss();
            }
        });
    }
}