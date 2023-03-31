package com.hamdan.elaajmedicalapp.models;

public class Appointments
{
    String appointment_id,date,time,type,status,patient_id,doctor_id;

    public Appointments(String date, String time, String doctor_id) {
        this.date = date;
        this.time = time;
        this.doctor_id = doctor_id;
    }

    public Appointments(String date, String time, String type, String doctor_id) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.doctor_id = doctor_id;
    }

    public Appointments(String appointment_id, String date, String time, String type, String status, String patient_id, String doctor_id) {
        this.appointment_id = appointment_id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
    }

    public Appointments(String appointment_id, String date, String time, String type, String doctor_id) {
        this.appointment_id = appointment_id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.doctor_id = doctor_id;
    }


    public String getAppointment_id() {
        return appointment_id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
}
