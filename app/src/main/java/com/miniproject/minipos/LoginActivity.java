package com.miniproject.minipos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miniproject.minipos.Models.MessageModel;
import com.miniproject.minipos.Models.UserModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static xdroid.toaster.Toaster.toast;
import static xdroid.toaster.Toaster.toastLong;

public class LoginActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginBtn;
    UserModel user = new UserModel();
    MessageModel message = new MessageModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);

        final OkHttpClient client = new OkHttpClient();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameEditText.getText().toString().trim().equalsIgnoreCase("")|| passwordEditText.getText().toString().trim().equalsIgnoreCase("")){
                    usernameEditText.setError("กรุณากรอกชื่อบัญชี");
                    passwordEditText.setError("กรุณากรอกรหัสผ่าน");
                }else{
                    user.getUser().setUsername(usernameEditText.getText().toString());
                    user.getUser().setPassword(passwordEditText.getText().toString());
                    String url = "http://192.168.1.36:8080/services/login";
                    String json = new Gson().toJson(user.getUser());
                    try {
                        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("กำลังโหลดข้อมูล...");
                        progressDialog.show();
                        post(url, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                progressDialog.dismiss();
                                toast("ไม่สามารถเชื่อมต่อระบบได้");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseStr = response.body().string();
                                user = new UserModel(responseStr);
                                if(user.getUser().getId()!=0) {
                                    toastLong("Welcome : " + user.getUser().getFirstName());
                                }else{
                                    message = new MessageModel(responseStr);
                                    toastLong(message.getMessage().getMessage());
                                }
                                progressDialog.dismiss();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            Call post(String url, String json, Callback callback) throws IOException {
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(callback);
                return call;
            }

        });


    }
}
