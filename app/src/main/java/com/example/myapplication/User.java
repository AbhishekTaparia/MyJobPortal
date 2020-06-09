package com.example.myapplication;

public class User {

    private String name;
    private String mobile;
    private String address;
    private String pinCode;
    private String degree;
    private String currentCity;
    private String gender;
    private String dob;
    private String jobApplied;
    private String cv;
    private String imageURL;

    public User() {
    }



    public User(String name, String mobile, String address, String pinCode,
                String degree, String currentCity, String gender, String dob, String jobApplied, String cv, String imageURL) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.pinCode = pinCode;
        this.degree = degree;
        this.currentCity = currentCity;
        this.gender = gender;
        this.dob = dob;
        this.jobApplied = jobApplied;
        this.cv = cv;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getJobApplied() {
        return jobApplied;
    }

    public void setJobApplied(String jobApplied) {
        this.jobApplied = jobApplied;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
