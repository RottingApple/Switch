package Model;

import java.util.ArrayList;

public class AclList{
	private ArrayList<AclRule>aclList;
	private int portnumber;
	public AclList(int portnumber){
		this.portnumber = portnumber;
		aclList = new ArrayList<AclRule>();
	}
	public ArrayList<AclRule> getAclList() {
		return aclList;
	}
	public void setAclList(ArrayList<AclRule> aclList) {
		this.aclList = aclList;
	}
	public int getPortnumber() {
		return portnumber;
	}
	public void setPortnumber(int portnumber) {
		this.portnumber = portnumber;
	}
	
	public void addToList(AclRule rule){
		synchronized(aclList){
			aclList.add(rule);
		}
	}
	public void removeFromList(int number){
		synchronized(aclList){
			aclList.remove(number);
		}
	}
	public boolean checkList(){
		synchronized(aclList){
			return false;
		}
	}
}
