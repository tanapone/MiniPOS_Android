package com.miniproject.minipos.WSManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.miniproject.minipos.LoginActivity;
import com.miniproject.minipos.MainActivity;
import com.miniproject.minipos.Models.UserModel;
import com.miniproject.minipos.R;
import com.miniproject.minipos.WSHelper.WSHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static xdroid.toaster.Toaster.toast;

public class LoginManager {
    Context context;

    public LoginManager(Context context){
        this.context = context;
    }

    public void verifyLogin(String jsonUserModelObj) throws IOException {
        final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("กำลังโหลดข้อมูล...");
            progressDialog.show();
            WSHelper wsHelper = new WSHelper(context);
            wsHelper.post(context.getString(R.string.login_url), jsonUserModelObj, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast("ไม่สามารถเชื่อมต่อระบบได้");
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                UserModel userModel = new UserModel(responseStr);
                if(userModel.getUser().getId()!=0){
                    toast("ยินดีต้อนรับคุณ "+userModel.getUser().getFirstName());
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("jsonUserModelObj",responseStr);
                    context.startActivity(intent);
                }else{
                    toast("ชื่อบัญชี หรือ รหัสผ่านผิดพลาด");
                }

                progressDialog.dismiss();
            }
        });
    }

}
