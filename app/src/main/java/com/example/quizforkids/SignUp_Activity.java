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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp_Activity extends AppCompatActivity {

    Button sign_up;
    EditText fullname, email, password;
    ImageView backbtn;
    String str_name, str_email, str_password;
    RadioButton teacher, student;
    String accType;
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

        setContentView(R.layout.activity_sign_up);

        fullname = findViewById(R.id.etFullnameSU);
        email = findViewById(R.id.etEmailSU);
        teacher = findViewById(R.id.rbTeacherSU);
        student = findViewById(R.id.rbStudentSU);
        password = findViewById(R.id.etPasswordSU);
        backbtn = findViewById(R.id.btnBackSU);
        sign_up = findViewById(R.id.btnSignupSU);

        progressDialog = new Dialog(SignUp_Activity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_text = progressDialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Registering User...");

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


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
                    checkAccType();
                    signup_new_user();
                }

            }

        });
    }

    private void checkAccType() {

        if(teacher.isChecked()){
            accType = "Teacher";
        }else{
            accType = "Student";
        }

    }

    private boolean validateData() {

        str_name = fullname.getText().toString().trim();
        str_email = email.getText().toString().trim();
        str_password = password.getText().toString().trim();

        if(str_name.isEmpty()){
            fullname.setError("Enter Full Name!");
            fullname.requestFocus();
            return false;
//            else --> fullname.setError(null);
        }

        if(str_email.isEmpty()){
            email.setError("Enter Email Address!");
            email.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            email.setError("Enter Valid Email Address!");
            email.requestFocus();
            return false;
        }

        if(str_password.isEmpty()){
            password.setError("Enter Password!");
            password.requestFocus();
            return false;
        }

        if(str_password.length() < 6){
            password.setError("Password is too short!");
            password.requestFocus();
            return false;
        }


        return true;
    }

    private void signup_new_user() {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String, Object> UserInfo = new HashMap<>();
                            UserInfo.put("Full_Name", str_name);
                            UserInfo.put("Email", str_email);
                            UserInfo.put("Account_Type", accType);

                            fStore.collection("Users").document(user.getUid())
                                    .set(UserInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            if(accType.equals("Teacher"))
                                            {
                                                Map<String, Object> teacher = new HashMap<>();
                                                teacher.put("NoOfClasses", 0);

                                                fStore.collection("Teachers").document(str_email)
                                                        .set(teacher)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Toast.makeText(SignUp_Activity.this, "Signup Successful !", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                FirebaseAuth.getInstance().signOut();
                                                                Intent i = new Intent(getApplicationContext(),Login_Activity.class);
                                                                startActivity(i);
                                                                finish();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception er) {

                                                                    fStore.collection("Users").document(user.getUid())
                                                                            .delete()
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Log.d("Successful", "DocumentSnapshot successfully deleted!");
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.w("Unsuccessful", "Error deleting document", e);
                                                                                }
                                                                            });

                                                                    user.delete()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        Log.d("Sign up Failed", "User account deleted.");
                                                                                    }
                                                                                }
                                                                            });

                                                                }
                                                            });
                                            }
//                                            else
//                                            {
//
//                                            }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Sign up Failed", "User account deleted.");
                                                            }
                                                        }
                                                    });

                                        }
                                    });

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp_Activity.this, "Failed to Sign up.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}