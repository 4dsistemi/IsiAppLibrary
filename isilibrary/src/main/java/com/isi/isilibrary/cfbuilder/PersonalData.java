package com.isi.isilibrary.cfbuilder;

public class PersonalData {
    private String name;
    private String surname;
    private String dd;
    private String mm;
    private String yy;
    private String birthplace;
    private boolean gender; //true-->Male

    public  PersonalData(String name, String surname, String dd, String mm, String yy, boolean gender, String birthplace) {
        try {
            this.setName(name);
            this.setSurname(surname);
            this.setDd(dd);
            this.setMm(mm);
            this.setYy(yy);
            this.setBirthplace(birthplace);
            this.setGender(gender);
        }catch (Error e)
        {
            throw new RuntimeException("Something goes wrong in new PersonalData",e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname.toUpperCase();
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

}
