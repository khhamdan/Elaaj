package com.hamdan.elaajmedicalapp.models;

public class Symptoms
{
    String symptomsOfPatient,noOfDays,appointment_id;

    public Symptoms(String symptomsOfPatient, String noOfDays) {
        this.symptomsOfPatient = symptomsOfPatient;
        this.noOfDays = noOfDays;
    }

    public String getSymptomsOfPatient() {
        return symptomsOfPatient;
    }

    public void setSymptomsOfPatient(String symptomsOfPatient) {
        this.symptomsOfPatient = symptomsOfPatient;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }
}
