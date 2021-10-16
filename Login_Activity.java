package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {

    Button login;
    EditText email, password;
    TextView forgot_password, sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etEmailLP);
        password = findViewById(R.id.etPasswordLP);
        login = findViewById(R.id.btnLoginLP);
        forgot_password = findViewById(R.id.tcForgotPasswordLP);
        sign_up = findViewById(R.id.tvSignUpLP);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),SignUp_Activity.class);
                startActivity(in);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(email.getText().toString().isEmpty()){
                    email.setError("Enter Email Address !");
                    return;
                }else{
                    email.setError(null);
                }

                if(password.getText().toString().isEmpty()){
                    password.setError("Enter Password !");
                    return;
                }else{
                    password.setError(null);
                }

                if(validateData())
                {
                    login();
                }

            }

        });

    }

    private boolean validateData()
    {

        if(email.getText().toString().isEmpty())
        {
            email.setError("enter email");
            return false;
        }
        if(password.getText().toString().isEmpty())
        {
            password.setError("enter password");
            return false;
        }

        return true;
    }

    private void login()
    {

    }

}