package com.miniproject.minipos.WSHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.miniproject.minipos.DBHelper.DatabaseHelper;
import com.miniproject.minipos.Models.MessageModel;
import com.miniproject.minipos.Models.ProductModel;
import com.miniproject.minipos.R;
import com.miniproject.minipos.StorageHelper.StorageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static xdroid.toaster.Toaster.toast;
import static xdroid.toaster.Toaster.toastLong;

public class WSHelper {
    Context context;
    String test;
    String resData;

    public WSHelper(Context context){
        this.context = context;
    }
    StorageManager sm = new StorageManager(context);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();
    public Call post(String url, String json, Callback callback) throws IOException {
        String[] serverConfig = sm.readServerConfig();
        String serverPath = "http://"+serverConfig[0]+":"+serverConfig[1]+"/services";
        String newUrl = serverPath.concat(url);
        System.out.println("------------------------");
        System.out.println(newUrl);
        System.out.println("------------------------");

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(newUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public String getRequest(String url){
        String[] serverConfig = sm.readServerConfig();
        String serverPath = "http://"+serverConfig[0]+":"+serverConfig[1]+"/services";
        String newUrl = serverPath.concat(url);
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(serverPath)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                resData = responseData;
            }
        });
        return null;
    }

    public void testConnector(final String serverIP, final String serverPort) throws IOException{
        final String serverPath = "http://"+serverIP.concat(":" + serverPort+"/services/testConnector");
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("กำลังทดสอบการเชื่อมต่อ...");
        progressDialog.show();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(serverPath)
                .build();
            client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast("ไม่สามารถเชื่อมต่อเซิฟเวอร์ได้");
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                MessageModel message = new MessageModel(responseData);
                if(message.getMessage().getMessage().toString().equalsIgnoreCase("Connected")){
                    toast("เชื่อมต่อเซิฟเวอร์ได้");
                    System.out.println("----------------------------");
                    System.out.println(serverPath);
                    System.out.println("----------------------------");
                    Log.e("test","inside");
                    test = "555";
                    progressDialog.dismiss();
               }else{
                    System.out.println("----------------------------");
                    System.out.println(message.getMessage().getMessage().toString());
                    System.out.println("----------------------------");
                   toast("เซิฟเวอร์ไม่ถูกต้อง");
                   progressDialog.dismiss();
                }


            }
        });
        Log.e("test","oue");    }

    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    public String getTest() {
        return test;
    }
}
