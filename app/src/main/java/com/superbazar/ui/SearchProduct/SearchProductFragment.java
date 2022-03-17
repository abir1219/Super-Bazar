package com.superbazar.ui.SearchProduct;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentSearchProductBinding;
import com.superbazar.ui.SearchProduct.Adapter.SearchProductAdapter;
import com.superbazar.ui.SearchProduct.Model.SearchProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchProductFragment extends Fragment implements View.OnClickListener{
    FragmentSearchProductBinding binding;
    List<SearchProductModel> modelList;

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
        binding = FragmentSearchProductBinding.inflate(inflater,container,false);
        try {
            BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
            navBar.setVisibility(View.GONE);
        } catch (NullPointerException e) {
        }
        BtnClick();
        loadCartCount();
        loadWishlistCount();
        setLayout();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //searchProductModels.clear();
                if(newText.length() == 0){
                    binding.llNoSearchFound.setVisibility(View.VISIBLE);
                    binding.rvSearchList.setVisibility(View.GONE);
                }else{
                    binding.rvSearchList.setVisibility(View.VISIBLE);
                    binding.llNoSearchFound.setVisibility(View.GONE);
                    //binding.searchView.setFocusable(false);
                    getSearchList(newText);
                }
                return true;
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

    private void getSearchList(String newText) {
        modelList = new ArrayList<>();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.PRODUCT_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("SEARCH_RES",response);
                    if(jsonObject.getString("status").equals("1")){
                        JSONArray array = jsonObject.getJSONArray("results");
                        for(int i=0;i< array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            String ProductId = object.getString("ProductId");
                            String ProductName = object.getString("ProductName");
                            String ProductShortDescription = object.getString("ProductShortDescription");
                            JSONArray jsonArray = object.getJSONArray("ProductFiles");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String ProductFileName = "https://smlawb.org/superbazaar/web/uploads/product/" +obj.getString("ProductFileName");

                            modelList.add(new SearchProductModel(ProductId,ProductName,ProductShortDescription,ProductFileName));
                        }
                        SearchProductAdapter adapter = new SearchProductAdapter(modelList,getActivity());
                        binding.rvSearchList.setAdapter(adapter);

                    }else if(jsonObject.getString("status").equals("0")){
                        binding.searchView.setFocusable(true);
                        binding.llNoSearchFound.setVisibility(View.VISIBLE);
                        binding.rvSearchList.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("search_data",newText);
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void BtnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.llWisth.setOnClickListener(this);
    }

    private void setLayout() {
        binding.rvSearchList.setLayoutManager(new GridLayoutManager(getActivity(),1, RecyclerView.VERTICAL,false));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.llCart:
                Navigation.findNavController(view).navigate(R.id.nav_search_product_to_cart);
                break;
            case R.id.llWisth:
                Navigation.findNavController(view).navigate(R.id.nav_search_product_to_wishlist);
                break;
        }
    }
}