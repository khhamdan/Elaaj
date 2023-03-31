package com.hamdan.elaajmedicalapp.models;

public class TimeModel
{
    private String timeOfAppointment;
    private String colorOfResource;


    public TimeModel(String timeOfAppointment, String colorOfResource) {
        this.timeOfAppointment = timeOfAppointment;
        this.colorOfResource = colorOfResource;
    }
//
//    public TimeModel(String timeOfAppointment)
//    {
//        this.timeOfAppointment = timeOfAppointment;
//    }

    public void setTimeOfAppointment(String timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
    }
    public String getTimeOfAppointment() {
        return timeOfAppointment;
    }

    public String getColorOfResource() {
        return colorOfResource;
    }

    public void setColorOfResource(String colorOfResource) {
        this.colorOfResource = colorOfResource;
    }

}
