package com.hamdan.elaajmedicalapp.models;

public class PatientWalletModel {
    private String appointmentWalletDate;
    private String appointmentWalletTime;
    private String appointmentWalletFees;
    private String appointmentStatus;

    private String appointmentWalletDoctorName;
//
//    public PatientWalletModel(String appointmentWalletDoctorName) {
//        this.appointmentWalletDoctorName = appointmentWalletDoctorName;
//    }
//
//    public PatientWalletModel(String appointmentWalletDate, String appointmentWalletTime, String appointmentWalletFees, String appointmentStatus) {
//        this.appointmentWalletDate = appointmentWalletDate;
//        this.appointmentWalletTime = appointmentWalletTime;
//        this.appointmentWalletFees = appointmentWalletFees;
//        this.appointmentStatus = appointmentStatus;
//    }

    public PatientWalletModel(String appointmentWalletDate, String appointmentWalletTime, String appointmentWalletFees, String appointmentStatus, String appointmentWalletDoctorName) {
        this.appointmentWalletDate = appointmentWalletDate;
        this.appointmentWalletTime = appointmentWalletTime;
        this.appointmentWalletFees = appointmentWalletFees;
        this.appointmentStatus = appointmentStatus;
        this.appointmentWalletDoctorName = appointmentWalletDoctorName;
    }

    public String getAppointmentWalletDate() {
        return appointmentWalletDate;
    }

    public void setAppointmentWalletDate(String appointmentWalletDate) {
        this.appointmentWalletDate = appointmentWalletDate;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppointmentWalletTime() {
        return appointmentWalletTime;
    }

    public void setAppointmentWalletTime(String appointmentWalletTime) {
        this.appointmentWalletTime = appointmentWalletTime;
    }

    public String getAppointmentWalletFees() {
        return appointmentWalletFees;
    }

    public void setAppointmentWalletFees(String appointmentWalletFees) {
        this.appointmentWalletFees = appointmentWalletFees;
    }

    public String getAppointmentWalletDoctorName() {
        return appointmentWalletDoctorName;
    }

    public void setAppointmentWalletDoctorName(String appointmentWalletDoctorName) {
        this.appointmentWalletDoctorName = appointmentWalletDoctorName;
    }
}
