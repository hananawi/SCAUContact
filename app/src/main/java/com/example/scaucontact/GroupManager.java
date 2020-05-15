package com.example.scaucontact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class GroupManager {
	private ArrayList<Group>ateam = new ArrayList<Group>();//所有分组的集合存储单个分组

	public GroupManager(Set<String> groupNameSet){
//		addSingleteam(new Group("家人"));
//		addSingleteam(new Group("朋友"));
		for(String i: groupNameSet){
			addSingleteam(new Group(i));
		}
	}

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
}