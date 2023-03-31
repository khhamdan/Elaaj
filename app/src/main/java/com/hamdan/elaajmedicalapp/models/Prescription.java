package com.hamdan.elaajmedicalapp.models;

public class Prescription
{
    String date,time,prescriptions,voice_note,appointment_id,patient_id,doctor_id,name,profile_pic;

    public Prescription(String date, String time, String prescriptions, String voice_note, String appointment_id, String patient_id, String doctor_id, String name, String profile_pic) {
        this.date = date;
        this.time = time;
        this.prescriptions = prescriptions;
        this.voice_note = voice_note;
        this.appointment_id = appointment_id;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.name = name;
        this.profile_pic = profile_pic;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(String prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getVoice_note() {
        return voice_note;
    }

    public void setVoice_note(String voice_note) {
        this.voice_note = voice_note;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }
}
