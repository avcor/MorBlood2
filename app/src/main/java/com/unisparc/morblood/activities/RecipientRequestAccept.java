package com.unisparc.morblood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.unisparc.morblood.R;
import com.unisparc.morblood.adapter.AcceptOrNot_Adapter;
import com.unisparc.morblood.model.RequestModel;

// activity for recipient if the request is accepted by the donor or not
public class RecipientRequestAccept extends AppCompatActivity {


    private final String TAG = "donorFragment",
            USERS = "users",
            REQUEST = "request",
            DONORID = "donorId",
            RECIPIENTID = "recipientId";
    RecyclerView recyclerView;
    AcceptOrNot_Adapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_request_accept);

        recyclerView = findViewById(R.id.recycler_RecipientRequestAccept);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(REQUEST);
        Query query = ref.whereEqualTo(RECIPIENTID,
                /* this must be recipient */ FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirestoreRecyclerOptions<RequestModel> options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query, RequestModel.class)
                .build();
        rAdapter = new AcceptOrNot_Adapter(options);
        rAdapter.startListening();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rAdapter);
//
//        rAdapter.setOnItemClickListener((requestSnapshot, position, imageGender) -> {
//            RequestModel requestModel = requestSnapshot.toObject(RequestModel.class);
//            Intent intent = new Intent(getActivity(), AcceptOrNotActivity.class);
//            intent.putExtra("requestModel", requestModel);
//            startActivity(intent);
//        });

    }
}