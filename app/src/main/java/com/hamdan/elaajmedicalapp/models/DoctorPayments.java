package com.hamdan.elaajmedicalapp.models;

public class DoctorPayments
{
    String date,amountReceived;

    public DoctorPayments(String date, String amountReceived) {
        this.date = date;
        this.amountReceived = amountReceived;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }
}
