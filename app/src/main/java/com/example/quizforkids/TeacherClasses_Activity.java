package com.example.quizforkids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherClasses_Activity extends AppCompatActivity {

    RecyclerView recycler_view;
    private Button addClass, dialog_btnaddnewclass;
    //public static List<String> classlist = new ArrayList<>();
    public static List<TeacherClassName_Model> classlist = new ArrayList<>();
    private FirebaseFirestore fStore;
    private Dialog progressDialog, addnewclass_dialog;
    private TextView dialog_text;
    private EditText dialog_etaddnewclass;
    long classNum;
    private TeacherClassAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_teacher_classes);

        recycler_view = findViewById(R.id.TeacherClassRecycler);
        addClass = findViewById(R.id.btnCreateClasstc);

        progressDialog = new Dialog(TeacherClasses_Activity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_text = progressDialog.findViewById(R.id.dialog_text);
        dialog_text.setText("Loading...");

        addnewclass_dialog = new Dialog(TeacherClasses_Activity.this);
        addnewclass_dialog.setContentView(R.layout.add_newclass_dialog);
        addnewclass_dialog.setCancelable(false);
        addnewclass_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_etaddnewclass = addnewclass_dialog.findViewById(R.id.etClassNameAdd);
        dialog_btnaddnewclass = addnewclass_dialog.findViewById(R.id.btnClassNameAdd);

        fStore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);

        TeacherClassAdapter adapter = new TeacherClassAdapter(classlist);
        recycler_view.setAdapter(adapter);

        loadClasses();

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_etaddnewclass.getText().clear();
                addnewclass_dialog.show();

            }
        });

        dialog_btnaddnewclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dialog_etaddnewclass.getText().toString().isEmpty())
                {
                    dialog_etaddnewclass.setError("Class Name Empty!");
                    dialog_etaddnewclass.requestFocus();
                    return;
                }
                else
                    {
                        addNewClass(dialog_etaddnewclass.getText().toString());
                    }
            }
        });
    }

    private void addNewClass(String className) {

        addnewclass_dialog.dismiss();
        progressDialog.show();

        User_Details userdetails = com.example.quizforkids.User_Details.getInstance();
        DocumentReference rf = fStore.collection("Teachers").document(userdetails.getData());
        rf.update("NoOfClasses", FieldValue.increment(1));

        Map<String, Object> NameCL = new HashMap<>();
        NameCL.put("Name", dialog_etaddnewclass.getText().toString());

        fStore.collection("Teachers").document(userdetails.getData())
                .collection("classes").document("class"+classNum)
                .set(NameCL, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(TeacherClasses_Activity.this, "New Class Added!", Toast.LENGTH_SHORT).show();
                        classlist.add(new TeacherClassName_Model("class"+classNum, dialog_etaddnewclass.getText().toString()));
                        adapter.notifyItemInserted(classlist.size());
                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception er) {

                        Toast.makeText(TeacherClasses_Activity.this, er.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }

    private void loadClasses()
    {
        classlist.clear();

        progressDialog.show();

        User_Details userdetails = com.example.quizforkids.User_Details.getInstance();
        fStore.collection("Teachers")
                .document(userdetails.getData())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();

                            if(doc.exists())
                            {
                                classNum = (long)doc.get("NoOfClasses");
                                long count = (long)doc.get("NoOfClasses");

                                if(count != 0)
                                {

                                    for(int i=1; i <= count; i++)
                                    {
//                                        String catName = doc.getString("CAT" + String.valueOf(i) + "_NAME");
//                                        String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

//                                        classlist.add(new CategoryModel(catID,catName));


                                        int finalI = i;
                                        fStore.collection("Teachers")
                                                .document(userdetails.getData())
                                                .collection("classes")
                                                .document("class"+i)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                        if(task.isSuccessful())
                                                        {
                                                            DocumentSnapshot doc = task.getResult();

                                                            if(doc.exists())
                                                            {
                                                                adapter = new TeacherClassAdapter(classlist);
                                                                recycler_view.setAdapter(adapter);

                                                                String clName = doc.getString("Name");
                                                                Log.d("TAG", clName);
                                                                classlist.add(new TeacherClassName_Model("class"+ finalI, clName));

                                                                progressDialog.dismiss();

                                                            }
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(TeacherClasses_Activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });
                                    }
                                }
                                else
                                {

                                }
                            }
                            else
                            {
                                Toast.makeText(TeacherClasses_Activity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                //finish();
                            }

                        }
                        else
                        {
                            Toast.makeText(TeacherClasses_Activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

}