package com.example.majorproject;

import java.io.Serializable;

public class Tutor implements Serializable{
    
    int tutorid;
    String name;
    String email;
    String phone;
    String address;
    String qualification;
    String occupation;
    String gender;
    String password;
    String ts1status;
    String ts2status;
    int approved;
    
    
    public Tutor()
    {
        
    }

    public Tutor(int tutorid, String name, String email, String phone, String address, String qualification, String occupation, String gender, String password, String ts1status, String ts2status, int approved) {
        super();
        this.tutorid = tutorid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.qualification = qualification;
        this.occupation = occupation;
        this.password = password;
        this.gender = gender;
        this.ts1status = ts1status;
        this.ts2status = ts2status;
        this.approved = approved;
    }

    public void setTutorid(int tutorid) {
        this.tutorid = tutorid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setTs1status(String ts1status) {
        this.ts1status = ts1status;
    }

    public void setTs2status(String ts2status) {
        this.ts2status = ts2status;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getTutorid() {
        return tutorid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getQualification() {
        return qualification;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getTs1status() {
        return ts1status;
    }

    public String getTs2status() {
        return ts2status;
    }

    public int getApproved() {
        return approved;
    }

    
    
    
}
