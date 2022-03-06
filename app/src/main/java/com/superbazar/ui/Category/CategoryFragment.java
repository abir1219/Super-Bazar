package com.superbazar.ui.Category;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superbazar.Activity.LoginActivity;
import com.superbazar.Helper.YoDB;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.databinding.FragmentCategoryBinding;

public class CategoryFragment extends Fragment implements View.OnClickListener{
    FragmentCategoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater,container,false);

        btnClick();
        binding.tvName.setText(YoDB.getPref().read(Constants.NAME,""));
        binding.tvPhone.setText(YoDB.getPref().read(Constants.PHONE,""));
        return binding.getRoot();
    }

    private void btnClick() {
        binding.ivBack.setOnClickListener(this);
        binding.llEditProfile.setOnClickListener(this);
        binding.llLogout.setOnClickListener(this);
        binding.llLogin.setOnClickListener(this);
        binding.llAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;
            case R.id.llAddress:
                Bundle bundle = new Bundle();
                bundle.putString("from","account");
                Navigation.findNavController(v).navigate(R.id.nav_account_to_address_list,bundle);
                break;
            case R.id.llLogin:
                Intent mIntent = new Intent(getActivity(), LoginActivity.class);
                mIntent.putExtra("from", "account");
                startActivity(mIntent);
                getActivity().overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                getActivity().finish();
                break;
            case R.id.llEditProfile:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_account_to_profile);
                break;
        }
    }
}