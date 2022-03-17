package com.superbazar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.superbazar.Activity.LoginActivity;
import com.superbazar.Activity.RegActivity;
import com.superbazar.Adapter.DrawerAdapter;
import com.superbazar.Helper.ManageLoginData;
import com.superbazar.Helper.YoDB;
import com.superbazar.Model.DrawerModel;
import com.superbazar.Utils.Constants;
import com.superbazar.Utils.Urls;
import com.superbazar.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private NavOptions.Builder navBuilder;

    private ActionBarDrawerToggle mDrawerToggle;

    List<DrawerModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.baseColor));
        }
        setDefaultView();
        setDrawerMenu();
        binding.navProfileName.setText(YoDB.getPref().read(Constants.NAME,""));
        binding.navProfileEmail.setText(YoDB.getPref().read(Constants.EMAIL,""));
        //setVersion();
        /*loadUserDetails();
        loadProfile();*/
    }

    /*private void loadProfile() {
        CustomProgressDialog.showDialog(MainActivity.this,true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.PERSONAL_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(MainActivity.this,false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("0")){
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            CustomPreference cp = new CustomPreference(MainActivity.this);
                            cp.write(Constants.REFERRAL_CODE,"",object.getString("refferal_code"));
                            cp.write(Constants.QCOIN,"",object.getString("qcoins"));
                        }
                    }else{
                        CustomProgressDialog.showDialog(MainActivity.this,false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(MainActivity.this,false);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID,""));
                return body;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(sr);
    }

    private void loadUserDetails() {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    CustomPreference cp = new CustomPreference(MainActivity.this);
                    if(object.getString("user_personal_percentage").isEmpty() ||
                            object.getString("other_details_percentage").isEmpty() ||
                            object.getString("kyc_percentage").isEmpty() ||
                            object.getString("pan_percentage").isEmpty() ||
                            object.getString("doc_percentage").isEmpty() ||
                            object.getString("alternate_contact_verify").isEmpty() ||
                            object.getString("mail_verify").isEmpty()){
                        cp.write(Constants.isFullyDocumented,"","false");
                    }else{
                        cp.write(Constants.isFullyDocumented,"","true");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userid", YoDB.getPref().read(Constants.ID, ""));
                return map;
            }
        };;
        Volley.newRequestQueue(MainActivity.this).add(sr);
    }*/

    private void setDrawerMenu() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvMenu.setLayoutManager(layoutManager);

        /*int goldenColor = Color.parseColor("#F7DC5F");
        int whiteColor = Color.parseColor("#FFFFFF");*/

        modelList = new ArrayList<>();

        modelList.add(new DrawerModel(R.drawable.ic_baseline_home_24, "Home"));
        modelList.add(new DrawerModel(R.drawable.bag, "Cart"));
        modelList.add(new DrawerModel(R.drawable.love, "Wishlist"));
        modelList.add(new DrawerModel(R.drawable.list, "My Order"));
        if(YoDB.getPref().read(Constants.ID,"").isEmpty()){
            modelList.add(new DrawerModel(R.drawable.logout, "Login"));
        }else{
            modelList.add(new DrawerModel(R.drawable.logout, "Logout"));
        }
        /*modelList.add(new DrawerModel(R.drawable.stretching, "Activity"));
        modelList.add(new DrawerModel(R.drawable.coins, "QCoins"));
        modelList.add(new DrawerModel(R.drawable.payment_method, "Payment Method"));
        modelList.add(new DrawerModel(R.drawable.ic_friends, "Friends"));
        modelList.add(new DrawerModel(R.drawable.ic_refer, "Refer & Earn"));
        modelList.add(new DrawerModel(R.drawable.ic_help, "Help"));
        modelList.add(new DrawerModel(R.drawable.logout, "Logout"));*/

        DrawerAdapter drawerMenuAdapter = new DrawerAdapter(modelList, this);
        binding.rvMenu.setAdapter(drawerMenuAdapter);

        drawerMenuAdapter.setListenerDrawerMenu(new OnDrawerMenuListener() {
            @Override
            public void onDrawerMenuClick(int pos) {
                Bundle bundle = new Bundle();
                switch (pos) {
                    case 0:
                        Log.e("Pos", "= 0");
                            /*DrawerAdapter.DrawerViewHolder.ivMenu.setImageTintList(DrawerAdapter.selected_postion == pos ? ColorStateList.valueOf(goldenColor) : ColorStateList.valueOf(whiteColor));
                            DrawerAdapter.DrawerViewHolder.tvMenu.setTextColor(DrawerAdapter.selected_postion == pos ? getResources().getColor(R.color.goldenColor) : getResources().getColor(R.color.white));*/
                            /*drawerMenuAdapter.notifyItemChanged(DrawerAdapter.selected_postion);
                            DrawerAdapter.selected_postion = pos;*/
                        //drawerMenuAdapter.notifyItemChanged(DrawerAdapter.selected_postion);

                        navController.navigate(R.id.navigation_home, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;
                    case 1:
                        navController.navigate(R.id.navigation_cart, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;

                    case 2:
                        navController.navigate(R.id.navigation_wishlist, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;

                    case 3:
                        navController.navigate(R.id.navigation_order_list, bundle, navBuilder.build());//This will open
                        openDrawer();
                        break;

                    case 4:
                        if(YoDB.getPref().read(Constants.ID,"").isEmpty()){
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                            finish();
                        }else{
                            logout();
                        }
                        openDrawer();
                        break;


                }
            }

            private void logout() {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Logging Out");
                progressDialog.show();
                StringRequest sr = new StringRequest(Request.Method.POST, Urls.LOGOUT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        ManageLoginData.clearLoginData();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> body = new HashMap<>();
                        body.put("id", YoDB.getPref().read(Constants.ID,""));
                        return body;
                    }
                };
                Volley.newRequestQueue(MainActivity.this).add(sr);
            }
        });
    }

    public void searchProduct(int destination){
        navController.navigate(destination);//This will open
    }

    public void openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }


    private void setDefaultView() {
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.appBarMain.bottomNav, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.fade_in_animation)
                .setExitAnim(R.anim.fade_out_animation)
                .setPopEnterAnim(R.anim.fade_in_animation)
                .setPopExitAnim(R.anim.fade_out_animation);

        /*mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                R.drawable.ic_dashboard_black_24dp, R.string.title_home) {*/
        mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                R.drawable.ic_dashboard_black_24dp, R.string.title_home) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
                Log.e("onDrawerClosed= ", "Closed");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                Log.e("onDrawerOpened= ", "Closed");
            }
        };
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
    }

    public interface OnDrawerMenuListener {
        void onDrawerMenuClick(int pos);
    }
}