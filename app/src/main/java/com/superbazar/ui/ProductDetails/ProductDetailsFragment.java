package com.superbazar.ui.ProductDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.FragmentProductDetailsBinding;
import com.superbazar.ui.ProductDetails.Adapter.SliderAdapter;
import com.superbazar.ui.ProductDetails.Model.SliderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductDetailsFragment extends Fragment implements View.OnClickListener {
    FragmentProductDetailsBinding binding;
    List<SliderModel> modelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        BtnClick();
        loadProduct();
        return binding.getRoot();
    }

    private void loadProduct() {
        modelList = new ArrayList<>();
        String api = Urls.PRODUCT_DETAILS + "?id=" + getArguments().getString("id");
        StringRequest sr = new StringRequest(Request.Method.POST, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("results");
                        binding.tvOffPrice.setText("₹ " + object.getString("ProductMarketPrice"));
                        binding.tvPrice.setText("₹ " + object.getString("ProductSellingPrice"));
                        binding.tvProductName.setText(object.getString("ProductName"));
                        binding.tvProdDesc.setText(object.getString("ProductShortDescription"));
                        binding.tvpDescr.setText(Html.fromHtml(object.getString("ProductLongDescription")));

                        JSONArray array = object.getJSONArray("ProductFiles");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String image = "https://smlawb.org/superbazaar/web/uploads/product/"+obj.getString("ProductFileName");
                            modelList.add(new SliderModel(image));
                        }
                        SliderAdapter adapter = new SliderAdapter(modelList,getActivity());
                        binding.sliderView.setSliderAdapter(adapter);
                        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        binding.sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                        binding.sliderView.startAutoCycle();
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