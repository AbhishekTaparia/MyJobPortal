package com.example.myapplication;

public class Job {
    private String jobPosition;
    private String company;
    private String location;
    private String jobDesc;
    private String companyDesc;
    private String salary;
    private String key;


    public Job(){
        // Empty Constructor
    }


    public Job(String jobPosition, String company, String location, String jobDesc, String companyDesc, String salary, String key) {
        this.jobPosition = jobPosition;
        this.company = company;
        this.location = location;
        this.jobDesc = jobDesc;
        this.companyDesc = companyDesc;
        this.salary = salary;
        this.key = key;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
