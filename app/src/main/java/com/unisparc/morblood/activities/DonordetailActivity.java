package com.unisparc.morblood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unisparc.morblood.R;
import com.unisparc.morblood.model.UserDetailsModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import static java.text.DateFormat.getDateTimeInstance;

public class DonordetailActivity extends AppCompatActivity {

    String TAG = "userdetailActivity";
    TextView nameTV, ageTV, genderTV, stateTV, cityTV, districtTV, pincodeTV, bloodGrpTV;
    CircularImageView genderIV;
    Button requestButton;

    String REQUEST = "request";
    FirebaseUser recipientUser;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(DonordetailActivity.this, MainActivity.class);
//        Log.d("tag1", "setup: " +  getIntent().getStringExtra("return"));
//        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, genderIV, getIntent().getStringExtra("return"));
//        startActivity(intent, activityOptionsCompat.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);

        findallviews();
        recipientUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        UserDetailsModel donorDetails = (UserDetailsModel) intent.getSerializableExtra("snapShotClass");

        nameTV.setText(donorDetails.getName());
        ageTV.setText(donorDetails.getAge());
        genderTV.setText(donorDetails.getGender());
        cityTV.setText(donorDetails.getCity());
        districtTV.setText(donorDetails.getDistrict());
        pincodeTV.setText(donorDetails.getPincode());
        bloodGrpTV.setText(donorDetails.getBloodGrp());

        switch (donorDetails.getImageGender()) {
            case "girl0":
                genderIV.setImageResource(R.drawable.girl0);
                break;
            case "girl1":
                genderIV.setImageResource(R.drawable.girl1);
                break;
            case "girl2":
                genderIV.setImageResource(R.drawable.girl2);
                break;
            case "man0":
                genderIV.setImageResource(R.drawable.man0);
                break;
            case "man1":
                genderIV.setImageResource(R.drawable.man1);
                break;
            case "man2":
                genderIV.setImageResource(R.drawable.man2);
                break;
            default:
                genderIV.setImageResource(R.drawable.ic_launcher_foreground);
        }

        Log.d(TAG, "onCreate: " + donorDetails.getDistrict());

        requestButton.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            long timestamp = 0L;
            timestamp = System.currentTimeMillis();
            Log.d(TAG, "onCreate: " +  donorDetails.getId() +"  "+ recipientUser.getUid());

            if (!donorDetails.getId().equals(recipientUser.getUid()) && !recipientUser.equals(null) && timestamp != 0L) {
                // one cant send request to itself
                Map<String, Object> map = new HashMap<>();
                map.put("recipientId", recipientUser.getUid());
                map.put("timestamp",timestamp);
                map.put("donorId", donorDetails.getId());
                //Log.d("tag1", "onCreate: " + "write");

                FirebaseFirestore.getInstance()
                        .collection(REQUEST)
                        .document(donorDetails.getId()+recipientUser.getUid())
                        .set(map)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "request send", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
                        });
            }
            if (recipientUser.equals(null) || timestamp == 0L){
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            if (donorDetails.getId().equals(recipientUser.getUid())){
                Toast.makeText(getApplicationContext(), "User can'nt send request to himself", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void findallviews(){
        nameTV = findViewById(R.id.name_UserDetail);
        ageTV = findViewById(R.id.age_UserDetail);
        genderTV = findViewById(R.id.gender_UserDetail);
//        stateTV = findViewById(R.id.state_UserDetail);
        cityTV = findViewById(R.id.city_UserDetail);
        districtTV = findViewById(R.id.district_UserDetail);
        pincodeTV = findViewById(R.id.pincode_UserDetail);
        bloodGrpTV = findViewById(R.id.bloodGrp_UserDetail);
        genderIV = findViewById(R.id.circularGender_UserDetails);
        requestButton = findViewById(R.id.requestButton);
    }
}