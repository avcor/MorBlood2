package com.unisparc.morblood;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unisparc.morblood.activities.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class Onboard extends AppCompatActivity {

    Button btn_donor , btn_recipient, btn_done,btn_done_rec;
    LinearLayout ll_recipient_details,ll_donor_details;
    RelativeLayout ll_options;
    ImageView img;
    EditText donor_name, donor_phone_no;
    AutoCompleteTextView donor_state, donor_city, donor_tehsil ,donor_bloodgrp;
    EditText recipient_name, recipient_phone_no;
    AutoCompleteTextView recipient_state, recipient_city, recipient_tehsil ;
    RadioGroup donor_rg;
    RadioButton blood,plasma,both;
    TextView topDisplay;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    AutoCompleteTextView gender_d,gender_r;
    TextView morblood,slogan;

    DocumentReference docRef;
    final String MYPREF = "mypref";
    boolean clicked = false;
    boolean rec_clicked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        img = findViewById(R.id.search_img);
        topDisplay = findViewById(R.id.plasma_blood_tv);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        btn_donor = findViewById(R.id.donor_butt);
        morblood = findViewById(R.id.textView2);
        slogan = findViewById(R.id.textview3);
        btn_recipient = findViewById(R.id.recipient_butt);
        ll_donor_details = findViewById(R.id.form_fill);
        ll_recipient_details = findViewById(R.id.form_fill_rec);
        ll_options = findViewById(R.id.r_u_option);
        btn_done = findViewById(R.id.done_but);
        btn_done_rec = findViewById(R.id.done_butt_reciever);

        donor_name = findViewById(R.id.name_edt);
        donor_phone_no = findViewById(R.id.mob_edt);
        gender_d = findViewById(R.id.donor_gender);
        donor_state = findViewById(R.id.state_edt);
        donor_city = findViewById(R.id.city_edt);
        donor_tehsil = findViewById(R.id.tehsil_edt);
//        donor_rg = findViewById(R.id.rd_grp);
        donor_bloodgrp = findViewById(R.id.BloodGroups_edt);
        blood = findViewById(R.id.blood);
        plasma = findViewById(R.id.plasma);
        both = findViewById(R.id.both);


        recipient_name = findViewById(R.id.name_edt_rec);
        recipient_phone_no = findViewById(R.id.mob_edt_rec);
        gender_r = findViewById(R.id.rec_gender);
        recipient_state = findViewById(R.id.state_edt_rec);
        recipient_city= findViewById(R.id.city_edt_rec);
        recipient_tehsil = findViewById(R.id.tehsil_edt_rec);
        
        //autocomplete text view setup


        final String gender[] = {"MALE","Female","OTHER","Prefer not to say"};
        final String states[] = {"Chhattisgarh"};
        final String bloodtypes[] = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

        final ArrayAdapter<String> donor_gender = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, gender);
        gender_d.setAdapter(donor_gender);

        ArrayAdapter<String> recipient_gender = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,gender);
        gender_r.setAdapter(recipient_gender);






        // reciever autosetup





        btn_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked =true;


                    ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, bloodtypes);
                    donor_bloodgrp.setAdapter(bloodAdapter);

                    ArrayAdapter<String> statesadapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, states);
                    donor_state.setAdapter(statesadapter);
                    donor_state.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            ArrayAdapter<String> cityadapter;
                            String states = donor_state.getText().toString();
                            String[] Cities;
                            switch (states) {
                                case "Chhattisgarh":
                                    Cities = new String[]{"SUKMA", "DANTEWADA", "BASTAR", "KONDAGAON", "NARAYANPUR", "KANKER", "KAWARDHA", "RAJNANDGAON", "BALOD", "DURG", "BEMETARA", "DHAMTARI", "GARIABAND", "RAIPUR", "BALODA BAZAR", "MAHASAMUND", "BILASPUR", "MUNGELI", "KORBA", "Janjgir-Champa", "RAIGARH", "JASHPUR", "KORBA", "SURAJPUR", "SARGUJA", "BALRAMPUR", "MARWAHI", "BIJAPUR"};
                                    cityadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Cities);
                                    donor_city.setAdapter(cityadapter);
                                    break;
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    donor_city.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String city = donor_city.getText().toString();
                            String[] tehsils;
                            ArrayAdapter<String> donor_adaptertehsil;

                            switch (city) {
                                case "SUKMA":
                                    tehsils = new String[]{"KONTA", "CHHINDGARH", "SUKMA"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "DANTEWADA":
                                    tehsils = new String[]{"DANTEWADA", "GEEDAM", "KATEKALYAN", "KUAKONDA"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "BASTAR":
                                    tehsils = new String[]{"BADE RAJPUR", "BAKAVAND", "BASTANAR", "BASTAR", "DARBHA", "FARASGAON", "JAGDALPUR", "LOHANDIGUDA", "TOKAPAL"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "KONDAGAON":
                                    tehsils = new String[]{"KONDAGAON", "MAKDI", "PHARASGAON", "KASHKAL", "BADERAJPUR"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "NARAYANPUR":
                                    tehsils = new String[]{"NARAYANPUR", "Orchha"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "KANKER":
                                    tehsils = new String[]{"kanker", "Charama", "Narharpur", "Bhanupratappur", "Durugkondal", "Antagarh"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "KAWARDHA":
                                    tehsils = new String[]{"KAWARDHA", "PANDARIYA", "BODLA", "SAHASPUR", "LOHARA"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "RAJNANDGAON":
                                    tehsils = new String[]{"Chhuikhadan", "Khairagarh", "Rajnandgaon", "Dongargarh", "Chhuriya", "Ambagarh Chowki", "Mohla", "Manpur", "Dongargoan"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "BALOD":
                                    tehsils = new String[]{"Gunderdehi", "Dondi", "Luhara", "Dondi", "Gurur", "Balod"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "DURG":
                                    tehsils = new String[]{"Durg", "Patan", "Dhamdha"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "DHAMTARI":
                                    tehsils = new String[]{"Dhamtari", "Kurud", "Nagri", "Magarlod"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "GARIYABAND":
                                    tehsils = new String[]{"Rajim", "Mainpur", "Chhura", "Bindranavagarh(Gariyaband)", "Deobhog"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "RAIPUR":
                                    tehsils = new String[]{"Raipur", "Arang", "Abhanpur", "Tilda", "Simga", "DHARSIVA"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "BALODA BAZAR":
                                    tehsils = new String[]{"Bhatapara", "Palari", "Bilaigarh", "Kasdol", "Baloda Bazar"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "MAHASAMUND":
                                    tehsils = new String[]{"Mahasamund", "Pithora", "Saraipali", "Bagbahra", "Basna"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "BILASPUR":
                                    tehsils = new String[]{"Belha", "Bilaspur", "Kota", "Takhatpur", "Masturi", "Lormi"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "BEMETARA":
                                    tehsils = new String[]{"Bemetara", "Berla", "Saja", "Thankhamhariya", "Nawagarh"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "BALRAMPUR":
                                    tehsils = new String[]{"Balrampur", "Tulsipur", "Utraula", "Lakhanpur"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "MARWAHI":
                                    tehsils = new String[]{"Marwahi", "Pendra Road", "Gorella", "Pendra"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "SURAJPUR":
                                    tehsils = new String[]{"Surajpur", "Ramanujnagar", "Premnagar"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "KABIRDHAM":
                                    tehsils = new String[]{"Pandariya", "Kawardha", "Bodla", "Sahaspur Lohara"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "BIJAPUR":
                                    tehsils = new String[]{"Bhairamgarh", "Bijapur", "Usur", "Bhopalpattnam"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "SARGUJA":
                                    tehsils = new String[]{"Ambikapur","Ramanujganj","Wadrafnagar","Pratappur","Bhaiyathan","Lundra"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "JASHPUR":
                                    tehsils = new String[]{"Pathalgaon", "Bagicha", "Farsabahar", "Jashpur", "Kunkuri", "Kansabel", "Manora", "Duldula"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "KORIA":
                                    tehsils = new String[]{"Baikunthpur", "Khadganva", "Manendragarh", "Bharatpur", "Sonhat", "Sitapur"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "RAIGARH":
                                    tehsils = new String[]{"Raigarh", "Sarangarh", "Udaipur (Dharamjaigarh)", "Baramkela", "Kharsia", "Pusour", "Lailunga", "Tamnar", "Gharghoda"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "JANJGHIR CHAMPA":
                                    tehsils = new String[]{"Champa", "Sakti", "Akaltara", "Pamgarh", "Dabhra", "Jaijaipur", "Nawagarh", "Janjgir", "Malkharoda", "Baloda"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "KORBA":
                                    tehsils = new String[]{"Korba", "Katghora", "Pali", "Poundi-Uproda", "Kartala"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                                case "MUNGELI":
                                    tehsils = new String[]{"MUNGELI", "PATHARIA", "Lormi"};
                                    donor_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    donor_tehsil.setAdapter(donor_adaptertehsil);
                                    break;
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });



                ll_donor_details.setVisibility(View.VISIBLE);
                btn_done.setVisibility(View.VISIBLE);
                ll_options.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                morblood.setVisibility(View.GONE);
                slogan.setVisibility(View.GONE);



                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                ll_donor_details.startAnimation(slide_up);

                topDisplay.setVisibility(View.VISIBLE);
                Animation alpha_onboard = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_onboard);

                topDisplay.startAnimation(alpha_onboard);
                docRef = fStore.collection("donor").document(fAuth.getCurrentUser().getUid());

                ifEmptyerror();
                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", donor_name.getText().toString());
                        data.put("phoneno", donor_phone_no.getText().toString());
                        data.put("gender",gender_d.getText().toString());
                        data.put("state", donor_state.getText().toString());
                        data.put("city", donor_city.getText().toString());
                        data.put("tehsil", donor_tehsil.getText().toString());
                        data.put("bloodgrp", donor_bloodgrp.getText().toString());
                        switch (donor_rg.getCheckedRadioButtonId()) {
                            case R.id.blood:
                                data.put("blood", true);
                                data.put("plasma", false);
                                break;

                            case R.id.plasma:
                                data.put("blood", false);
                                data.put("plasma", true);
                                break;

                            case R.id.both:
                                data.put("blood", true);
                                data.put("plasma", true);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "oops some error", Toast.LENGTH_LONG).show();
                                break;
                        }
                        // endd of switch

                        try{
                            docRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Welcome" + donor_name.getText().toString(), Toast.LENGTH_LONG).show();
                                        persistance("donor");
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "OOps something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });}catch (NullPointerException e){}
                    }
                });
            }
        });

        //


        btn_recipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_recipient_details.setVisibility(View.VISIBLE);
                ll_options.setVisibility(View.GONE);
                btn_done_rec.setVisibility(View.VISIBLE);
                morblood.setVisibility(View.GONE);
                slogan.setVisibility(View.GONE);

                rec_clicked=true;
                    ArrayAdapter<String> recstatesadapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, states);
                    recipient_state.setAdapter(recstatesadapter);
                    recipient_state.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            ArrayAdapter<String> reccityadapter;
                            String states = recipient_state.getText().toString();
                            String[] Cities;
                            switch (states) {
                                case "Chhattisgarh":
                                    Cities = new String[]{"SUKMA", "DANTEWADA", "BASTAR", "KONDAGAON", "NARAYANPUR", "KANKER", "KAWARDHA", "RAJNANDGAON", "BALOD", "DURG", "BEMETARA", "DHAMTARI", "GARIABAND", "RAIPUR", "BALODA BAZAR", "MAHASAMUND", "BILASPUR", "MUNGELI", "KORBA", "Janjgir-Champa", "RAIGARH", "JASHPUR", "KORBA", "SURAJPUR", "SARGUJA", "BALRAMPUR", "MARWAHI", "BIJAPUR"};
                                    reccityadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Cities);
                                    recipient_city.setAdapter(reccityadapter);
                                    break;
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    recipient_city.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String city = recipient_city.getText().toString();
                            String[] tehsils;
                            ArrayAdapter<String> rec_adaptertehsil;

                            switch (city) {
                                case "SUKMA":
                                    tehsils = new String[]{"KONTA", "CHHINDGARH", "SUKMA"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "DANTEWADA":
                                    tehsils = new String[]{"DANTEWADA", "GEEDAM", "KATEKALYAN", "KUAKONDA"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "BASTAR":
                                    tehsils = new String[]{"BADE RAJPUR", "BAKAVAND", "BASTANAR", "BASTAR", "DARBHA", "FARASGAON", "JAGDALPUR", "LOHANDIGUDA", "TOKAPAL"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "KONDAGAON":
                                    tehsils = new String[]{"KONDAGAON", "MAKDI", "PHARASGAON", "KASHKAL", "BADERAJPUR"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "NARAYANPUR":
                                    tehsils = new String[]{"NARAYANPUR", "Orchha"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "KANKER":
                                    tehsils = new String[]{"kanker", "Charama", "Narharpur", "Bhanupratappur", "Durugkondal", "Antagarh"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "KAWARDHA":
                                    tehsils = new String[]{"KAWARDHA", "PANDARIYA", "BODLA", "SAHASPUR", "LOHARA"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "RAJNANDGAON":
                                    tehsils = new String[]{"Chhuikhadan", "Khairagarh", "Rajnandgaon", "Dongargarh", "Chhuriya", "Ambagarh Chowki", "Mohla", "Manpur", "Dongargoan"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "BALOD":
                                    tehsils = new String[]{"Gunderdehi", "Dondi", "Luhara", "Dondi", "Gurur", "Balod"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "DURG":
                                    tehsils = new String[]{"Durg", "Patan", "Dhamdha"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "DHAMTARI":
                                    tehsils = new String[]{"Dhamtari", "Kurud", "Nagri", "Magarlod"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "GARIYABAND":
                                    tehsils = new String[]{"Rajim", "Mainpur", "Chhura", "Bindranavagarh(Gariyaband)", "Deobhog"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "RAIPUR":
                                    tehsils = new String[]{"Raipur", "Arang", "Abhanpur", "Tilda", "Simga", "DHARSIVA"};
                                    rec_adaptertehsil = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "BALODA BAZAR":
                                    tehsils = new String[]{"Bhatapara", "Palari", "Bilaigarh", "Kasdol", "Baloda Bazar"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "MAHASAMUND":
                                    tehsils = new String[]{"Mahasamund", "Pithora", "Saraipali", "Bagbahra", "Basna"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "BILASPUR":
                                    tehsils = new String[]{"Belha", "Bilaspur", "Kota", "Takhatpur", "Masturi", "Lormi"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "BEMETARA":
                                    tehsils = new String[]{"Bemetara", "Berla", "Saja", "Thankhamhariya", "Nawagarh"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "BALRAMPUR":
                                    tehsils = new String[]{"Balrampur", "Tulsipur", "Utraula", "Lakhanpur"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "MARWAHI":
                                    tehsils = new String[]{"Marwahi", "Pendra Road", "Gorella", "Pendra"};
                                    rec_adaptertehsil = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "SURAJPUR":
                                    tehsils = new String[]{"Surajpur", "Ramanujnagar", "Premnagar"};
                                    rec_adaptertehsil = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "KABIRDHAM":
                                    tehsils = new String[]{"Pandariya", "Kawardha", "Bodla", "Sahaspur Lohara"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "BIJAPUR":
                                    tehsils = new String[]{"Bhairamgarh", "Bijapur", "Usur", "Bhopalpattnam"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "SARGUJA":
                                    tehsils = new String[]{"Ambikapur","Ramanujganj","Wadrafnagar","Pratappur","Bhaiyathan","Lundra"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "JASHPUR":
                                    tehsils = new String[]{"Pathalgaon", "Bagicha", "Farsabahar", "Jashpur", "Kunkuri", "Kansabel", "Manora", "Duldula"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "KORIA":
                                    tehsils = new String[]{"Baikunthpur", "Khadganva", "Manendragarh", "Bharatpur", "Sonhat", "Sitapur"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "RAIGARH":
                                    tehsils = new String[]{"Raigarh", "Sarangarh", "Udaipur (Dharamjaigarh)", "Baramkela", "Kharsia", "Pusour", "Lailunga", "Tamnar", "Gharghoda"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "JANJGHIR CHAMPA":
                                    tehsils = new String[]{"Champa", "Sakti", "Akaltara", "Pamgarh", "Dabhra", "Jaijaipur", "Nawagarh", "Janjgir", "Malkharoda", "Baloda"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "KORBA":
                                    tehsils = new String[]{"Korba", "Katghora", "Pali", "Poundi-Uproda", "Kartala"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                case "MUNGELI":
                                    tehsils = new String[]{"MUNGELI", "PATHARIA", "Lormi"};
                                    rec_adaptertehsil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tehsils);
                                    recipient_tehsil.setAdapter(rec_adaptertehsil);
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + city);
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });



                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                ll_recipient_details.startAnimation(slide_up);

                img.setVisibility(View.GONE);

                topDisplay.setVisibility(View.VISIBLE);
                Animation alpha_onboard = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_onboard);

                topDisplay.startAnimation(alpha_onboard);

                final DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection("recipient").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                recifEmptyerror();

                btn_done_rec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", recipient_name.getText().toString());
                        data.put("phoneno", recipient_phone_no.getText().toString());
                        data.put("gender",gender_r.getText().toString());
                        data.put("state", recipient_state.getText().toString());
                        data.put("city", recipient_city.getText().toString());
                        data.put("tehsil", recipient_tehsil.getText().toString());

                        try{
                            docRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Welcome"+recipient_name.getText().toString(), Toast.LENGTH_LONG).show();
                                        persistance("recipient");
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        intent.putExtra("city",recipient_city.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "OOps something went wrong", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });}catch (NullPointerException e){}


                    }
                });
            }

        });


    }

    private void recifEmptyerror() {
        recipient_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String name=recipient_name.getText().toString();
                if(name.length()<4){btn_done_rec.setEnabled(false);}
                else { recipient_phone_no.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String ph_no = recipient_phone_no.getText().toString();
                        if(ph_no.length()<10){ btn_done_rec.setEnabled(false); }
                        else { gender_r.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                String gender = gender_r.getText().toString();
                                if(gender.length()<4){ btn_done_rec.setEnabled(false); }
                                else { recipient_state.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        String state = recipient_state.getText().toString();
                                        if(state.length()<3){ btn_done_rec.setEnabled(false); }
                                        else { recipient_city.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                String city = recipient_city.getText().toString();
                                                if(city.length()<4){ btn_done_rec.setEnabled(false);}
                                                else { recipient_tehsil.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                                                    @Override
                                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                        String tehsil =recipient_tehsil.getText().toString();
                                                        if(tehsil.length()<5){ btn_done_rec.setEnabled(false); }
                                                        else { btn_done_rec.setEnabled(true);}
                                                    }

                                                    @Override
                                                    public void afterTextChanged(Editable editable) { }
                                                });}
                                            }
                                            @Override
                                            public void afterTextChanged(Editable editable) { }
                                        });}
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) { }
                                }); }
                            }
                            @Override
                            public void afterTextChanged(Editable editable) {  }
                        }); }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
                }); }

            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    private void ifEmptyerror() {
        donor_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String name=donor_name.getText().toString();
                if(name.length()<4){btn_done.setEnabled(false);}
                else { donor_phone_no.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String ph_no = donor_phone_no.getText().toString();
                        if(ph_no.length()<10){ btn_done.setEnabled(false); }
                        else { gender_d.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                String gender = gender_d.getText().toString();
                                if(gender.length()<4){ btn_done.setEnabled(false); }
                                else { donor_state.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        String state = donor_state.getText().toString();
                                        if(state.length()<3){ btn_done.setEnabled(false); }
                                        else { donor_city.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                String city = donor_city.getText().toString();
                                                if(city.length()<4){ btn_done.setEnabled(false);}
                                                else { donor_tehsil.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                                                    @Override
                                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                        String tehsil = donor_tehsil.getText().toString();
                                                        if(tehsil.length()<5){ btn_done.setEnabled(false); }
                                                        else { donor_bloodgrp.addTextChangedListener(new TextWatcher() {
                                                            @Override
                                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                                                            @Override
                                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                                String BloodGrp = donor_bloodgrp.getText().toString();
                                                                if( BloodGrp.length()<2 ){btn_done.setEnabled(false);}
                                                                else { if(donor_rg.getCheckedRadioButtonId()==-1){ btn_done.setEnabled(false); }
                                                                else {btn_done.setEnabled(true);}}
                                                            }
                                                            @Override
                                                            public void afterTextChanged(Editable editable) { }
                                                        });
                                                        }
                                                    }

                                                    @Override
                                                    public void afterTextChanged(Editable editable) { }
                                                });}
                                            }
                                            @Override
                                            public void afterTextChanged(Editable editable) { }
                                        });}
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) { }
                                }); }
                            }
                            @Override
                            public void afterTextChanged(Editable editable) {  }
                        }); }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
                }); }

            }

            @Override
            public void afterTextChanged(Editable editable) { }
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
        alertDialogBuilder.setNeutralButton("HomePage", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Onboard.this,Onboard.class));
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



    public void persistance(String type){
        SharedPreferences sharedPreferences = getSharedPreferences(MYPREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type",type);
        editor.commit();
    }




}