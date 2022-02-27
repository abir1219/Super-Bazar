package com.superbazar.ui.Address;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.superbazar.Helper.YoDB;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentAddressListBinding;
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
        setLayout();
        loadAddress();
        return binding.getRoot();
    }

    private void loadAddress() {
        modelList = new ArrayList<>();
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ADDRESS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("1")){
                        binding.llAddressFound.setVisibility(View.VISIBLE);
                        binding.noAddress.setVisibility(View.GONE);
                        JSONArray array = jsonObject.getJSONArray("results");
                        for(int i=0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            String Name = object.getString("Name");
                            String Phone = object.getString("Phone");
                            String PinCode = object.getString("PinCode");
                            String Address = object.getString("Address");
                            String Landmark = object.getString("Landmark");

                            modelList.add(new AddressModel(Name,Phone,Address,Landmark,PinCode));
                        }


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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbNewAddress:
                Navigation.findNavController(v).navigate(R.id.navigation_address_list_to_address);
                break;
        }
    }
}