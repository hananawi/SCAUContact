package com.example.scaucontact;

import java.util.Date;
import java.util.LinkedList;

public class Contact {
    private String name; 
    private String phone;
    private String address;
    private String email;
//    private Date birthday;
//    private int avatarSource;
    private String workUnit;
    private String zipCode;
    private String remarks;
    private boolean delete = false;
    private boolean display = false;
    private LinkedList<String> groups;


//    public Date getBirthday() {
//    	if(birthday==null) return "null";
//		return birthday;
//	}
//	public void setBirthday(Date birthday) {
//		this.birthday = birthday;
//	}


//	public int getAvatarSource() {
//    	if(==null) return "null";
//		return avatarSource;
//	}
//	public void setAvatarSource(int avatarSource) {
//		this.avatarSource = avatarSource;
//	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Contact){
			Contact tmp = (Contact)obj;
			return tmp.name.equals(name) && tmp.phone.equals(phone);
		}
		return false;
	}

	public LinkedList<String> getGroups(){
		return groups;
	}
	public void setGroups(LinkedList<String> groups){
		this.groups = groups;
	}

	public boolean getDisplay(){
		return display;
	}
	public void setDisplay(boolean display){
		this.display = display;
	}

	public boolean getDelete(){
		return delete;
	}
	public void setDelete(boolean delete){
		this.delete = delete;
	}

	public String getWorkUnit() {
    	if(workUnit.equals("")) return "null";
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getZipCode() {
    	if(zipCode.equals("")) return "null";
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getRemarks() {
    	if(remarks.equals("")) return "null";
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
    public String getName() {
    	if(name.equals("")) return "null";
        return name;
    }

    public void setName(String namee) {
        this.name = namee;
    }
    public String getPhone() {
    	if(phone.equals("")) return "null";
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
    	if(address.equals("")) return "null";
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
    	if(email.equals("")) return "null";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
   
    public Contact(String name, String phone, String email, String workUnit,
				   String address, String zipCode, String remarks, LinkedList<String> groups) {
		super();
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.workUnit = workUnit;
		this.zipCode = zipCode;
		this.remarks = remarks;
		this.groups = groups;
	}
    @Override
    public String toString(){
        return name+'\t'+phone+'\t'+address+'\t'+email+'\t'+workUnit+'\t'+zipCode+'\t'+remarks;
    }
}


