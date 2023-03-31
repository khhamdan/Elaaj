package com.hamdan.elaajmedicalapp.models;

public class DoctorSpeciality {
    private String DoctorSpecialityText;
    private Integer specialityIcon;

    public DoctorSpeciality(String doctorSpecialityText, Integer specialityIcon) {
        DoctorSpecialityText = doctorSpecialityText;
        this.specialityIcon = specialityIcon;
    }

    public String getDoctorSpecialityText() {
        return DoctorSpecialityText;
    }

    public void setDoctorSpecialityText(String doctorSpecialityText) {
        DoctorSpecialityText = doctorSpecialityText;
    }

    public Integer getSpecialityIcon() {
        return specialityIcon;
    }

    public void setSpecialityIcon(Integer specialityIcon) {
        this.specialityIcon = specialityIcon;
    }
}
