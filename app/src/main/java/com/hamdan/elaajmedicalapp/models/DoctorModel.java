package com.hamdan.elaajmedicalapp.models;

public class DoctorModel
{
    String doctor_id,profile_pic,name,expertise;

    public DoctorModel() {
    }

    public DoctorModel(String doctor_id, String profile_pic, String name, String expertise) {
        this.doctor_id = doctor_id;
        this.profile_pic = profile_pic;
        this.name = name;
        this.expertise = expertise;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}
