package com.superbazar.ui.StripePay;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.ActivityCheckoutBinding;
import com.superbazar.databinding.OrderSuccessDialogBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CheckoutActivity extends AppCompatActivity {
    ActivityCheckoutBinding binding;

    String SECRET_KEY = "sk_test_51Ko4PfSHEt3Npo9LkwLAqZz8NSztQF3CtCa2CxNgnJejms0W1or2AXU94bcHzHvHMdqRGQ9fXldTbFzCKTx7kq5l00XIWEhvq1";
    String PUBLISH_KEY = "pk_test_51Ko4PfSHEt3Npo9LZ0yezYX2qtYhZZqL2eIRvgvIOGv6lNvv3bXhTGXaGXz6Cyr8KRYiYeT6fzbcR4zCzAIfkpgD009XZFtpwL";


    String CustomerId = "";
    String EphemeralKey = "";
    String ClientSecret = "";
    PaymentSheet paymentSheet;

    ProgressDialog progressDialog;

    float totalAmount;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        PaymentConfiguration.init(this, PUBLISH_KEY);
        /*paymentSheet = new PaymentSheet(this, new PaymentSheetResultCallback() {
            @Override
            public void onPaymentSheetResult(@NonNull PaymentSheetResult paymentSheetResult) {
                onPaymentResult(paymentSheetResult);
            }
        });*/

        //paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        paymentSheet = new PaymentSheet(this, PaymentSheetResult -> {
            onPaymentResult(PaymentSheetResult);
        });
        //paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        /*PaymentIntentResult -> {
            onPaymentResult(PaymentIntentResult);
        }*/


        binding.tvAmount.setText("Total Amount: Rs."+getIntent().getStringExtra("total"));
        Log.d("AMOUNT_RES", getIntent().getStringExtra("total").replace(",", ""));
        totalAmount = Float.parseFloat(getIntent().getStringExtra("total"));

        totalAmount = totalAmount * 100;

        int amnt = (int)totalAmount;
        amount = String.valueOf(amnt);


        /*if(totalAmount.contains(".")){
            amount = String.valueOf(Integer.parseInt(amount) *100);
        }*/


        binding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentFlow();
                Log.d("BUTTON_CLICK", "CustomerId: " + CustomerId + " EmpharalId: " + EphemeralKey + " ClientSecret: " + ClientSecret);
            }
        });

        StringRequest sr = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    CustomerId = object.getString("id");
                    getEphemeralKey(CustomerId);

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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(sr);

    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Cancelled Payment", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            //Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            placeOrder();
        }
    }

    private void getEphemeralKey(String customerId) {
        Log.d("CUSTOMER_ID",customerId);

        StringRequest sr = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    EphemeralKey = object.getString("id");

                    //placeOrder();

                    getClientSecret(CustomerId, EphemeralKey);
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2020-08-27");
                return header;
            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("customer", customerId);
                return body;
            }
        };
        Volley.newRequestQueue(this).add(sr);
    }

    private void getClientSecret(String customerId, String ephemeralKey) {
        StringRequest sr = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("ClientSecret_RES",response);
                    ClientSecret = object.getString("client_secret");
                    Log.d("ClientSecret_KEY",ClientSecret);
                    progressDialog.dismiss();

                    //getClientSecret(CustomerId, EphemeralKey);
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2020-08-27");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("PARAMS_BODY","customer: "+customerId+",ClientSecret: "+ClientSecret+",currency: inr");

                Map<String, String> body = new HashMap<>();
                body.put("customer", customerId);
                body.put("amount", amount);
                body.put("currency", "inr");
                body.put("automatic_payment_methods[enabled]", "true");
                return body;
            }
        };
        Volley.newRequestQueue(this).add(sr);
    }

    private void placeOrder() {
        ProgressDialog dialog = new ProgressDialog(CheckoutActivity.this);
        /*dialog.setMessage("Loading...");
        dialog.show();*/
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.PLACE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ONLINE_RES", response);
                //dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        showSuccessDialog();
                    } else {
                        Toast.makeText(CheckoutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog.dismiss();
                Toast.makeText(CheckoutActivity.this, "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("user_id", YoDB.getPref().read(Constants.ID, ""));
                Log.d("payment_type", getIntent().getStringExtra("payment_type"));
                Log.d("address_id", getIntent().getStringExtra("addressId"));
                Log.d("total", getIntent().getStringExtra("total").replace(",", ""));

                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                body.put("payment_type", getIntent().getStringExtra("payment_type"));
                body.put("address_id", getIntent().getStringExtra("addressId"));
                body.put("total", getIntent().getStringExtra("total").replace(",", ""));
                body.put("delidery_charge",getIntent().getStringExtra("deliveryCharge"));
                return body;
            }
        };
        Volley.newRequestQueue(CheckoutActivity.this).add(sr);
    }


    private void showSuccessDialog() {
        final Dialog dialog = new Dialog(CheckoutActivity.this);
        OrderSuccessDialogBinding binding = OrderSuccessDialogBinding.inflate(LayoutInflater.from(CheckoutActivity.this));
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //binding.tvDescription.setText("Your order with order id " + order_id + " has been successfully placed");
        dialog.show();
        dialog.setCancelable(false);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CheckoutActivity.this, "Go to order listing page from drawer menu", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
                CheckoutActivity.this.finish();
                //Navigation.findNavController(view).navigate(R.id.nav_place_order_to_order_list);
                dialog.dismiss();
            }
        });
    }

    private void paymentFlow() {

        Log.d("ClientSecret",ClientSecret);

        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration(
                "SuperBazar", new PaymentSheet.CustomerConfiguration(CustomerId, EphemeralKey)
        ));

    }
}