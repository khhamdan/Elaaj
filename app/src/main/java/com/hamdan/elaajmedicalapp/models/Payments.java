package com.hamdan.elaajmedicalapp.models;

public class Payments
{
    String amount;

    public Payments(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
