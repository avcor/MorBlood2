package com.unisparc.morblood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class recipient_activity extends AppCompatActivity {

    private Button button_search;
    AutoCompleteTextView et_tehsil,et_bloodgrp;
    private RecyclerView recyclerView;
    private List<user_list_item> list_items;
    private myAdapter madapter;
    private ChipNavigationBar chipNavigationBar;
    String city;

    private String tehsil;
    private String bloodgrp;

    final Query firestore_blood_query =  FirebaseFirestore.getInstance().collection("donor").whereEqualTo("blood",true);
    final Query firestore_plasma_query =  FirebaseFirestore.getInstance().collection("donor").whereEqualTo("plasma",true);

    private ArrayList<Object> id_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_activity);

        Intent into = getIntent();
        city = into.getStringExtra("city");
        et_tehsil = findViewById(R.id.et_tehsil);
        et_bloodgrp = findViewById(R.id.et_bloodgrp);

        final String bloodtypes[] = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, bloodtypes);
        et_bloodgrp.setAdapter(bloodAdapter);

        String[] tehsils;
        ArrayAdapter<String> rec_adaptertehsil;

        switch (city) {
            case "SUKMA":
                tehsils = new String[]{"KONTA", "CHHINDGARH", "SUKMA"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "DANTEWADA":
                tehsils = new String[]{"DANTEWADA", "GEEDAM", "KATEKALYAN", "KUAKONDA"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "BASTAR":
                tehsils = new String[]{"BADE RAJPUR", "BAKAVAND", "BASTANAR", "BASTAR", "DARBHA", "FARASGAON", "JAGDALPUR", "LOHANDIGUDA", "TOKAPAL"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "KONDAGAON":
                tehsils = new String[]{"KONDAGAON", "MAKDI", "PHARASGAON", "KASHKAL", "BADERAJPUR"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "NARAYANPUR":
                tehsils = new String[]{"NARAYANPUR", "Orchha"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "KANKER":
                tehsils = new String[]{"kanker", "Charama", "Narharpur", "Bhanupratappur", "Durugkondal", "Antagarh"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "KAWARDHA":
                tehsils = new String[]{"KAWARDHA", "PANDARIYA", "BODLA", "SAHASPUR", "LOHARA"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "RAJNANDGAON":
                tehsils = new String[]{"Chhuikhadan", "Khairagarh", "Rajnandgaon", "Dongargarh", "Chhuriya", "Ambagarh Chowki", "Mohla", "Manpur", "Dongargoan"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "BALOD":
                tehsils = new String[]{"Gunderdehi", "Dondi", "Luhara", "Dondi", "Gurur", "Balod"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "DURG":
                tehsils = new String[]{"Durg", "Patan", "Dhamdha"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "DHAMTARI":
                tehsils = new String[]{"Dhamtari", "Kurud", "Nagri", "Magarlod"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "GARIYABAND":
                tehsils = new String[]{"Rajim", "Mainpur", "Chhura", "Bindranavagarh(Gariyaband)", "Deobhog"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "RAIPUR":
                tehsils = new String[]{"Raipur", "Arang", "Abhanpur", "Tilda", "Simga", "DHARSIVA"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "BALODA BAZAR":
                tehsils = new String[]{"Bhatapara", "Palari", "Bilaigarh", "Kasdol", "Baloda Bazar"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "MAHASAMUND":
                tehsils = new String[]{"Mahasamund", "Pithora", "Saraipali", "Bagbahra", "Basna"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "BILASPUR":
                tehsils = new String[]{"Belha", "Bilaspur", "Kota", "Takhatpur", "Masturi", "Lormi"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "BEMETARA":
                tehsils = new String[]{"Bemetara", "Berla", "Saja", "Thankhamhariya", "Nawagarh"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "BALRAMPUR":
                tehsils = new String[]{"Balrampur", "Tulsipur", "Utraula", "Lakhanpur"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "MARWAHI":
                tehsils = new String[]{"Marwahi", "Pendra Road", "Gorella", "Pendra"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "SURAJPUR":
                tehsils = new String[]{"Surajpur", "Ramanujnagar", "Premnagar"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "KABIRDHAM":
                tehsils = new String[]{"Pandariya", "Kawardha", "Bodla", "Sahaspur Lohara"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "BIJAPUR":
                tehsils = new String[]{"Bhairamgarh", "Bijapur", "Usur", "Bhopalpattnam"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "SARGUJA":
                tehsils = new String[]{"Ambikapur","Ramanujganj","Wadrafnagar","Pratappur","Bhaiyathan","Lundra"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "JASHPUR":
                tehsils = new String[]{"Pathalgaon", "Bagicha", "Farsabahar", "Jashpur", "Kunkuri", "Kansabel", "Manora", "Duldula"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "KORIA":
                tehsils = new String[]{"Baikunthpur", "Khadganva", "Manendragarh", "Bharatpur", "Sonhat", "Sitapur"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "RAIGARH":
                tehsils = new String[]{"Raigarh", "Sarangarh", "Udaipur (Dharamjaigarh)", "Baramkela", "Kharsia", "Pusour", "Lailunga", "Tamnar", "Gharghoda"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "JANJGHIR CHAMPA":
                tehsils = new String[]{"Champa", "Sakti", "Akaltara", "Pamgarh", "Dabhra", "Jaijaipur", "Nawagarh", "Janjgir", "Malkharoda", "Baloda"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "KORBA":
                tehsils = new String[]{"Korba", "Katghora", "Pali", "Poundi-Uproda", "Kartala"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
            case "MUNGELI":
                tehsils = new String[]{"MUNGELI", "PATHARIA", "Lormi"};
                rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                et_tehsil.setAdapter(rec_adaptertehsil);
                break;
        }

        button_search = findViewById(R.id.btn_search);

        et_bloodgrp = findViewById(R.id.et_bloodgrp);
        chipNavigationBar = findViewById(R.id.chip_nav);

        chipNavigationBar.setItemSelected(R.id.nav_blood,true);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (chipNavigationBar.getSelectedItemId()){
                    case R.id.nav_blood :
                        tehsil = et_tehsil.getText().toString();
                        bloodgrp = et_bloodgrp.getText().toString();
                        if(tehsil.equals("") && bloodgrp.equals("")){
                            firebase_read_once(firestore_blood_query);
                        }
                        else {
                            firebase_read_once(firestore_blood_query.whereEqualTo("bloodgrp",bloodgrp).whereEqualTo("tehsil",tehsil)) ;
                        }
                        break;

                    case R.id.nav_plasma :
                        tehsil = et_tehsil.getText().toString();
                        bloodgrp = et_bloodgrp.getText().toString();
                        if(tehsil.equals("") && bloodgrp.equals("")){
                            firebase_read_once(firestore_plasma_query);
                        }
                        else {
                            firebase_read_once(firestore_plasma_query.whereEqualTo("bloodgrp",bloodgrp).whereEqualTo("tehsil",tehsil));
                        }
                        break;
                }

            }
        });


    }


    public void firebase_read_once(Query firestore_query){
        firestore_query
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()) {
                         Toast.makeText(getApplicationContext(),"no data found", Toast.LENGTH_LONG).show();
                        }
                        else {
                            load_recycler(queryDocumentSnapshots);
                        }
                    }
                });
    }

    public void load_recycler(QuerySnapshot queryDocumentSnapshots){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list_items = new ArrayList<>();

        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
        for(DocumentSnapshot snap: documentSnapshots){
            //Toast.makeText(this,"lol",Toast.LENGTH_SHORT).show();
            user_list_item xyz = new user_list_item("name " + snap.getString("name")
                    ,"phoneno " + snap.getString("phoneno")
                    ,"state " + snap.getString("state")
                    ,"city " + snap.getString("city")
                    ,"tehsil " + snap.getString("tehsil")
                    ,"bloodgrp " + snap.getString("bloodgrp"));
            list_items.add(xyz);
            id_list.add(snap.getId());
        }
        madapter=new myAdapter(list_items,this);
        recyclerView.setAdapter(madapter);

        madapter_setclick();
    }

    public void madapter_setclick(){
        madapter.setOnItemClickListener(new myAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
//                Intent intent = new Intent(MainActivity.this, detail_info.class);
//                intent.putExtra("example item", list_items.get(position));
//                startActivity(intent);
                user_list_item ult = list_items.get(position);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(recipient_activity.this);
                final EditText edittext = new EditText(getApplicationContext());
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder1.setView(edittext);
                builder1.setTitle("confirmation");
                builder1.setMessage("donor " + ult.getName() + "\n" + ult.getBloodgrp() + "\nConfirm u r phone no. ");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(getApplicationContext(),id_list.get(position).toString(),Toast.LENGTH_LONG).show();
                                fire_request(edittext.getText().toString(), id_list.get(position).toString());
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }

    public void fire_request(String u_phoneno, final String donor_id){

        FirebaseFirestore.getInstance()
                .collection("recipient")
                .whereEqualTo("phoneno",u_phoneno)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size() != 1){
                            Toast.makeText(getApplicationContext(),"check number", Toast.LENGTH_LONG).show();
                        }
                        else {
                            for(DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()){

                                Map<String, Object> data = new HashMap<>();
                                data.put("recipient_name",snap.getString("name"));
                                data.put("recipient_city",snap.getString("city"));
                                data.put("recipient_id",snap.getId());
                                data.put("recipient_phoneno", snap.getString("phoneno"));
                                data.put("donor_id",donor_id);
                                FirebaseFirestore.getInstance()
                                        .collection("request")
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getApplicationContext(),"request send", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),"error try again", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }

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