package com.miniproject.minipos;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.miniproject.minipos.Models.ProductModel;
import com.miniproject.minipos.R;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.miniproject.minipos.Models.UserModel;
import com.miniproject.minipos.StorageHelper.StorageManager;
import com.miniproject.minipos.WSHelper.WSHelper;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static xdroid.toaster.Toaster.toast;
import static xdroid.toaster.Toaster.toastLong;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private WSHelper wsHelper;
    private StorageManager sm;
    String url2;
    String re;
    String pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sm = new StorageManager(getApplicationContext());
        wsHelper = new WSHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      Declare global class
        String jsonUserModelObj = getIntent().getStringExtra("jsonUserModelObj");
        UserModel userModel = (UserModel)getApplication();
        userModel.setModelByJson(jsonUserModelObj);
//        String responseData = wsHelper.get("/products/mobile?authKey="+userModel.getUser().getAuthKey());

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        drawer = findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar
//        ,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(),wsHelper.getResData(),Toast.LENGTH_SHORT).show();
//            }
//        }, 2000);
//
//        try {
//            pd = new WSTask().execute("/products?authKey="+userModel.getUser().getAuthKey()).get();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        try {
            String res = new OkHttpHandler().execute("/products?authKey="+userModel.getUser().getAuthKey()).get();
            toast(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        return;
    }



    private class OkHttpHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        @Override
        protected String doInBackground(String... params) {
            String[] serverConfig = sm.readServerConfig();
            String serverPath = "http://"+serverConfig[0]+":"+serverConfig[1]+"/services";
            String newUrl = serverPath.concat(params[0]);

            Request.Builder builder = new Request.Builder();
            builder.url(newUrl);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
