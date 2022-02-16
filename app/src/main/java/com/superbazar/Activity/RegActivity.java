package com.superbazar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.databinding.ActivityRegBinding;

public class RegActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityRegBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnClick();
    }

    private void btnClick() {
        binding.btSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btSignUp:
                startActivity(new Intent(RegActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                finish();
                break;
        }
    }
}