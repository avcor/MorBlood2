package com.unisparc.morblood.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unisparc.morblood.R;
import com.unisparc.morblood.model.UserDetailsModel;

import java.util.ArrayList;

// searching for donor
public class CustomUser_Adapter extends RecyclerView.Adapter<CustomUser_Adapter.ViewHolder> {
    ArrayList<String> data;
    FirebaseFirestore firebaseFirestore;
    String TAG = "customuser_adapter";
    ArrayList<UserDetailsModel> donorDetailModelsAL = new ArrayList<>();
    private OnItemClickListener listener;

    public CustomUser_Adapter(ArrayList<String> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdetail_cardview, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseFirestore.collection("users")
                .document(data.get(position))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        donorDetailModelsAL.add(position,documentSnapshot.toObject(UserDetailsModel.class));

                        holder.name.setText(donorDetailModelsAL.get(position).getName());
                        holder.age.setText(donorDetailModelsAL.get(position).getAge());
                        holder.city.setText(donorDetailModelsAL.get(position).getCity());
                        holder.district.setText(donorDetailModelsAL.get(position).getDistrict());

                        switch (donorDetailModelsAL.get(position).getImageGender()) {
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
                                break;



                            default:
                                holder.imageGender.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                    }
                    else {
                        data.remove(position);
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, age, gender, city, bloodGrp, district ;
        CircularImageView imageGender;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageGender = itemView.findViewById(R.id.imageGender);
            name = itemView.findViewById(R.id.usernameCard);
            age = itemView.findViewById(R.id.userageCard);
            //gender = itemView.findViewById(R.id.usergenderCard);
            city = itemView.findViewById(R.id.usercityCard);
            district = itemView.findViewById(R.id.userdistrictCard);
            //bloodGrp = itemView.findViewById(R.id.userbloodGrpCard);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Object Tag = imageGender.getTag();
                Log.d(TAG, "NoteHolder: " + Tag);
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(donorDetailModelsAL.get(position), position, imageGender);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(UserDetailsModel donorDetailModel, int position, ImageView imageGender);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
