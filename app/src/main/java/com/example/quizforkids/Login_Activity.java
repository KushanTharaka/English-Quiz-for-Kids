package com.example.quizforkids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login_Activity extends AppCompatActivity {

    Button login;
    EditText email, password;
    TextView forgot_password, sign_up;
    Dialog progressDialog;
    TextView dialog_text;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

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

        progressDialog = new Dialog(Login_Activity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_text = progressDialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Signing In...");

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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
            email.setError("Enter Email Address !");
            email.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Enter Valid Email Address!");
            email.requestFocus();
            return false;
        }
        if(password.getText().toString().isEmpty())
        {
            password.setError("Enter Password !");
            email.requestFocus();
            return false;
        }

        return true;
    }

    private void login()
    {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            User_Details userdetails = com.example.quizforkids.User_Details.getInstance();
                            userdetails.setData(email.getText().toString().trim());

                            progressDialog.dismiss();
                            Toast.makeText(Login_Activity.this, "Login Successful !", Toast.LENGTH_SHORT).show();

                            DocumentReference df = fStore.collection("Users").document(mAuth.getCurrentUser().getUid());
                            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        String acctype = document.getString("Account_Type");

                                        if (acctype.equals("Teacher")) {

                                            Intent i = new Intent(Login_Activity.this, TeacherMainMenu_Activity.class);
                                            startActivity(i);
                                            Login_Activity.this.finish();

                                        } else {
                                            Intent i = new Intent(Login_Activity.this, StudentClasses.class);
                                            startActivity(i);
                                            Login_Activity.this.finish();
                                        }

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });
        };
    }