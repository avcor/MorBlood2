package com.unisparc.morblood.model;

import java.io.Serializable;

public class UserDetailsModel implements Serializable {
    private String name
            ,age
            ,gender
            ,bloodGrp
            ,city
            ,district
            ,imageGender
            ,pincode
            ,id;

    public UserDetailsModel() {
        // emppty constructor needed
    }
    public UserDetailsModel(String name, String age, String gender, String bloodGrp, String city, String district, String imageGender, String pincode, String id) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGrp = bloodGrp;
        this.city = city;
        this.district = district;
        this.imageGender = imageGender;
        this.pincode = pincode;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict(){return district;}

    public String getImageGender(){return imageGender;}

    public String getPincode(){return pincode;}

    public String getId(){return id;}
}
