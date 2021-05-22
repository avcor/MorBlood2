package com.unisparc.morblood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unisparc.morblood.R;
import com.unisparc.morblood.model.RequestModel;
import com.unisparc.morblood.model.UserDetailsModel;

import java.util.HashMap;
import java.util.Map;

// this activity is for donor to accept or reject a request sent by recipient
public class AcceptOrNotActivity extends AppCompatActivity {

    private final String USERS = "users"
            ,REQUEST = "request";


    TextView name, age, gender, state, city, district, pincode;
    CircularImageView genderIV;
    Button acceptButton, declineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptornot);

        findAllIds();

        RequestModel requestModel = (RequestModel) getIntent().getSerializableExtra("requestModel");

        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(requestModel.getRecipientId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        UserDetailsModel recipientModel = documentSnapshot.toObject(UserDetailsModel.class);
                        name.setText(recipientModel.getName());
                        age.setText(recipientModel.getAge());
                        gender.setText(recipientModel.getGender());
                        //state.setText();
                        city.setText(recipientModel.getCity());
                        district.setText(recipientModel.getDistrict());
                        pincode.setText(recipientModel.getPincode());

                        switch (recipientModel.getImageGender()) {
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
                    }
                });

        acceptButton.setOnClickListener(view -> {
            Map<String, Object> data = new HashMap<>();
            data.put("accept",true);
            FirebaseFirestore.getInstance()
                    .collection(REQUEST)
                    .document(requestModel.getDonorId() + requestModel.getRecipientId())
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this,"accepted", Toast.LENGTH_SHORT).show();
                    });
        });

        declineButton.setOnClickListener(view ->{
            Map<String, Object> data = new HashMap<>();
            data.put("accept",false);
            FirebaseFirestore.getInstance()
                    .collection(REQUEST)
                    .document(requestModel.getDonorId() + requestModel.getRecipientId())
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this,"accepted", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    public void findAllIds(){
        name = findViewById(R.id.name_accept);
        age= findViewById(R.id.age_accept);
        gender = findViewById(R.id.gender_accept);
//        state = findViewById(R.id.state_accept);
        city = findViewById(R.id.city_accept);
        district = findViewById(R.id.district_accept);
        pincode = findViewById(R.id.pincode_accept);

        genderIV = findViewById(R.id.imageGender_accept);
        acceptButton = findViewById(R.id.acceptButton);
        declineButton = findViewById(R.id.declineButton);
    }
}