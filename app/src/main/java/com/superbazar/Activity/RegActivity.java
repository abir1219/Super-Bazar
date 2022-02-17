package com.superbazar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.ActivityRegBinding;

public class RegActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityRegBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(RegActivity.this);
        btnClick();
    }

    private void btnClick() {
        binding.btSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btSignUp:
                /*startActivity(new Intent(RegActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                finish();*/
                checkData();
                break;
        }
    }

    private void checkData() {
        if(binding.etName.getText().toString().isEmpty()){
            Toast.makeText(RegActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            binding.etName.requestFocus();
        }else if(binding.etPhone.getText().toString().isEmpty()){
            Toast.makeText(RegActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            binding.etPhone.requestFocus();
        }else if(binding.etPhone.getText().toString().length() != 10){
            Toast.makeText(RegActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            binding.etPhone.requestFocus();
        }else if(binding.etUserName.getText().toString().isEmpty()){
            Toast.makeText(RegActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            binding.etUserName.requestFocus();
        }else if(binding.etEmail.getText().toString().isEmpty()){
            Toast.makeText(RegActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            binding.etEmail.requestFocus();
        }else if(binding.etUserName.getText().toString().isEmpty()){
            Toast.makeText(RegActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            binding.etUserName.requestFocus();
        }else if(binding.etPassword.getText().toString().isEmpty()){
            Toast.makeText(RegActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            binding.etPassword.requestFocus();
        }else if(binding.etConfirmPassword.getText().toString().isEmpty()){
            Toast.makeText(RegActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            binding.etConfirmPassword.requestFocus();
        }else if(!binding.etConfirmPassword.getText().toString().equals(binding.etPassword.getText().toString())){
            Toast.makeText(RegActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
            binding.etConfirmPassword.requestFocus();
        }else{
            registerUser();
        }
    }

    private void registerUser() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String WebUserIP = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        String api = Urls.WEBUSER+"?WebUserIP="+WebUserIP+"&WebUserPhone="+binding.etPhone.getText().toString()+
                "&WebUserEmail="+binding.etEmail.getText().toString()+"&WebUserFullName="+binding.etName.getText().toString()+
                "&WebUserName="+binding.etUserName.getText().toString()+"&WebUserPassword="+binding.etPassword.getText().toString();

        Log.d("REG_RES",api);
        StringRequest sr = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("REG_RESPONSE",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        Volley.newRequestQueue(RegActivity.this).add(sr);
    }
}