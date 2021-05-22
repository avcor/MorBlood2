package com.unisparc.morblood;

import android.os.Parcel;
import android.os.Parcelable;

public class user_list_item implements Parcelable {

    String name;
    String phoneno;
    String state;
    String city;
    String tehsil;
    String bloodgrp;

    public user_list_item(String name, String phoneno, String state, String city, String tehsil, String bloodgrp) {
        this.name = name;
        this.phoneno = phoneno;
        this.state = state;
        this.city = city;
        this.tehsil = tehsil;
        this.bloodgrp = bloodgrp;
    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getTehsil() {
        return tehsil;
    }

    public String getBloodgrp() {
        return bloodgrp;
    }

    public static final Creator<user_list_item> CREATOR = new Creator<user_list_item>() {
        @Override
        public user_list_item createFromParcel(Parcel in) {
            return new user_list_item(in);
        }

        @Override
        public user_list_item[] newArray(int size) {
            return new user_list_item[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneno);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(tehsil);
        dest.writeString(bloodgrp);
    }

    protected user_list_item(Parcel in) {
        name = in.readString();
        phoneno = in.readString();
        state = in.readString();
        city = in.readString();
        tehsil = in.readString();
        bloodgrp = in.readString();
    }



}
