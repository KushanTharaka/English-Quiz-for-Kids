package com.example.quizforkids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp_Activity extends AppCompatActivity {

    Button sign_up;
    EditText fullname, email, password, confirmPassword;
    ImageView backbtn;
    String str_name, str_email, str_password, str_confirmpassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        fullname = findViewById(R.id.etFullnameSU);
        email = findViewById(R.id.etEmailSU);
        password = findViewById(R.id.etPasswordSU);
        confirmPassword = findViewById(R.id.etConfirmPassSU);
        backbtn = findViewById(R.id.btnBackSU);
        sign_up = findViewById(R.id.btnSignupSU);

        mAuth = FirebaseAuth.getInstance();


        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }

        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateData())
                {
                    signup_new_user();
                }

            }

        });
    }

    private boolean validateData() {

        str_name = fullname.getText().toString();
        str_email = email.getText().toString();
        str_confirmpassword = confirmPassword.getText().toString();
        str_password = password.getText().toString();

        if(fullname.getText().toString().isEmpty()){
            fullname.setError("Enter Full Name!");
            return false;
//            else --> fullname.setError(null);
        }

        if(email.getText().toString().isEmpty()){
            email.setError("Enter Email!");
            return false;
        }

        if(password.getText().toString().isEmpty()){
            password.setError("Enter Email Address !");
            return false;
        }

        if(confirmPassword.getText().toString().isEmpty()){
            confirmPassword.setError("Enter Password !");
            return false;
        }

//        if(!password.equals(confirmPassword)){
//            Toast.makeText(SignUp_Activity.this, "Passwords do not Match!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    private void signup_new_user() {

        mAuth.createUserWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignUp_Activity.this, "Signup Successfull!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),Login_Activity.class);
                            startActivity(i);

                        } else {

                            Toast.makeText(SignUp_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}