package com.unisparc.morblood.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.unisparc.morblood.R;
import com.unisparc.morblood.model.RequestModel;

public class AcceptOrNot_Adapter extends FirestoreRecyclerAdapter<RequestModel, AcceptOrNot_Adapter.Holder> {

    public AcceptOrNot_Adapter(@NonNull FirestoreRecyclerOptions<RequestModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull RequestModel model) {
        Log.d("tag1", "onBindViewHolder: " + model.getAccept());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.acceptornot_cardview, parent, false);
        return new Holder(v);
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView name, age, district, accepted;
        Button call;
        public Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_AcceptOrNot);
            district = itemView.findViewById(R.id.district_AcceptOrNot);
            accepted = itemView.findViewById(R.id.accept_AcceptOrNot);
            call = itemView.findViewById(R.id.call_AcceptOrNot);
        }
    }
}
