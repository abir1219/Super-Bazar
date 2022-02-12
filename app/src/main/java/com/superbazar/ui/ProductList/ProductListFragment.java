package com.superbazar.ui.ProductList;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentProductListBinding;

import java.util.HashMap;
import java.util.Map;

public class ProductListFragment extends Fragment implements View.OnClickListener{
    FragmentProductListBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductListBinding.inflate(inflater,container,false);
        BtnClick();
        //Bundle bundle = getArguments();
        //Log.d("ID","dsasda"+bundle.getString("id"));
        //Toast.makeText(getActivity(), "ID:"+bundle.getString("id"), Toast.LENGTH_SHORT).show();
        setLayout();
        loadProductList();
        return binding.getRoot();
    }

    private void loadProductList() {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CATEGORYWISEPRODCUTDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("id",getArguments().getString("id"));
                return body;

            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void setLayout() {
        binding.rvProductList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
    }

    private void BtnClick() {
        binding.llMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;
        }
    }
}