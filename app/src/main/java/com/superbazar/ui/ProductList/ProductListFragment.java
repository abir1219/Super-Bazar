package com.superbazar.ui.ProductList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentProductListBinding;
import com.superbazar.ui.ProductList.Adapter.ProductListAdapter;
import com.superbazar.ui.ProductList.Model.ProductListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements View.OnClickListener {
    FragmentProductListBinding binding;
    List<ProductListModel> modelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        BtnClick();
        //Bundle bundle = getArguments();
        //Log.d("ID","dsasda"+bundle.getString("id"));
        //Toast.makeText(getActivity(), "ID:"+bundle.getString("id"), Toast.LENGTH_SHORT).show();
        setLayout();
        loadProductList();
        return binding.getRoot();
    }

    private void loadProductList() {
        modelList = new ArrayList<>();
        String api = Urls.CATEGORYWISEPRODCUTDETAILS + "?id=" + getArguments().getString("id");
        Log.d("API RES", "API: " + api);
        StringRequest sr = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("1")) {
                        JSONArray jsonArray = object.getJSONArray("results");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            String ProductId = jobj.getString("ProductId");
                            String ProductName = jobj.getString("ProductName");
                            String ProductShortDescription = jobj.getString("ProductShortDescription");
                            String ProductMarketPrice = jobj.getString("ProductMarketPrice");
                            String ProductSellingPrice = jobj.getString("ProductSellingPrice");
                            JSONArray array = jobj.getJSONArray("ProductFiles");
                            //for(int i=0;i<array.length();i++){}
                            JSONObject jsonObject = array.getJSONObject(0);
                            String productImage = "https://smlawb.org/superbazaar/web/uploads/product/" + jsonObject.getString("ProductFileName");
                            modelList.add(new ProductListModel(ProductId, ProductName, productImage, ProductMarketPrice, ProductSellingPrice, ProductShortDescription));
                        }
                    }
                    ProductListAdapter adapter = new ProductListAdapter(modelList,getActivity());
                    binding.rvProductList.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })/*{
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("BODY_ID",getArguments().getString("id"));
                Map<String, String> body = new HashMap<>();
                body.put("id",getArguments().getString("id"));
                return body;
            }
        }*/;
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void setLayout() {
        binding.rvProductList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
    }

    private void BtnClick() {
        binding.llMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
        }
    }
}