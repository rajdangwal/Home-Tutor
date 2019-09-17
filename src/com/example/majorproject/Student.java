package com.example.majorproject;

import java.io.Serializable;

public class Student implements Serializable{
	
	int studentid;
    String name;
    String email;
    String phone;
    String address;
    String institution;
    String area;
    String gender;
    int standard;
    String password;
    
    public Student()
    {
    	
    }
    
	public Student(int studentid, String name, String email, String phone,
			String address, String institution, String area, String gender,
			int standard, String password) {
		super();
		this.studentid = studentid;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.institution = institution;
		this.area = area;
		this.gender = gender;
		this.standard = standard;
		this.password = password;
	}

	public int getStudentid() {
		return studentid;
	}

	public void setStudentid(int studentid) {
		this.studentid = studentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getStandard() {
		return standard;
	}

	public void setStandard(int standard) {
		this.standard = standard;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
    

}
