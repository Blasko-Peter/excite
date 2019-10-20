package com.xcite.javatest.action.standalone;

public class Newslettersub {

	private int id;
	private boolean subscribed;
	private int listid;
	private String createdate;

	public Newslettersub(int id, boolean subscribed, int listid, String createdate) {
		this.id = id;
		this.subscribed = subscribed;
		this.listid = listid;
		this.createdate = createdate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}

	public int getListid() {
		return listid;
	}

	public void setListid(int listid) {
		this.listid = listid;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

}
