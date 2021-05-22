package com.unisparc.morblood.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.unisparc.morblood.R;
import com.unisparc.morblood.activities.DonordetailActivity;
import com.unisparc.morblood.activities.MainActivity;
import com.unisparc.morblood.adapter.CustomUser_Adapter;
import com.unisparc.morblood.adapter.UserList_Adapter;
import com.unisparc.morblood.model.UserDetailsModel;
import com.warkiz.widget.IndicatorSeekBar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class RecipientFragment extends Fragment {

    View v;
    private static final String TAG = "recipientFragment" ;
    int lastButton_pressed;
    String country, state, city, pincode, address, district, sublocality;

    Button searchButton, autoSearch_button, seekbarSearch_button;
    FloatingActionButton but;
    LinearLayout searchLayout;
    RadioGroup searchGrp;
    EditText searchQueryEdt;
    Spinner bloodGrp_spinner;
    RecyclerView recyclerView;
    IndicatorSeekBar seekBar;

    String[] bloog_grp = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O-", "O+"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref = db.collection("users");
    CustomUser_Adapter Cadapter; // custom made extends recycler view and item view holder
    UserList_Adapter adapter; // extends firebase adapter

    AnimatedBottomBar bottomBar;

    LocationManager lm;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    ArrayList<String> autosearchList = new ArrayList<>();

    public RecipientFragment() {
        // Required empty public constructor
    }

    public static RecipientFragment newInstance(String param1, String param2) {
        RecipientFragment fragment = new RecipientFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
            v =  inflater.inflate(R.layout.fragment_recipient, container, false);
            findByids(v);
            setup(ref.orderBy("name", Query.Direction.DESCENDING)
                    .whereEqualTo(Objects.requireNonNull(bottomBar.getSelectedTab()).getTitle().toLowerCase(), true));

            lm = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            Cadapter = new CustomUser_Adapter(autosearchList);  // custom adapter for auto search and seekbar search
            ExampleRunnable exampleRunnable = new ExampleRunnable(20);  // this is thread class for autosearch and seekbar search.

            ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, bloog_grp);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bloodGrp_spinner.setAdapter(aa);

        but.setOnClickListener(view -> animateSearchLayout());

        searchGrp.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbState || i == R.id.rbCity || i == R.id.rbDistrict) {
                searchQueryEdt.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                searchQueryEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        searchButton.setOnClickListener(view -> {
            lastButton_pressed = view.getId();
            if (searchQueryEdt.getText().toString().equals("")
                    || searchGrp.getCheckedRadioButtonId() == -1) {
                Log.i(TAG, "onCreate: " + "toast");
                Toast.makeText(getContext(), "Enter name to be searched", Toast.LENGTH_LONG).show();
            } else {
                RadioButton RB = v.findViewById(searchGrp.getCheckedRadioButtonId());

                Query query = ref.orderBy(FieldPath.documentId())
                        .whereEqualTo(RB.getText().toString().toLowerCase(), searchQueryEdt.getText().toString().toUpperCase()) //state, city, pincode
                        .whereEqualTo("bloodGrp", bloodGrp_spinner.getSelectedItem().toString()) // bloodgrp type
                        .whereEqualTo(bottomBar.getSelectedTab().getTitle().toLowerCase(), true); // plasma, blood, both
                FirestoreRecyclerOptions<UserDetailsModel> options = new FirestoreRecyclerOptions.Builder<UserDetailsModel>()
                        .setQuery(query, UserDetailsModel.class)
                        .build();
                recyclerView.setAdapter(adapter);
                adapter.updateOptions(options);
            }
        });

        autoSearch_button.setOnClickListener(view -> {
            // use geofire and realtime database
            lastButton_pressed = view.getId();
            grant_permission();
            check_location_enableornot();
            if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                autosearchList.clear();
                exampleRunnable.UpdateKmRadius(20);
                executor.execute(exampleRunnable);
            }

        });

        seekbarSearch_button.setOnClickListener(view -> {
            // will use geofire and realtime database
            lastButton_pressed = view.getId();
            grant_permission();
            check_location_enableornot();
            int radialVal = seekBar.getProgress();
            seekBar.setVisibility(View.VISIBLE);

            if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                autosearchList.clear();
                exampleRunnable.UpdateKmRadius(radialVal);
                executor.execute(exampleRunnable);
            }
        });

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabSelected(int i, @org.jetbrains.annotations.Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {
                switch (lastButton_pressed) {
                    case R.id.searchButton : searchButton.performClick();
                        break;
                    case R.id.autoSearch_button : autoSearch_button.performClick();
                        break;
                    case R.id.seekbarSearch_button : seekbarSearch_button.performClick();
                        break;
                    // when app launches and user have not selected any item(button). just get the plasma, blood, both option and make query
                    default : Query query = ref.orderBy(FieldPath.documentId())
                            .whereEqualTo(bottomBar.getSelectedTab().getTitle().toLowerCase(), true); // plasma, blood, both
                        FirestoreRecyclerOptions<UserDetailsModel> options = new FirestoreRecyclerOptions.Builder<UserDetailsModel>()
                                .setQuery(query, UserDetailsModel.class)
                                .build();
                        recyclerView.setAdapter(adapter);
                        adapter.updateOptions(options);
                }
            }
            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {
                // do nothing
            }
        });

        return v;
    }

    public class ExampleRunnable implements Runnable {
        private int kmRadius;
        private double lat = 0, longi = 0;
        private String requirementType, bloodGrp;
        DatabaseReference ref;
        GeoFire geoFire;
        GeoQuery geoQuery;

        public ExampleRunnable(int i) {
            this.kmRadius = i;
        }
        public void UpdateKmRadius(int val){
            this.kmRadius = val;
        }

        @Override
        public void run() {
            lastLocation();
        }

        private synchronized void lastLocation() {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                grant_permission();
                check_location_enableornot();
                return;
            }
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(executor,location -> {
                if (location != null) {
                    //We have a location
                    Log.d(TAG, "run: last location" + location);
                    this.lat = location.getLatitude();
                    this.longi = location.getLongitude();
                    Log.i(TAG, "lastLocation: " + Thread.currentThread().getName());
                    print();
                } else  {
                    Log.d(TAG, "onSuccess: Location was null... calling robust");
                    robust();
                }
            }).addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getLocalizedMessage() ));
        }

        @SuppressLint("MissingPermission")
        private synchronized void robust(){
            Log.d(TAG, "robust thread" + Thread.currentThread().getName());
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(3 * 1000);
            locationRequest.setFastestInterval(1000);

            LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest).build();
            SettingsClient client = LocationServices.getSettingsClient(Objects.requireNonNull(getActivity()));
            Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
            locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> {
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Log.d(TAG + "new method", locationResult.getLocations() + "");
                        // check carefully before calling it
                        //print(locationResult, kmRadius, null);
                        for (Location location : locationResult.getLocations()) {
                            lat = location.getLatitude();
                            longi = location.getLongitude();
                        }
                        Log.i(TAG, "onLocationResult: " + Thread.currentThread().getName());
                        executor.execute(() -> print());
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    }
                };
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            });
            locationSettingsResponseTask.addOnFailureListener(e -> Log.d(TAG, "robust location failure " + e));
        }

        public synchronized void print(){
            Log.d(TAG, "print: " + Thread.currentThread().getName());
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(lat,longi,2);
                country = (addresses.get(1).getCountryName()).toUpperCase();
                state = (addresses.get(1).getAdminArea()).toUpperCase();
                city = (addresses.get(1).getLocality()).toUpperCase();
                district = (addresses.get(1).getSubAdminArea()).toUpperCase();
                address = (addresses.get(1).getAddressLine(0)).toUpperCase();
                pincode = (addresses.get(1).getPostalCode()).toUpperCase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "onCreate: " + country + state + city + district + address);
            requirementType = Objects.requireNonNull(bottomBar.getSelectedTab()).getTitle().toUpperCase(); // plasma,blood,both
            bloodGrp =  bloodGrp_spinner.getSelectedItem().toString().toUpperCase(); // bloodgrp

            ref = FirebaseDatabase.getInstance()
                    .getReference("DONOR/"+country+"/"+state+"/"+city+"/"+pincode+"/"+requirementType+"/"+bloodGrp);
            geoFire = new GeoFire(ref);
            geoQuery = geoFire.queryAtLocation(new GeoLocation(lat,longi), kmRadius);
            geosearch_AutoQuery();

        }

        public void geosearch_AutoQuery(){
            Log.d(TAG, "geosearchQuery thread " + Thread.currentThread().getName());
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    autosearchList.add(key);
                    Log.i(TAG, "onKeyEntered: "+ key);
                }

                @Override
                public void onKeyExited(String key) {
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                }

                @Override
                public void onGeoQueryReady() {
                    if (lastButton_pressed == R.id.autoSearch_button){
                        if(autosearchList.size()<10 && geoQuery.getRadius()<=40) {
                            geoQuery.setRadius(geoQuery.getRadius()+20);
                            Log.i(TAG, "print radius: " + geoQuery.getRadius() + autosearchList);
                            //autosearchList.clear();
                        }else{
                            lastPrint();
                        }
                    }
                    if (lastButton_pressed == R.id.seekbarSearch_button){
                        lastPrint();
                    }
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                }
            });

        }

        public void lastPrint(){
            geoQuery.removeAllListeners();
            Log.d(TAG, "lulli " + autosearchList);
            Log.d(TAG, "lulli: " + Thread.currentThread().getName());

            if (autosearchList.size() == 0){
                Log.d(TAG, "lulli there are no value");
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                });
            }
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                recyclerView.setAdapter(Cadapter);
                Cadapter.setOnItemClickListener((donorDetailModel, position, imageGender) -> {
                    Log.d("tag1", "lastPrint: " + donorDetailModel.getId() + donorDetailModel.getName() + "\n" + position);
                    Intent intent = new Intent(getContext(), DonordetailActivity.class);
                    intent.putExtra("snapShotClass", donorDetailModel);
                    startActivity(intent);
                });
                Cadapter.notifyDataSetChanged();
            });
        }

    }

    public void setup(Query query){
        FirestoreRecyclerOptions<UserDetailsModel> options = new FirestoreRecyclerOptions.Builder<UserDetailsModel>()
                .setQuery(query, UserDetailsModel.class)
                .build();
        adapter = new UserList_Adapter(options);

        adapter.setOnItemClickListener((documentSnapshot, position, imageGender) -> {
            Intent intent = new Intent(getContext(), DonordetailActivity.class);
            UserDetailsModel userDetailsModel = documentSnapshot.toObject(UserDetailsModel.class);
            intent.putExtra("snapShotClass", userDetailsModel);
//            intent.putExtra("return", imageGender.getTransitionName());
//            Log.d("tag1", "setup: " +  imageGender.getTransitionName());
//            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageGender, "recyclerImage_Transition");
            startActivity(intent);
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void findByids(View v){
        but = v.findViewById(R.id.but);
        searchLayout = v.findViewById(R.id.searchLayout);

        searchGrp = v.findViewById(R.id.RadiosearchGrp);
        searchQueryEdt = v.findViewById(R.id.searchQuery);

        but = v.findViewById(R.id.but);
        searchButton = v.findViewById(R.id.searchButton);
        autoSearch_button = v.findViewById(R.id.autoSearch_button);
        seekbarSearch_button = v.findViewById(R.id.seekbarSearch_button);
        bloodGrp_spinner = v.findViewById(R.id.bloodGrp_search);
        seekBar = v.findViewById(R.id.seekbar);

        bottomBar = v.findViewById(R.id.animatedBottom_bar);

        recyclerView = v.findViewById(R.id.rv);
    }
    public void animateSearchLayout(){
        // if it is visible, make it gone
        if(searchLayout.getVisibility() == View.VISIBLE){
            searchLayout.post(() -> {
                int cx = searchLayout.getRight() - searchLayout.getLeft();
                int cy = searchLayout.getBottom() - searchLayout.getTop();
                float finalRadius = (float) Math.hypot(cx,cy);

                Animator anim = ViewAnimationUtils.createCircularReveal(searchLayout, 0,0, finalRadius*1.0f,0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        searchLayout.setVisibility(View.GONE);
                    }
                });
                anim.setDuration(1000);
                anim.start();
            });
        }

        // if is gone, make it visible
        else if (searchLayout.getVisibility() == View.GONE){

            searchLayout.setVisibility(View.VISIBLE);
            searchLayout.post(() ->{
                int cx = searchLayout.getRight() - searchLayout.getLeft();
                int cy = searchLayout.getBottom() - searchLayout.getTop();
                float finalRadius = (float) Math.hypot(cx,cy);
                Animator anim = ViewAnimationUtils.createCircularReveal(searchLayout, 0,0, 0, finalRadius*1.0f);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
                anim.setDuration(1000);
                anim.start();
                Log.i(TAG, "onCreate: " + cx + " " + cy);
            });
        }
    }
    public void grant_permission(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }
    public void check_location_enableornot(){
        LocationManager lm = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle("Please, Enable gps service.\n " +
                            "So that we can reach people precisely.")
                    .setCancelable(false)
                    .setPositiveButton("enable", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).show();
        }
    }

}


//    public void trial(ArrayList<String> al){
//        LinearLayoutManager manager;
//        ArrayList<String> searchList;
//
//        manager = new LinearLayoutManager(getContext());
////        searchList = new ArrayList(al);
//        autosearchList.clear();
//        Cadapter = new CustomUser_Adapter(autosearchList);
//
//        recyclerView.setAdapter(Cadapter);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setHasFixedSize(true);
//    }