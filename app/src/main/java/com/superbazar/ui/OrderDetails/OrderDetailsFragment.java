package com.superbazar.ui.OrderDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superbazar.R;
import com.superbazar.databinding.FragmentOrderDetailsBinding;

public class OrderDetailsFragment extends Fragment {
    FragmentOrderDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}