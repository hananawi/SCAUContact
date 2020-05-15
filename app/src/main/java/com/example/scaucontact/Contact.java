package com.example.scaucontact;

import java.util.Date;

public class Contact {
    private String name; 
    private String phone;
    private String address;
    private String email;
    private int post;
    private Date birthday;
    private int avatarSource;
    private String workUnit;
    private String zipCode;
    private String groupName;
    private String remarks;
    public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getAvatarSource() {
		return avatarSource;
	}
	public void setAvatarSource(int avatarSource) {
		this.avatarSource = avatarSource;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String grouping) {
		this.groupName = grouping;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
    public String getName() {
        return name;
    }

    public void setName(String namee) {
        this.name = namee;
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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
   
    public Contact(String name, String phone, String address, String email, int post, Date birthday, int avatarSource,
			String workUnit, String zipCode, String groupName, String remarks) {
		super();
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.post = post;
		this.birthday = birthday;
		this.avatarSource = avatarSource;
		this.workUnit = workUnit;
		this.zipCode = zipCode;
		this.groupName = groupName;
		this.remarks = remarks;
	}
	public Contact(String name, String phone, int avatarSource){
    	this.name = name;
    	this.phone = phone;
    	this.avatarSource = avatarSource;
	}
	public Contact(String name, String phone, String groupName) {
        this.name = name;
        this.phone = phone;
        this.groupName = groupName;
    }
    @Override
    public String toString(){
    	
        return name+'\t'+phone+'\t'+address+'\t'+email+'\t'+post+'\t'+birthday+'\t'+'\t'+workUnit+'\t'+zipCode+'\t'+groupName+'\t'+remarks;
    }
}


