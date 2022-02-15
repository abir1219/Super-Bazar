package com.superbazar.ui.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.superbazar.ui.Home.Adapter.BannerAdapter;
import com.superbazar.ui.Home.Adapter.CategoryAdapter;
import com.superbazar.MainActivity;
import com.superbazar.ui.Home.Adapter.ProductAdapter;
import com.superbazar.ui.Home.Model.BannerModel;
import com.superbazar.ui.Home.Model.CategoryModel;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentHomeBinding;
import com.superbazar.ui.Home.Model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    FragmentHomeBinding binding;
    ArrayList<BannerModel> bannerModelList;
    ArrayList<CategoryModel> categoryModelList;
    ArrayList<ProductModel> productModelList;
    ArrayList<ProductModel> bestSellerProductModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        BtnClick();
        setLayout();
        loadSlider();
        loadCategory();
        loadNewArrivalProducts();
        loadBestSellerProducts();
        return binding.getRoot();
    }

    private void loadBestSellerProducts() {
        bestSellerProductModelList = new ArrayList<>();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.BEST_SELLER_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("Best_Seller_RES",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray array = jsonObject.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String ProductId = object.getString("ProductId");
                            String ProductName = object.getString("ProductName");
                            String ProductMarketPrice = object.getString("ProductMarketPrice");
                            String ProductSellingPrice = object.getString("ProductSellingPrice");
                            JSONArray jsonArray = object.getJSONArray("ProductFiles");
                            //Log.d("SELLER_jsonArray",jsonArray.toString());
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String ProductFileName = "https://smlawb.org/superbazaar/web/uploads/product/" +
                                    obj.getString("ProductFileName");

                            bestSellerProductModelList.add(new ProductModel(ProductId,ProductName,ProductFileName,ProductMarketPrice,ProductSellingPrice));
                        }
                        ProductAdapter adapter = new ProductAdapter(bestSellerProductModelList,getActivity());
                        binding.rvBestSellerProduct.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void loadNewArrivalProducts() {
        productModelList = new ArrayList<>();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.NEW_ARRIVAL_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray array = jsonObject.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String ProductId = object.getString("ProductId");
                            String ProductName = object.getString("ProductName");
                            JSONArray jsonArray = object.getJSONArray("ProductFiles");
                            String ProductMarketPrice = object.getString("ProductMarketPrice");
                            String ProductSellingPrice = object.getString("ProductSellingPrice");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String ProductFileName = "https://smlawb.org/superbazaar/web/uploads/product/" +
                                    obj.getString("ProductFileName");

                            productModelList.add(new ProductModel(ProductId,ProductName,ProductFileName,ProductMarketPrice,ProductSellingPrice));
                        }
                        ProductAdapter adapter = new ProductAdapter(productModelList,getActivity());
                        binding.rvNewArrivalProduct.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void setLayout() {
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        binding.rvNewArrivalProduct.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        binding.rvBestSellerProduct.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
    }

    private void loadCategory() {
        categoryModelList = new ArrayList<>();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray array = jsonObject.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String CategoryId = object.getString("CategoryId");
                            String CategoryDisplayName = object.getString("CategoryDisplayName");
                            String CategoryImage = "https://smlawb.org/superbazaar/web/uploads/category/" + object.getString("CategoryImage");

                            categoryModelList.add(new CategoryModel(CategoryId,CategoryDisplayName,CategoryImage));
                        }
                        CategoryAdapter adapter = new CategoryAdapter(categoryModelList,getActivity());
                        binding.rvCategory.setAdapter(adapter);
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
        });

        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void loadSlider() {
        bannerModelList = new ArrayList<>();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.SLIDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray array = jsonObject.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String image = "https://smlawb.org/superbazaar/web/uploads/slider/"+object.getString("SliderImg");
                            bannerModelList.add(new BannerModel(image));

                            BannerAdapter adapter = new BannerAdapter(bannerModelList);
                            binding.sliderView.setSliderAdapter(adapter);
                            binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                            binding.sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                            binding.sliderView.startAutoCycle();
                        }
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
        });
        Volley.newRequestQueue(getActivity()).add(sr);
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