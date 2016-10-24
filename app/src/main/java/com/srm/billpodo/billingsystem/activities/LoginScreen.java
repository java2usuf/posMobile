package com.srm.billpodo.billingsystem.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.srm.billpodo.billingsystem.MyService;
import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.singleton.AppController;
import com.srm.billpodo.billingsystem.util.AppConstants;
import com.srm.billpodo.billingsystem.util.Login;
import com.srm.billpodo.billingsystem.util.ShowMsg;
import com.srm.billpodo.billingsystem.util.SystemConfig;
import com.srm.billpodo.billingsystem.util.Utility;

import static com.srm.billpodo.billingsystem.singleton.AppController.getInstance;

public class LoginScreen extends AppCompatActivity implements ReceiveListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private android.os.Handler handler;
    private EditText user_ed, pass_ed;
    HomeScreen m = new HomeScreen();
    private FilterOption mFilterOption = null;
    Context context=this;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getApplicationContext(), Utility.getBrandProperty(R.string.ad_app_id));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        setContentView(R.layout.activity_login);
        user_ed = (EditText) findViewById(R.id.username);

        if (AppController.getInstance().getSharedpreferences().contains(AppConstants.LASTUSEDUSERNAME)){
            user_ed.setText(AppController.getInstance().getSharedpreferences().getString(AppConstants.LASTUSEDUSERNAME,""));
        }

        pass_ed = (EditText) findViewById(R.id.password);
        startService();
        if(checkPermission()){
            requestPermission();
        }
        AppCompatButton button=(AppCompatButton)findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        messageHandler.sendEmptyMessage(0);
                    }
                }.start();
            }
        });
    }

    void startService() {
        i = new Intent(this, MyService.class);
        startService(i);
    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH);
        return result == PackageManager.PERMISSION_GRANTED || result2 == PackageManager.PERMISSION_DENIED;
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.BLUETOOTH_ADMIN)){

            Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permission Granted, Now you can access location data.",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    Log.i("Printer Name",deviceInfo.getDeviceName());
                    Log.i("Target", deviceInfo.getTarget());
                    Toast.makeText(getApplicationContext(), "Application Connecting to Printer IP : "+deviceInfo.getIpAddress(), Toast.LENGTH_LONG).show();
                    SystemConfig.getInstance().setIp(deviceInfo.getIpAddress());
                }
            });
        }
    };




    public int getWidth() {
        Display manager = getWindowManager().getDefaultDisplay();
        return manager.getWidth();
    }

    public int getHeight() {
        Display manager = getWindowManager().getDefaultDisplay();
        return manager.getHeight();
    }

    private android.os.Handler messageHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                submitForm();
            }

        }
    };

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        String user = user_ed.getText().toString();
        String pass = pass_ed.getText().toString();

        Login loginAs = Login.fromString(user, pass);

        switch(loginAs){
            case MENCITY:
                Log.i("Sing - MENCITY User", user);
                break;
            case SAFIRE:
                Log.i("Sing - SAFIRE User", user);
                break;
            case US_FASHION:
                Log.i("Sing -  US_FASHION User", user);
                break;
            case US_TAILOR:
                Log.i("Sing -  US_TAILOR User", user);
                break;
            case LOGIN_FAILED:
                Log.i("Login Failed", user);
                break;
        }

        if (Login.LOGIN_FAILED != loginAs) {
            getInstance().setLogin(loginAs);
            Intent i = new Intent(this, HomeScreen.class);
            loginAs.saveUserName();
            startActivity(i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateName() {
        if (user_ed.getText().toString().trim().isEmpty()) {
            user_ed.setError(AppConstants.LOGIN_VALIDATION_MESSAGE);
            return false;
        } else {
            user_ed.setError(null);
        }

        return true;
    }


    private boolean validatePassword() {
        if (pass_ed.getText().toString().trim().isEmpty()) {
            pass_ed.setError("Password Field is Empty");
            //requestFocus(pass_ed);
            return false;
        } else {
            pass_ed.setError(null);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void enableKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void disableKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onResume() {
        AppController.getInstance().onActivityResumed(this);
        super.onResume();
        mFilterOption = new FilterOption();
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
        try {
            Discovery.start(this, mFilterOption, mDiscoveryListener);
            Thread.currentThread().sleep(1000);
            Discovery.stop();
        }
        catch (Exception e) {
            ShowMsg.showException(e, "start", this);
        }
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(i);
    }
}
