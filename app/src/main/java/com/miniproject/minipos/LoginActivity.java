package com.miniproject.minipos;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.miniproject.minipos.Models.MessageModel;
import com.miniproject.minipos.Models.UserModel;
import com.miniproject.minipos.StorageHelper.StorageManager;
import com.miniproject.minipos.WSHelper.WSHelper;
import com.miniproject.minipos.WSManager.LoginController;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;

import static xdroid.toaster.Toaster.toast;
import static xdroid.toaster.Toaster.toastLong;

public class LoginActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private StorageManager sm = new StorageManager(LoginActivity.this);
    EditText usernameEditText;
    EditText passwordEditText;
    ImageButton WSConfigImageBtn;
    Button loginBtn;
    UserModel user = new UserModel();
    MessageModel message = new MessageModel();
    Pattern pattern;
    Matcher matcher;
    String Port_PATTERN = "^[1-9]{1}[0-9]{3}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pattern = Pattern.compile(Port_PATTERN);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        WSConfigImageBtn = findViewById(R.id.WSConfigImageBtn);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEditText.getText().toString().trim().equalsIgnoreCase("") || passwordEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    usernameEditText.setError("กรุณากรอกชื่อบัญชี");
                    passwordEditText.setError("กรุณากรอกรหัสผ่าน");
                } else {
                    user.getUser().setUsername(usernameEditText.getText().toString());
                    user.getUser().setPassword(passwordEditText.getText().toString());
                    try{
                        LoginController loginController = new LoginController(LoginActivity.this);
                        String jsonUserModelObj = new Gson().toJson(user.getUser());
                        loginController.verifyLogin(jsonUserModelObj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

        });

        WSConfigImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configWS();
            }
        });

    }
    public boolean validatePort(final String serverPort){
        matcher = pattern.matcher(serverPort);
        return matcher.matches();
    }
    public void configWS(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.wsconfig_dialog, null);
        final EditText serverIP_EditText = (EditText) mView.findViewById(R.id.serverIP_EditText);
        final EditText serverPort_EditText = (EditText) mView.findViewById(R.id.serverPort_EditText);
        final Button setupBtn = (Button) mView.findViewById(R.id.setupBtn);
        String[] serverPath = sm.readServerConfig();
        if(serverPath!=null){
            serverIP_EditText.setText(serverPath[0].toString());
            serverPort_EditText.setText(serverPath[1].toString());
        }
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setContentView(R.layout.wsconfig_dialog);
        dialog.setTitle("ตั้งค่าการเชื่อมต่อระบบ");

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverIP = serverIP_EditText.getText().toString().trim();
                String serverPort = serverPort_EditText.getText().toString().trim();
                if(serverIP.trim().equalsIgnoreCase("")||serverPort.trim().equalsIgnoreCase("")){
                    serverIP_EditText.setError("กรุณากรอก Server IP");
                    serverPort_EditText.setError("กรุณากรอก Server Port");
                }else if(validatePort(serverPort)==false){
                    serverPort_EditText.setError("รูปแบบ Port ผิด");
                }else{
                  WSHelper wsHelper = new WSHelper(LoginActivity.this);
                    try {
                        wsHelper.testConnector(serverIP,serverPort);
                        sm.writeServerConfig(serverIP,serverPort);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.show();
    }

}
