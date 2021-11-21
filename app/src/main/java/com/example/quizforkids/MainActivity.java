package com.example.quizforkids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        new Thread() {

            @Override
            public void run() {

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    DocumentReference df = fStore.collection("Users").document(mAuth.getCurrentUser().getUid());
                    df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String acctype = document.getString("Account_Type");
                                String email = document.getString("Email");

                                User_Details userdetails = com.example.quizforkids.User_Details.getInstance();
                                userdetails.setData(email);

                                if (acctype.equals("Teacher")) {
                                    Intent i = new Intent(MainActivity.this, Classroom.class);
                                    startActivity(i);
                                    MainActivity.this.finish();
                                } else {
                                    Intent i = new Intent(MainActivity.this, StudentClasses.class);
                                    startActivity(i);
                                    MainActivity.this.finish();
                                }

                            } else {
                                Intent i = new Intent(MainActivity.this, Login_Activity.class);
                                startActivity(i);
                                MainActivity.this.finish();
                            }
                        }
                    });
                } else {
                    Intent i = new Intent(MainActivity.this, Login_Activity.class);
                    startActivity(i);
                    MainActivity.this.finish();
                }
            }

        }.start();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            Intent i = new Intent(MainActivity.this, Login_Activity.class);
//            startActivity(i);
//        }
//    }
}