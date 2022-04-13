package com.superbazar.Helper;

import android.app.Application;
import android.content.Context;

import com.stripe.android.PaymentConfiguration;
import com.superbazar.Utils.CustomPreference;

public class YoDB extends Application {
    private static Context mContext;
    private static CustomPreference sharedPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;


        PaymentConfiguration.init(
                getApplicationContext(),
                //"pk_test_51BTUDGJAJfZb9HEBwDg86TN1KNprHjkfipXmEDMb0gSCassK5T3ZfxsAbcgKVmAIXF7oZ6ItlZZbXO6idTHE67IM007EwQ4uN3"
                "pk_live_51KnJ81SJcmJjtU7t2PBMOZgkezLVn0LhQFBftDFDlkfeRdaru5r8MXerxWKX7CCicHYl9LDLJsKrKedZS7AP7AJf00eQVhysxw"
        );
    }

    public static CustomPreference getPref(){
        if(sharedPreference == null){
            sharedPreference = new CustomPreference(mContext);
        }
        return sharedPreference;
    }
}
