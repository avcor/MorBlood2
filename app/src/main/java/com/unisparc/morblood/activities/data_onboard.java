package com.unisparc.morblood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unisparc.morblood.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class data_onboard extends AppCompatActivity implements LocationListener{

    Button button_donor, button_recipient, done_button;
    EditText name_edt, age_edt;
    TextView address_tv;
    ProgressBar addressProgress;

    LinearLayout formFill, donorOptions_ll;
    RadioGroup rdGrp_gender, rdGrp_donationType;

    LocationManager locationManager;
    String country, state, city, pincode, address, district, sublocality;
    Double latitude, longitude;

    Spinner spinner_bld;
    String[] bloog_grp = {"click here","A+", "A-", "AB+", "AB-", "B+", "B-", "O-", "O+"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_onboard);

        // find all view by id
        findingAll_views();

        // permission for accessing location
        grant_permission();
        check_location_enableornot();
        getlocation();


        button_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open form
                formFill.setVisibility(View.VISIBLE);
                donorOptions_ll.setVisibility(View.VISIBLE);
//                ArrayAdapter arrayAdapter = new ArrayAdapter(data_onboard.this,android.R.layout.simple_spinner_item,bloog_grp);
//                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner_bld.setAdapter(arrayAdapter);
                ArrayAdapter<String> dataadapter = new ArrayAdapter<String>(data_onboard.this,
                        android.R.layout.simple_spinner_item, bloog_grp);
                dataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_bld.setAdapter(dataadapter);
            }
        });
        button_recipient.setOnClickListener(view -> {
            formFill.setVisibility(View.VISIBLE);
            donorOptions_ll.setVisibility(View.GONE);
        });
        done_button.setOnClickListener(view -> {
            // if true then none is empty
            if(checkEmptyField()){
                Map<String, Object> data = new HashMap<>();
                data = addBasic_info(data);

                // check for donor and recipient
                if(donorOptions_ll.getVisibility() == View.VISIBLE){ // its donor
                    data.put("donor",true);
                    data.put("bloodGrp", spinner_bld.getSelectedItem().toString());
                    switch (rdGrp_donationType.getCheckedRadioButtonId()) {
                        case R.id.blood:
                            data.put("blood", true);
                            data.put("plasma", false);
                            data.put("both", false);
                            break;

                        case R.id.plasma:
                            data.put("blood", false);
                            data.put("plasma", true);
                            data.put("both", false);
                            break;

                        case R.id.both:
                            data.put("blood", true);
                            data.put("plasma", true);
                            data.put("both", true);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "oops some error", Toast.LENGTH_LONG).show();
                            break;
                    }
                    realtimedatabse_geofireWrite();
                }
                else{ // recipient
                    data.put("recipient", true);
                }
                firestoreGeneral_infoWrite(data);
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),2);
            country = (addresses.get(1).getCountryName()).toUpperCase();
            state = (addresses.get(1).getAdminArea()).toUpperCase();
            city = (addresses.get(1).getLocality()).toUpperCase();
            district = (addresses.get(1).getSubAdminArea()).toUpperCase();
            address = (addresses.get(1).getAddressLine(0)).toUpperCase();
            pincode = (addresses.get(1).getPostalCode()).toUpperCase();

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            //sublocality = (addresses.get(0).getSubLocality());  sometimes it may be null

            // set the address in the address text_view and set done button visible and make progress bar visible
            address_tv.setText("Address " + address);
            addressProgress.setVisibility(View.GONE);
            done_button.setVisibility(View.VISIBLE);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }
    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }


    public void realtimedatabse_geofireWrite(){
        RadioButton RB = findViewById(rdGrp_donationType.getCheckedRadioButtonId());
        String donorType =  RB.getText().toString().toUpperCase();
        String bloodGrp = spinner_bld.getSelectedItem().toString().toUpperCase();

        DatabaseReference ref =FirebaseDatabase.getInstance()
                .getReference("DONOR/" + country + "/" + state + "/" + city + "/" + pincode + "/" + donorType + "/" + bloodGrp );
        GeoFire geofire = new GeoFire(ref);
        geofire.setLocation(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), new GeoLocation(latitude, longitude),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null){
                            Toast.makeText(data_onboard.this,"geolocatation error", Toast.LENGTH_SHORT).show();
                            Log.d("abcd", error.toString());
                        }
                        else {
                            // success
                            //Toast.makeText(data_onboard.this,"geolocatation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void firestoreGeneral_infoWrite(Map data){
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(data_onboard.this, "register success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(data_onboard.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(data_onboard.this, "register unsuccess", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean checkEmptyField(){
        // check for null and unselected or else write to database
        if(donorOptions_ll.getVisibility() == View.VISIBLE) {
           // its donor checking
            if (name_edt.getText().toString().isEmpty()
                    || age_edt.getText().toString().isEmpty()
                    || rdGrp_gender.getCheckedRadioButtonId() == -1
                    || rdGrp_donationType.getCheckedRadioButtonId() == -1
                    || spinner_bld.getSelectedItem().toString().equals("click here")) {
                Toast.makeText(data_onboard.this, "you missed something", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        else{
            // its recipient
            if (name_edt.getText().toString().isEmpty()
                    || age_edt.getText().toString().isEmpty()){
                Toast.makeText(data_onboard.this, "you missed something r", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> addBasic_info (Map<String,Object> data){
        RadioButton radioButton_gender;
        radioButton_gender = findViewById(rdGrp_gender.getCheckedRadioButtonId());

        data.put("name", name_edt.getText().toString());
        data.put("age", age_edt.getText().toString());
        data.put("phoneno", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());
//        Log.d("abcd", "addBasic_info: " + FirebaseAuth.getInstance().getCurrentUser());
        data.put("gender", radioButton_gender.getText().toString());
        data.put("country", country);
        data.put("state",state);
        data.put("city", city );
        data.put("district", district);
        data.put("address", address);
        data.put("pincode", pincode);

        if(radioButton_gender.getText().toString().toUpperCase().equals("FEMALE")){
            // setting rando image for female identity
            switch ((int) (Math.random() * 3)) {
                case 0:
                    data.put("imageGender","girl0");
                    break;
                case 1:
                    data.put("imageGender","girl1");
                    break;
                case 2:
                default:data.put("imageGender","girl2");
            }
        }
        else{
            //setting random image for male identity
            switch ((int) (Math.random() * 3)){
                case 0:
                    data.put("imageGender","man0");
                    break;
                case 1:
                    data.put("imageGender","man1");
                    break;
                case 2:
                default: data.put("imageGender","man2");
            }
        }

        // now null for recipient and donor
//        data.put("recipient",null);
//        data.put("donor",null);
//        data.put("blood", false);
//        data.put("plasma", false);
//        data.put("both", false);
        return data;
    }

    public void grant_permission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }
    public void check_location_enableornot(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(data_onboard.this)
                    .setTitle("Please, Enable gps service.\n " +
                            "So that we can reach people precisely.")
                    .setCancelable(false)
                    .setPositiveButton("enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).show();
        }
    }
    public void getlocation(){
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,500,10,(LocationListener) this) ;
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }


    public void findingAll_views(){

        button_donor = findViewById(R.id.donor_but);
        button_recipient = findViewById(R.id.recipient_but);
        done_button = findViewById(R.id.done_but);
        address_tv = findViewById(R.id.address_text);

        name_edt = findViewById(R.id.name_edt);
        age_edt = findViewById(R.id.age_edt);

        addressProgress = findViewById(R.id.address_progress);

        formFill = findViewById(R.id.form_fill);
        donorOptions_ll = findViewById(R.id.donor_option);

        spinner_bld = findViewById(R.id.spinner_bldGrp);
        rdGrp_gender = findViewById(R.id.rd_grp_gender);
        rdGrp_donationType = findViewById(R.id.rd_grp_chose);
    }
}