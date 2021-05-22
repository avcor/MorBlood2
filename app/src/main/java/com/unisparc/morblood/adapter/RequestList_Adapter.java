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
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unisparc.morblood.R;
import com.unisparc.morblood.model.UserDetailsModel;
import com.unisparc.morblood.model.RequestModel;

import java.util.Date;

// request list if for donor, recipient send this to donor
public class RequestList_Adapter extends FirestoreRecyclerAdapter<RequestModel,RequestList_Adapter.Holder> {
    private static final String TAG = "RequestList_Adapter" ;
    private String USERS = "users";
    private Long timeStamp;
    private OnItemClickListener listener;

    public RequestList_Adapter(@NonNull FirestoreRecyclerOptions<RequestModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull RequestModel model) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(model.getRecipientId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserDetailsModel recipientDetail = documentSnapshot.toObject(UserDetailsModel.class);

                        switch (recipientDetail.getImageGender()) {
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

                        holder.name.setText(recipientDetail.getName());
                        holder.age.setText(recipientDetail.getAge());
                        holder.district.setText(recipientDetail.getDistrict());

                            // processing for timestamp and making timestamp visible and other TV invisible
                            holder.city.setVisibility(View.GONE);
                            holder.timestamp.setVisibility(View.VISIBLE);
                            // pre processing on time stamp
                            timeStamp = (Long) model.getTimestamp();
                            Date d1 = new Date((Long) timeStamp);
                            Date d2 = new Date(System.currentTimeMillis());
                            long diff = (d2.getTime() - d1.getTime());
                            long t = diff/1000/60;  // convert it into min
                            if(t <= 60) {
                                holder.timestamp.setText(+ t + " min ago");
                            }
                            else if(t/60 <= 24){// convert into hrs
                                holder.timestamp.setText(t/60 + " hrs ago");
                            }
                            else{
                                holder.timestamp.setText(t/60/24 + " days ago");
                            }
                    }
                });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdetail_cardview, parent, false);
        return new Holder(v);
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView name, age, gender, city, bloodGrp, district, timestamp;
        CircularImageView imageGender;

        public Holder(@NonNull View itemView) {
            super(itemView);

            imageGender = itemView.findViewById(R.id.imageGender);
            name = itemView.findViewById(R.id.usernameCard);
            age = itemView.findViewById(R.id.userageCard);
            //gender = itemView.findViewById(R.id.usergenderCard);
            //bloodGrp = itemView.findViewById(R.id.userbloodGrpCard);
            city = itemView.findViewById(R.id.usercityCard);
            district = itemView.findViewById(R.id.userdistrictCard);
            timestamp =  itemView.findViewById(R.id.timestamp);

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
        void onItemClick(DocumentSnapshot requestSnapshot, int position, ImageView imageGender);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
