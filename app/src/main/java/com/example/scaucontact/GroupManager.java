package com.example.scaucontact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class GroupManager {
	private ArrayList<Group>ateam = new ArrayList<>();//所有分组的集合存储单个分组

	public GroupManager(ArrayList<String> groupNameArray){
//		groupNameArray.add("家人");
//		groupNameArray.add("朋友");
		for(String i: groupNameArray){
			addSingleteam(new Group(i));
		}
	}

	public  GroupManager(){}

	public ArrayList<Group> getAteam() {
		return ateam;
	}

	public void setAteam(ArrayList<Group> ateam) {
		this.ateam = ateam;
	}
	
	public boolean addSingleteam(Group singleteam) {
		this.ateam.add(singleteam);//在所有分组里增加单个分组
		return true;
	}
	
	public Group getSingleteam(String teamname) {
		for(int i=0;i<this.ateam.size();i++) {
			Group singleteam1=this.ateam.get(i);//在所有分组里查找单个分组
			if(teamname.equals(singleteam1.getTeamname())) return this.ateam.get(i);
		}
		return null;
	}
	
	public boolean removePerson(Group singleteam) {
		Iterator<Group>iterator=this.ateam.iterator();
		while(iterator.hasNext()) {
			Group singleteam1=iterator.next();
			if(singleteam1.getTeamname()==singleteam.getTeamname()) {
				iterator.remove();//在所有分组里删除单个分组
				return true;
			}
		}
		return false;
	}

	public String[] getAllGroupName(){
		String[] ret = new String[ateam.size()];
		for(int i = 0; i < ateam.size(); i++){
			ret[i] = ateam.get(i).getTeamname();
		}
		return ret;
	}

	public LinkedList<Contact> getAllContacts(){
		LinkedList<Contact> ret = new LinkedList<>();
		for(Group i: ateam){
			ret.addAll(i.getSteam());
		}
		return ret;
	}
}