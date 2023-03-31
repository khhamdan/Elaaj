package com.hamdan.elaajmedicalapp.models;

public class Reviews
{
    String name,comments,ratings,appointment_id;

    public Reviews(String name, String comments, String ratings) {
        this.name = name;
        this.comments = comments;
        this.ratings = ratings;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }
}
