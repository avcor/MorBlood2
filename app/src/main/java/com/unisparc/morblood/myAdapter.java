package com.unisparc.morblood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private List<user_list_item> listItems;
    private Context context;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public myAdapter(List<user_list_item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
        return  new ViewHolder(v,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user_list_item listItem = listItems.get(position);
        holder.textView_username.setText(listItem.getName());
        holder.textView_userphoneno.setText(listItem.getPhoneno());
        holder.textView_userstate.setText(listItem.getState());
        holder.textView_usercity.setText(listItem.getCity());
        holder.textView_usertehsil.setText(listItem.getTehsil());
        holder.textView_userbloodgrp.setText(listItem.getBloodgrp());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_username;
        public TextView textView_userphoneno,textView_userstate , textView_usercity, textView_usertehsil, textView_userbloodgrp;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView_username = (TextView) itemView.findViewById(R.id.user_name);
            textView_userphoneno = (TextView) itemView.findViewById(R.id.user_phoneno);
            textView_userstate = (TextView) itemView.findViewById(R.id.user_state);
            textView_usercity = (TextView) itemView.findViewById(R.id.user_city);
            textView_usertehsil = (TextView) itemView.findViewById(R.id.user_tehsil);
            textView_userbloodgrp = (TextView) itemView.findViewById(R.id.user_bloodgrp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}


