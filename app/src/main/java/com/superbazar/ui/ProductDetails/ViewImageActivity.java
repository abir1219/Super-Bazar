package com.superbazar.ui.ProductDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.superbazar.R;
import com.superbazar.databinding.ActivityViewImageBinding;
import com.superbazar.databinding.FragmentProductDetailsBinding;

public class ViewImageActivity extends Fragment {
    ActivityViewImageBinding binding;


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
        binding = ActivityViewImageBinding.inflate(inflater, container, false);
        try {
            BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_nav);
            navBar.setVisibility(View.GONE);
        } catch (NullPointerException e) {
        }

        Glide.with(this).load(getArguments().getString("image")).into(binding.ivImage);

        binding.cvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", getArguments().getString("id"));
                Navigation.findNavController(view).navigate(R.id.nav_to_view_image_product_details, bundle);
            }
        });

        Zoomy.Builder builder = new Zoomy.Builder(getActivity())
                .target(binding.ivImage)
                .animateZooming(false)
                .enableImmersiveMode(false)
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {

                    }
                });
        builder.register();

        return binding.getRoot();
    }
}