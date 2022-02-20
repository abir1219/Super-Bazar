package com.superbazar.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.superbazar.Helper.ManageLoginData;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnClick();
    }

    private void btnClick() {
        binding.llSkip.setOnClickListener(this);
        binding.llRegister.setOnClickListener(this);
        binding.btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llSkip:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                finish();
                break;
            case R.id.llRegister:
                startActivity(new Intent(LoginActivity.this, RegActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                finish();
                break;
            case R.id.btLogin:
                checkLoginDetails();
                break;

        }
    }

    private void checkLoginDetails() {
        if(binding.etUsername.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
            binding.etUsername.requestFocus();
        }else if(binding.etPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            binding.etPassword.requestFocus();
        }else{
            login();
        }
    }

    private void login() {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //+"?username="+binding.etUsername.getText().toString()
        //                +"&password="+binding.etPassword.getText().toString()

        StringRequest sr = new StringRequest(Request.Method.POST, Urls.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("LOGIN RES",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        JSONObject object = jsonObject.getJSONObject("data");
                        String WebUserId = object.getString("WebUserId");
                        String name = object.getString("WebUserFullName");
                        String WebUserEmail = object.getString("WebUserEmail");
                        String WebUserPhone = object.getString("WebUserPhone");

                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        ManageLoginData.addLoginData(WebUserId,name,WebUserEmail,WebUserPhone);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("username",binding.etUsername.getText().toString());
                body.put("password",binding.etPassword.getText().toString());
                return body;
            }
        };
        Volley.newRequestQueue(LoginActivity.this).add(sr);
    }
}