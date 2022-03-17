package com.superbazar.ui.Profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.superbazar.Helper.ManageLoginData;
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentProfileBinding;
import com.superbazar.databinding.PwChangeLayoutBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    FragmentProfileBinding binding;
    private String getEditData = "";
    ProgressDialog dialog;
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
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        try {
            BottomNavigationView navView = getActivity().findViewById(R.id.bottom_nav);
            navView.setVisibility(View.GONE);
        } catch (Exception e) {
        }
        btnClick();
        loadCartCount();
        loadWishlistCount();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
        loadUser();
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

    private void loadUser() {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("Response",object.toString());
                    JSONObject obj = object.getJSONObject("result");

                    String Fullname = obj.getString("Fullname");
                    String Phone = obj.getString("Phone");
                    String Email = obj.getString("Email");
                    String Username = obj.getString("Username");
                    //String fullName = obj.getString("Fullname");

                    binding.tvName.setText(Fullname);
                    binding.tvContact.setText(Phone);
                    binding.tvEmail.setText(Email);
                    binding.tvUsername.setText(Username);
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
                body.put("user_id", YoDB.getPref().read(Constants.ID,""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void btnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.tvName.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.llWisth.setOnClickListener(this);
        binding.tvContact.setOnClickListener(this);
        binding.tvEmail.setOnClickListener(this);
        binding.tvAddress.setOnClickListener(this);
        binding.tvUsername.setOnClickListener(this);
        binding.changePw.setOnClickListener(this);
        binding.btnUpdate.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
    }


    private void inputDilog(String getEditData) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        final TextView dialogTitle = (TextView) mView.findViewById(R.id.dialogTitle);

        if (getEditData.equalsIgnoreCase(Constants.NAME)) {
            dialogTitle.setText("Enter Name");
            if (binding.tvName.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Name");
            } else {
                userInputDialogEditText.setText(binding.tvName.getText().toString());
            }
        }else if (getEditData.equalsIgnoreCase(Constants.PHONE)) {
            dialogTitle.setText("Enter Phone No");
            if (binding.tvContact.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Phone Number");
            } else {
                userInputDialogEditText.setText(binding.tvContact.getText().toString());
            }
        }else if (getEditData.equalsIgnoreCase(Constants.EMAIL)) {
            dialogTitle.setText("Enter Email Id");
            if (binding.tvEmail.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Email Address");
            } else {
                userInputDialogEditText.setText(binding.tvEmail.getText().toString());
            }
        }else if (getEditData.equalsIgnoreCase("username")) {
            dialogTitle.setText("Enter Username");
            if (binding.tvUsername.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Username");
            } else {
                //Toast.makeText(getActivity(), "Username: "+binding.tvUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                userInputDialogEditText.setText(binding.tvUsername.getText().toString());
            }
        }else if (getEditData.equalsIgnoreCase(Constants.ADDRESS)) {
            dialogTitle.setText("Enter Address");
            if (binding.tvAddress.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Address");
            } else {
                userInputDialogEditText.setText(binding.tvAddress.getText().toString());
            }
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int which) {
                        if (userInputDialogEditText.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(),"Please Input Valid Info!",Toast.LENGTH_LONG).show();
                        }else {
                            if (getEditData.equalsIgnoreCase(Constants.NAME)){
                                binding.tvName.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }else if (getEditData.equalsIgnoreCase(Constants.EMAIL)){
                                binding.tvEmail.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }else if (getEditData.equalsIgnoreCase(Constants.PHONE)){
                                binding.tvContact.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            } else if (getEditData.equalsIgnoreCase("username")){
                                binding.tvUsername.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }else if (getEditData.equalsIgnoreCase(Constants.ADDRESS)){
                                binding.tvAddress.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;
            case R.id.llSearch:
                ((MainActivity) getActivity()).searchProduct(R.id.nav_profile_to_search);
                break;
            case R.id.tvName:
                getEditData = Constants.NAME;
                inputDilog(getEditData);
                break;
            case R.id.tvUsername:
                getEditData = "username";
                inputDilog(getEditData);
                break;
            case R.id.tvContact:
                Log.d("Clicked", "Phone");
                getEditData = Constants.PHONE;
                inputDilog(getEditData);
                break;
            case R.id.tvEmail:
                Log.d("Clicked", "Email");
                getEditData = Constants.EMAIL;
                inputDilog(getEditData);
                break;
            case R.id.tvAddress:
                Log.d("Clicked", "Address");
                getEditData = Constants.ADDRESS;
                inputDilog(getEditData);
                break;
            case R.id.changePw:
                showPasswordChangeDialog();
                break;
            case R.id.llCart:
                Navigation.findNavController(v).navigate(R.id.nav_profile_to_cart);
                break;
            case R.id.llWisth:
                Navigation.findNavController(v).navigate(R.id.nav_profile_to_wishlist);
                break;
            case R.id.btnUpdate:
                updateProfile();
                break;
        }
    }

    private void updateProfile() {
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_PROFILE_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        JSONObject object = jsonObject.getJSONObject("data");
                        String userId = object.getString("userId");
                        String name = object.getString("name");
                        String phone = object.getString("phone");
                        String email = object.getString("email");

                        ManageLoginData.addLoginData(userId,name,email,phone);
                        loadUser();
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
                WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                String WebUserIP = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

                Map<String, String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                body.put("WebUserIP",WebUserIP);
                body.put("WebUserPhone",binding.tvContact.getText().toString());
                body.put("WebUserEmail",binding.tvEmail.getText().toString());
                body.put("WebUserFullName",binding.tvName.getText().toString());
                body.put("WebUserName",binding.tvUsername.getText().toString());
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void showPasswordChangeDialog(){
        final Dialog dialog = new Dialog(getActivity());
        PwChangeLayoutBinding binding1 = PwChangeLayoutBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(binding1.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCancelable(false);
        binding1.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding1.newPw.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter new password", Toast.LENGTH_SHORT).show();
                    binding1.newPw.requestFocus();
                }else{
                    updatePassword(dialog,binding1.newPw.getText().toString());
                }
            }
        });

        binding1.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void updatePassword(Dialog dialogWindow, String newPW) {
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("1")){
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogWindow.dismiss();
                    }else{
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogWindow.dismiss();
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
                Map<String,String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                body.put("new_password",newPW);
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

}