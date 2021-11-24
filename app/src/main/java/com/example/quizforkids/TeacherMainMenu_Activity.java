package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class TeacherMainMenu_Activity extends AppCompatActivity {

    Button classesbtn, classPerformancebtn, individualPerformancebtn, profilebtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_teacher_main_menu);

        mAuth = FirebaseAuth.getInstance();
        classesbtn = findViewById(R.id.btnClassestmm);
        profilebtn = findViewById(R.id.btnProfiletmm);

        classesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),TeacherClasses_Activity.class);
                startActivity(i);

            }

        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(i);

            }

        });


    }
}