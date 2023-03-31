package com.hamdan.elaajmedicalapp.models;

public class DoctorReceivePrescriptionOfPatient
{
    String current_medication;
    String UrlOfRecentPrescription;
    String UrlOfLabTest;
    String created_at;

    public DoctorReceivePrescriptionOfPatient(String current_medication, String urlOfRecentPrescription, String urlOfLabTest, String created_at) {
        this.current_medication = current_medication;
        UrlOfRecentPrescription = urlOfRecentPrescription;
        UrlOfLabTest = urlOfLabTest;
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getCurrent_medication() {
        return current_medication;
    }

    public void setCurrent_medication(String current_medication) {
        this.current_medication = current_medication;
    }

    public String getUrlOfRecentPrescription() {
        return UrlOfRecentPrescription;
    }

    public void setUrlOfRecentPrescription(String urlOfRecentPrescription) {
        UrlOfRecentPrescription = urlOfRecentPrescription;
    }

    public String getUrlOfLabTest() {
        return UrlOfLabTest;
    }

    public void setUrlOfLabTest(String urlOfLabTest) {
        UrlOfLabTest = urlOfLabTest;
    }
}
