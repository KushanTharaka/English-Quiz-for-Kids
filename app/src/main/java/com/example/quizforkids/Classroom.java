package com.example.quizforkids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;

public class Classroom extends AppCompatActivity {

    private Button signout;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        signout = findViewById(R.id.signout1);
        graphView = findViewById(R.id.graph);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        User_Details userdetails = com.example.quizforkids.User_Details.getInstance();
        fStore.collection("Results")
                .document(userdetails.getData())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {
                                long quizNo = document.getLong("NoOfQuizzes");
                                Log.d("TAG", String.valueOf(quizNo));

                                if(quizNo != 0)
                                {
                                    //DataPoint[] dp = new DataPoint[(int) quizNo];
                                    //LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                                    series = new LineGraphSeries<DataPoint>();
                                    double y;
                                    double x = 0;
                                    int j = 1;
                                    Map<String, Object> quizavg = (Map<String, Object>) document.getData().get("QuizAvg");
                                    for(int i = 1; i<=(int) quizNo; i++)
                                    {
                                        String nameField = ("quiz"+j);
                                        for (Map.Entry<String, Object> entry : quizavg.entrySet())
                                        {
                                            if(entry.getKey().equals(nameField))
                                            {
                                                //Map<String, Object> fieldName = (Map<String, Object>) entry.getValue();
                                                long quizmark = (long) quizavg.get("quiz"+j);
                                                //long quizmark = (long) entry.getValue();
                                                //Log.d("TAG", String.valueOf(quizmark));
                                                x++;
                                                y = quizmark;
                                                series.appendData(new DataPoint(x, y), true, (int) quizNo);
                                            }
                                        }
                                        j++;


//                                        long quizmark = (long) quizavg.get("quiz"+j);
//                                        Log.d("TAG", String.valueOf(quizmark));
//                                        x++;
//                                        y = quizmark;
//                                        series.appendData(new DataPoint(x, y), true, (int) quizNo);
//                                        j++;

                                    }
                                    series.setThickness(8);
                                    series.setDrawDataPoints(true);
                                    series.setDataPointsRadius(20);
                                    series.setDrawBackground(true);
                                    series.setBackgroundColor(Color.argb(50, 95, 226, 156));
                                    graphView.getViewport().setXAxisBoundsManual(true);
                                    graphView.getViewport().setMinX(0);
                                    graphView.getViewport().setMaxX((int) quizNo);
                                    graphView.getViewport().setYAxisBoundsManual(true);
                                    graphView.getViewport().setMinY(0);
                                    graphView.getViewport().setMaxY(100);
                                    //graphView.getViewport().setScrollable(true);
                                    graphView.getViewport().setScalable(true);
                                    graphView.getViewport().setScalableY(true);
                                    graphView.addSeries(series);
                                }
                                else
                                {

                                }

                            }
                            else
                            {
                                Log.d("TAG", "document does not exist");
                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Log.d("TAG", "Error");
                        }
                    }
                });







        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

            }

        });
    }

//    private DataPoint[] getDataPoint() {
//        DataPoint[] dp =new DataPoint[]{
//                new DataPoint(1,95),
//                new DataPoint(2,40),
//                new DataPoint(3,72),
//                new DataPoint(4,26),
//                new DataPoint(5,68),
//                new DataPoint(6,51),
//                new DataPoint(7,18),
//                new DataPoint(8,67),
//                new DataPoint(9,80),
//                new DataPoint(10,44)
//        };
//        return(dp);
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
////        DocumentReference df = fStore.collection("Results").document("RpD6Mko4pQWillHejbS89SYABpG2");
////        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if (task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
//////                    DataPoint[] dp = new DataPoint[Integer.parseInt(document.getString("NoOfQuizzes"))];
//////                    int index = 0;
////
//////                    Map<String, Object> quiz = document.getData();
//////                    for (Map.Entry<String, Object> entry : quiz.entrySet()) {
//////                        if (entry.getKey().equals("QuizzAvg")) {
//////                            Map<String, Object> avgval = (Map<String, Object>) entry.getValue();
//////                            for (Map.Entry<String, Object> e : avgval.entrySet()) {
//////                                Log.d("TAG", String.valueOf(avgval));
//////                            }
//////                            for()
//////                            {
//////
//////                            }
//////
//////                        }
//////                    }
////                    String quizNo = document.getString("NoOfQuizzes");
////        if(quizNo != 0)
////        {
////            //int quizno = Integer.parseInt(quizNo);
////            Log.d("TAG", String.valueOf(quizNo));
////        }
////        else
////        {
////            Log.d("TAG", "Null value");
////        }
////
//////                    int quizno = Integer.parseInt(quizNo);
//////                    int j = 1;
//////                    Map<String, Object> quizavg = (Map<String, Object>) document.getData().get("QuizzAvg");
//////                    for(int i = 0; i<=quizno; i++)
//////                    {
//////                        String quizmark = (String) quizavg.get("Address"+j);
//////                        Log.d("TAG", quizmark);
//////                        j++;
//////                    }
////
////
////                } else {
////
////                }
////            }
////        });
//
//
//
//
//    }
}