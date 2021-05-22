package com.unisparc.morblood.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unisparc.morblood.R;
import com.unisparc.morblood.model.UserDetailsModel;

// for searching of donor
public class UserList_Adapter extends FirestoreRecyclerAdapter<UserDetailsModel, UserList_Adapter.NoteHolder> {

    String TAG = "userlistAdapter";
    private OnItemClickListener listener;

    public UserList_Adapter(@NonNull FirestoreRecyclerOptions<UserDetailsModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull UserDetailsModel model) {
        holder.name.setText("Name " + model.getName());
        holder.age.setText("Age " + model.getAge());
        //holder.gender.setText(model.getGender());
        //holder.bloodGrp.setText(model.getBloodGrp());
        holder.city.setText("City " + model.getCity());
        holder.district.setText("District " + model.getDistrict());
        switch (model.getImageGender()) {
                case "girl0":
                    holder.imageGender.setImageResource(R.drawable.girl0);
                    break;
                case "girl1":
                    holder.imageGender.setImageResource(R.drawable.girl1);
                    break;
                case "girl2":
                    holder.imageGender.setImageResource(R.drawable.girl2);
                    break;
                case "man0":
                    holder.imageGender.setImageResource(R.drawable.man0);
                    break;
                case "man1":
                    holder.imageGender.setImageResource(R.drawable.man1);
                    break;
                case "man2":
                     holder.imageGender.setImageResource(R.drawable.man2);
                default:
                    holder.imageGender.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.imageGender.setTransitionName("recyclerImage_Transition" + Math.random());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdetail_cardview, parent, false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView name, age, gender, city, bloodGrp, district ;
        CircularImageView imageGender;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            imageGender = itemView.findViewById(R.id.imageGender);
            name = itemView.findViewById(R.id.usernameCard);
            age = itemView.findViewById(R.id.userageCard);
            //gender = itemView.findViewById(R.id.usergenderCard);
            //bloodGrp = itemView.findViewById(R.id.userbloodGrpCard);
            city = itemView.findViewById(R.id.usercityCard);
            district = itemView.findViewById(R.id.userdistrictCard);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Object Tag = imageGender.getTag();
                Log.d(TAG, "NoteHolder: " + Tag);
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position, imageGender);
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position, ImageView imageGender);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
