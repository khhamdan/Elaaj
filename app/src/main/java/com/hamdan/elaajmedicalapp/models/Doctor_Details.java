package com.hamdan.elaajmedicalapp.models;

public class Doctor_Details {
    String id,expertise,fees,experience,created_at,updated_at;

    public Doctor_Details(String id, String expertise, String fees) {
        this.id = id;
        this.expertise = expertise;
        this.fees = fees;
    }

    public Doctor_Details(String id, String expertise, String fees, String experience) {
        this.id = id;
        this.expertise = expertise;
        this.fees = fees;
        this.experience = experience;
    }
//    public Doctor_Details(String id, String expertise, String created_at, String updated_at) {
//        this.id = id;
//        this.expertise = expertise;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
