package com.ahmed.usuf.billingdesign.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.Volley.AppController;
import com.ahmed.usuf.billingdesign.utili.HttpClient;
import com.ahmed.usuf.billingdesign.utili.SystemConfig;
import com.google.common.base.Strings;

public class LoginScreen extends AppCompatActivity {

        //DUMMY CREDENTIALS
        private String[] credentials={"admin","1234"};
        private android.os.Handler handler;
        private EditText user_ed,pass_ed;
        private TextView safire;
        HomeScreen m=new HomeScreen();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            disableKeyboard();
            user_ed=(EditText)findViewById(R.id.username);
            user_ed.setText("admin");
            pass_ed=(EditText)findViewById(R.id.password);
            pass_ed.setFocusable(true);
            user_ed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableKeyboard();
                }
            });
            pass_ed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableKeyboard();
                }
            });

        }

        public int getWidth() {
            Display manager=getWindowManager().getDefaultDisplay();
            return manager.getWidth();
        }

        public int getHeight() {
            Display manager=getWindowManager().getDefaultDisplay();
            return manager.getHeight();
        }

        public void login(View view){
            new Thread() {
                public void run() {
                    messageHandler.sendEmptyMessage(0);
                }
            }.start();
        }

        private android.os.Handler messageHandler = new android.os.Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0)
                {
                    submitForm();
                }

            }
        };

        /**
         * Validating form
         */
        private void submitForm() {


            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String[] tab_names = getResources().getStringArray(R.array.printer_ip_address);
                        for(String ip : tab_names){
                            String response = new HttpClient().sendMessage("http://"+ip,"");


                            Log.i("PRINTER ** CONNECTING ...", "IP ADDRESS *****  "+ ip);

                            if(response.contains("EPSON TMNet WebConfig Ver.1.00")){
                                Log.i("PRINTER ** CONNECTED ********************", "IP ADDRESS *****  "+ ip);
                                SystemConfig.getInstance().setIp(ip);
                                break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();




            if (!validateName()) {
                return;
            }

            if (!validatePassword()) {
                return;
            }
            String user=user_ed.getText().toString(),pass=pass_ed.getText().toString();
            if(user.equals(credentials[0].toString())&pass.equals(credentials[1].toString())){
                Intent i=new Intent(this,HomeScreen.class);
                startActivity(i);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Access not Granted", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean validateName() {
            if (user_ed.getText().toString().trim().isEmpty()) {
                user_ed.setError("This Field should not be Empty");
                //  requestFocus(user_ed);
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

        private void enableKeyboard(){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        private void disableKeyboard(){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }


    }

