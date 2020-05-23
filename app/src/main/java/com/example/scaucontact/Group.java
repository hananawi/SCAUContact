package com.example.scaucontact;

import java.util.Iterator;
import java.util.LinkedList;

public class Group {
	private LinkedList<Contact>steam=new LinkedList<>();//单个分组存储联系人
	private String teamname;//分组名

	public Group(String teamname){
		this.teamname = teamname;
	}
	
	public LinkedList<Contact> getSteam() {
		return steam;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public void setSteam(LinkedList<Contact> steam) {
		this.steam = steam;
	}
	
	public boolean addPerson(Contact person) {
		this.steam.add(person);//在分组里增加联系人
		return true;
	}
	
	public Contact getPerson(String name) {
		for(int i=0;i<this.steam.size();i++) {
			Contact person1=this.steam.get(i);//在分组里查找联系人
			if(name.equals(person1.getName())) return this.steam.get(i);
		}
		return null;
	}
	
	public boolean removePerson(Contact person) {
		Iterator<Contact>iterator=this.steam.iterator();
		while(iterator.hasNext()) {
			Contact person1=iterator.next();
			if(person1.getPhone().equals(person.getPhone())) {
				iterator.remove();//在分组里删除联系人
				return true;
			}
		}
		return false;
	}
}
