package com.superbazar.ui.ProductDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentProductDetailsBinding;
import com.superbazar.ui.Home.Adapter.ProductAdapter;
import com.superbazar.ui.Home.Model.ProductModel;
import com.superbazar.ui.ProductDetails.Adapter.SliderAdapter;
import com.superbazar.ui.ProductDetails.Model.SliderModel;
import com.superbazar.ui.ProductList.Adapter.ProductListAdapter;
import com.superbazar.ui.ProductList.Model.ProductListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDetailsFragment extends Fragment implements View.OnClickListener {
    FragmentProductDetailsBinding binding;
    List<SliderModel> modelList;
    String imageLink = "";
    String categoryId = "";
    ProgressDialog progressDialog;
    String productId = "";

    List<ProductModel> prodmodelList;

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
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        try {
            BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
            navBar.setVisibility(View.GONE);
        } catch (NullPointerException e) {
        }
        setLayout();
        BtnClick();
        loadProduct();

        Zoomy.Builder builder = new Zoomy.Builder(getActivity())
                .target(binding.sliderView)
                .animateZooming(false)
                .enableImmersiveMode(false)
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {

                    }
                });
        builder.register();

        binding.tvViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent imageIntent = new Intent(getActivity(),ViewImageActivity.class);
                imageIntent.putExtra("image",imageLink);
                imageIntent.putExtra("id",getArguments().getString("id"));
                startActivity(imageIntent);
                getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);*/

                Bundle bundle = new Bundle();
                bundle.putString("image", imageLink);
                bundle.putString("id", getArguments().getString("id"));
                Navigation.findNavController(view).navigate(R.id.nav_product_details_to_view_image, bundle);
            }
        });

        return binding.getRoot();
    }

    private void setLayout() {
        binding.rvFeaturesPro.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
    }

    private void loadRelatedProduct(String categoryId) {
        prodmodelList = new ArrayList<>();
        String api = Urls.CATEGORYWISEPRODCUTDETAILS + "?id=" + categoryId;
        Log.d("API RES", "API: " + api);
        StringRequest sr = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_RELATE", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("1")) {
                        JSONArray jsonArray = object.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            String ProductId = jobj.getString("ProductId");
                            productId = ProductId;
                            String ProductName = jobj.getString("ProductName");
                            String categoryName = jobj.getString("CategoryName");
                            String ProductShortDescription = jobj.getString("ProductShortDescription");
                            String ProductMarketPrice = jobj.getString("ProductMarketPrice");
                            String ProductSellingPrice = jobj.getString("ProductSellingPrice");
                            JSONArray array = jobj.getJSONArray("ProductFiles");
                            //for(int i=0;i<array.length();i++){}
                            JSONObject jsonObject = array.getJSONObject(0);
                            String productImage = "https://smlawb.org/superbazaar/web/uploads/product/" + jsonObject.getString("ProductFileName");
                            prodmodelList.add(new ProductModel(ProductId, ProductName, productImage, ProductMarketPrice, ProductSellingPrice, categoryName));
                        }
                    }
                    ProductAdapter adapter = new ProductAdapter(prodmodelList, getActivity());
                    binding.rvFeaturesPro.setAdapter(adapter);

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

    private void loadProduct() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        modelList = new ArrayList<>();
        String api = Urls.PRODUCT_DETAILS + "?id=" + getArguments().getString("id");
        StringRequest sr = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("results");
                        categoryId = object.getString("CategoryId");
                        //Toast.makeText(getActivity(), "categoryId: "+categoryId+",CategoryId"+object.getString("CategoryId"), Toast.LENGTH_SHORT).show();
                        binding.tvPrice.setText("₹ " + object.getString("ProductMarketPrice"));
                        binding.tvOffPrice.setText("₹ " + object.getString("ProductSellingPrice"));
                        binding.tvProductName.setText(object.getString("ProductName"));
                        binding.tvProdDesc.setText(object.getString("ProductShortDescription"));
                        binding.tvpDescr.setText(Html.fromHtml(object.getString("ProductLongDescription")));

                        JSONArray array = object.getJSONArray("ProductFiles");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String image = "https://smlawb.org/superbazaar/web/uploads/product/" + obj.getString("ProductFileName");
                            imageLink = image;
                            modelList.add(new SliderModel(image));
                        }
                        SliderAdapter adapter = new SliderAdapter(modelList, getActivity());
                        binding.sliderView.setSliderAdapter(adapter);
                        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        binding.sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                        binding.sliderView.startAutoCycle();

                        loadRelatedProduct(categoryId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Getting some troubles.", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void BtnClick() {
        progressDialog = new ProgressDialog(getActivity());
        binding.llMenu.setOnClickListener(this);
        binding.llAddtoCart.setOnClickListener(this);
        binding.llAddtoWishlist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.llAddtoCart:
                addToCart();
                break;
            case R.id.llAddtoWishlist:
                addToWishlist();
                break;
        }
    }

    private void addToWishlist() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("1")){
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("WebUserId", YoDB.getPref().read(Constants.ID, ""));
                body.put("Type", "wishlist");
                body.put("ProductId", productId);
                body.put("Quantity", binding.tvCount.getText().toString());
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void addToCart() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("1")){
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("WebUserId", YoDB.getPref().read(Constants.ID, ""));
                body.put("Type", "cart");
                body.put("ProductId", productId);
                body.put("Quantity", binding.tvCount.getText().toString());
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }
}