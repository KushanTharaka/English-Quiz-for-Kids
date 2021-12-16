package com.example.quizforkids;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TeacherClassAdapter extends RecyclerView.Adapter<TeacherClassAdapter.ViewHolder>{

    private List<TeacherClassName_Model> classlist;

    public TeacherClassAdapter(List<TeacherClassName_Model> classlist) {
        this.classlist = classlist;
    }


    @NonNull
    @Override
    public TeacherClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_class_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherClassAdapter.ViewHolder holder, int position) {
        String classname = classlist.get(position).getClassName();
        String docname = classlist.get(position).getDocName();

        holder.setData(classname, docname, position, this);
    }

    @Override
    public int getItemCount() {
        return classlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView NameClass;
        private ImageView btnDel;
        private Dialog progressDialog;
        private TextView dialog_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            NameClass = itemView.findViewById(R.id.tvClasstci);
            btnDel = itemView.findViewById(R.id.ivClassDeltci);

            progressDialog = new Dialog(itemView.getContext());
            progressDialog.setContentView(R.layout.dialog_layout);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog_text = progressDialog.findViewById(R.id.dialog_text);
            dialog_text.setText("Deleting...");

        }

        private void setData(String classname, String Docname, int position, TeacherClassAdapter adapter)
        {
            NameClass.setText(classname);

            btnDel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    AlertDialog dialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Class")
                            .setMessage("Do you want to delete this class?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteClass(Docname, itemView.getContext(), position, adapter);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

        }

        private void deleteClass(String Docname, Context context, final int pos, TeacherClassAdapter adapter)
        {
            progressDialog.show();
            Log.d("TAG", Docname);

            User_Details userdetails = com.example.quizforkids.User_Details.getInstance();
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            fStore.collection("Teachers").document(userdetails.getData())
                    .collection("classes").document(Docname)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            TeacherClasses_Activity.classlist.remove(pos);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, "Class Deleted Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(context, "Failed to Delete the Class", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });


        }

    }
}
