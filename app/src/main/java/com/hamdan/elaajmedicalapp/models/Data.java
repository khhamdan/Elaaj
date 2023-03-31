
package com.hamdan.elaajmedicalapp.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("appointment")
    @Expose
    private List<Appointment> appointment = null;
    @SerializedName("doctor")
    @Expose
    private List<List<Doctor>> doctor = null;

    public List<Appointment> getAppointment() {
        return appointment;
    }

//    public Data(List<Appointment> appointment, List<List<Doctor>> doctor) {
//        this.appointment = appointment;
//        this.doctor = doctor;
//    }


    public void setAppointment(List<Appointment> appointment) {
        this.appointment = appointment;
    }

    public List<List<Doctor>> getDoctor() {
        return doctor;
    }

    public void setDoctor(List<List<Doctor>> doctor) {
        this.doctor = doctor;
    }

}
