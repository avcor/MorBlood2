package com.unisparc.morblood.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.unisparc.morblood.R;
import com.unisparc.morblood.activities.AcceptOrNotActivity;
import com.unisparc.morblood.activities.data_onboard;
import com.unisparc.morblood.adapter.RequestList_Adapter;
import com.unisparc.morblood.model.RequestModel;

public class DonorFragment extends Fragment {
    public DonorFragment() {
        // Required empty public constructor
    }
    public static DonorFragment newInstance(String param1, String param2) {
        //used for creatng neew instances of fragment
        // this is factory method
        DonorFragment fragment = new DonorFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    private final String TAG = "donorFragment",
            USERS = "users",
            REQUEST = "request",
            DONORID = "donorId";
    private boolean success = false,
            donor = false;
    View v;
    RelativeLayout notAdonorLL;
    LinearLayout allRequestLL;
    Button beADonorButton;
    RecyclerView request_RecyclerView;

    RequestList_Adapter rAdapter;
    FirebaseUser user;

    @Override
    public void onResume() {
        super.onResume();
        if(!donor) {
            // check for the new updated info if yes
            FirebaseFirestore.getInstance()
                    .collection(USERS)
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && documentSnapshot.getBoolean("donor")) {
                            // user is a donor let him enjoy app
                            success = true;
                            donor = true;
                            makeitVisible();
                        } else {
                            // user is not a donor, convert him
                            success = true;
                            donor = false;
                            notAdonorLL.setVisibility(View.VISIBLE);
                            allRequestLL.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(e -> {
                        success = false;
                        Toast.makeText(getContext(), "connection error. Restart App", Toast.LENGTH_LONG).show();
                    });

        }
        else {
            // already a donor no need to check
            makeitVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        rAdapter.stopListening();
    }

    public void makeitVisible(){
        rAdapter.startListening();
        notAdonorLL.setVisibility(View.GONE);
        allRequestLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_donor, container, false);
        request_RecyclerView = v.findViewById(R.id.request_RecyclerView);
        notAdonorLL = v.findViewById(R.id.notAdonorLL);
        beADonorButton = v.findViewById(R.id.beAdonorButton);
        allRequestLL = v.findViewById(R.id.allRequestLL);
        beADonorButton = v.findViewById(R.id.beAdonorButton);

        beADonorButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), data_onboard.class);
            startActivity(intent);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(REQUEST);

        Query query = ref.whereEqualTo(DONORID,
                /* this must be donor */ FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirestoreRecyclerOptions<RequestModel> options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query, RequestModel.class)
                .build();
        rAdapter = new RequestList_Adapter(options);
        request_RecyclerView.setHasFixedSize(true);
        request_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        request_RecyclerView.setAdapter(rAdapter);

        rAdapter.setOnItemClickListener((requestSnapshot, position, imageGender) -> {
            RequestModel requestModel = requestSnapshot.toObject(RequestModel.class);
            Intent intent = new Intent(getActivity(), AcceptOrNotActivity.class);
            intent.putExtra("requestModel", requestModel);
            startActivity(intent);
        });

        return v;

    }

}