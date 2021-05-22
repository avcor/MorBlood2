package com.unisparc.morblood;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class donor_activity extends AppCompatActivity {

    private String donor_id;



    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_activity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dailouge_builder();
    }

    public void dailouge_builder(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder1.setView(edittext);
        builder1.setTitle("confirmation");
        builder1.setMessage("enter phone no");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        get_fire_info(edittext.getText().toString());
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void get_fire_info(String donor_phoneno){

        FirebaseFirestore.getInstance()
                .collection("donor")
                .whereEqualTo("phoneno",donor_phoneno)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() != 1){
                            Toast.makeText(getApplicationContext(),"check number", Toast.LENGTH_LONG).show();
                            dailouge_builder();
                        }
                        else {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                donor_id = snapshot.getId();
                                //Toast.makeText(getApplicationContext(),donor_id,Toast.LENGTH_LONG).show();
                                get_request();
                            }
                        }
                    }
                });


    }

    public void get_request(){
        FirebaseFirestore.getInstance()
                .collection("request")
                .whereEqualTo("donor_id",donor_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        RecyclerView recycle;
                        List<user_list_item> list_item;
                        myAdapter madapter2;
                        recycle = findViewById(R.id.recycle);
                        recycle.setHasFixedSize(true);
                        recycle.setLayoutManager(new LinearLayoutManager(donor_activity.this));
                        list_item = new ArrayList<>();

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            // -> recipient name -> snapshot.getString("recipient_name")
                            // recipient city -> snapshot.getString("recipient_city")
                            // phone no -> snapshot.getString("recipient_phoneno"
                            Toast.makeText(getApplicationContext(),snapshot.getString("recipient_name"), Toast.LENGTH_LONG).show();
                            user_list_item xyz = new user_list_item("recipient name : " + snapshot.getString("recipient_name")
                                    ,"recipient city : " + snapshot.getString("recipient_city")
                                    ,"recipient phone no. : " +  snapshot.getString("recipient_phoneno")
                                    ,null
                                    ,null
                                    ,null);
                            list_item.add(xyz);
                        }
                        madapter2=new myAdapter(list_item,donor_activity.this);
                        recycle.setAdapter(madapter2);
                    }
                });
    }

    @Override
    public void onBackPressed() {



        //super.onBackPressed();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warning!!!");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_warning_24);
        alertDialogBuilder.setMessage("Where you want to go? \n All your data will be lost!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }


}