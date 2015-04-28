package edu.sjsu.picshare;

public class Album {
	public String id;
	public String title;
	public String desc;
	public String imgUrl;

	public Album(String title, String imgUrl) {
		this.title = title;
		this.imgUrl = imgUrl;
	}
	
	public Album() {		
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDesc() {
		return this.desc;
	}
}