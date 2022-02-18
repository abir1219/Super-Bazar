package com.superbazar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.superbazar.Helper.YoDB;
import com.superbazar.MainActivity;
import com.superbazar.R;
import com.superbazar.Utils.Constants;
import com.superbazar.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    Animation animation;
    Handler mHandler;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        redirect();
    }

    private void redirect() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(YoDB.getPref().read(Constants.ID,"").isEmpty()){
                    intent = new Intent(SplashScreenActivity.this, RegActivity.class);
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    startActivity(intent);
                    finish();
                }
            }
        },2500);
    }
}