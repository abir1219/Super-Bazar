package com.superbazar.ui.ProductDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superbazar.R;
import com.superbazar.databinding.FragmentProductDetailsBinding;


public class ProductDetailsFragment extends Fragment {
    FragmentProductDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}