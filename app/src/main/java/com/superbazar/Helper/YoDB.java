package com.superbazar.Helper;

import android.app.Application;
import android.content.Context;

import com.superbazar.Utils.CustomPreference;

public class YoDB extends Application {
    private static Context mContext;
    private static CustomPreference sharedPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static CustomPreference getPref(){
        if(sharedPreference == null){
            sharedPreference = new CustomPreference(mContext);
        }
        return sharedPreference;
    }
}
