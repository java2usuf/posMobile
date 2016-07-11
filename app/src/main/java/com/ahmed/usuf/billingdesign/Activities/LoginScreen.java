package com.ahmed.usuf.billingdesign.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmed.usuf.billingdesign.utili.AppConstants;
import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.singleton.AppController;

public class LoginScreen extends AppCompatActivity {

        private String[] credentials={AppConstants.DEFAULT_ADMIN, AppConstants.DEFAULT_PASSWORD};
        private android.os.Handler handler;
        private EditText user_ed,pass_ed;
        HomeScreen m=new HomeScreen();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            user_ed=(EditText)findViewById(R.id.username);
            user_ed.setText(AppConstants.DEFAULT_ADMIN);
            pass_ed=(EditText)findViewById(R.id.password);
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

        private void enableKeyboard(){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        private void disableKeyboard(){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }


    @Override
    protected void onResume() {
        AppController.getInstance().onActivityResumed(this);

        super.onResume();
        pass_ed.setFocusable(true);
    }
}

