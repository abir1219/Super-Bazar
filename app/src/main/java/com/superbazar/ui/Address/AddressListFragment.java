package com.superbazar.ui.Address;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.superbazar.R;
import com.superbazar.databinding.FragmentAddressListBinding;

public class AddressListFragment extends Fragment {
    FragmentAddressListBinding binding;

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        try{
            nav.setVisibility(View.GONE);
        }catch (Exception e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddressListBinding.inflate(inflater,container,false);
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        try{
            nav.setVisibility(View.GONE);
        }catch (Exception e){}
        return binding.getRoot();
    }
}