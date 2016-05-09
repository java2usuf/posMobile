package com.ahmed.usuf.billingdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


        //DUMMY CREDENTIALS
        private String[] credentials={"ahmed","1234"};
        private android.os.Handler handler;
        private EditText user_ed,pass_ed;
        private TextView safire;
        MainActivity m=new MainActivity();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            disableKeyboard();




            user_ed=(EditText)findViewById(R.id.username);
            pass_ed=(EditText)findViewById(R.id.password);
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
            if (!validateName()) {
                return;
            }

            if (!validatePassword()) {
                return;
            }
            String user=user_ed.getText().toString(),pass=pass_ed.getText().toString();
            if(user.equals(credentials[0].toString())&pass.equals(credentials[1].toString())){
                Intent i=new Intent(this,MainActivity.class);
                startActivity(i);
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

/*private class MyTextWatcher implements TextWatcher {

            private View view;

            private MyTextWatcher(View view) {
                this.view = view;
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (view.getId()) {
                    case R.id.username:
                        validateName();
                        break;
                    case R.id.password:
                        validatePassword();
                        break;

                }
            }
        }*/