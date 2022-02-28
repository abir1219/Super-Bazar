package com.superbazar.ui.OrderList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.databinding.FragmentOrderListBinding;

public class OrderListFragment extends Fragment implements View.OnClickListener{
    FragmentOrderListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderListBinding.inflate(inflater,container,false);

        btnClick();
        loadOrderList();
        return binding.getRoot();
    }

    private void loadOrderList() {

    }

    private void btnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);
        binding.flWishlist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.llCart:
                Bundle bundle = new Bundle();
                bundle.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_order_list_to_cart, bundle);
                break;
            case R.id.flWishlist:
                Bundle bundle1 = new Bundle();
                bundle1.putString("key", "val");
                Navigation.findNavController(v).navigate(R.id.nav_order_list_to_wishlist, bundle1);
                break;
        }
    }
}