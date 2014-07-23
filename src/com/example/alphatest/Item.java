package com.example.alphatest;

/*
 * Item model*/

public class Item {
	
	private String title;
	private String pubDate;
	private String link;
	
	public Item(String title, String pubDate, String link){
		this.title = title;
		this.pubDate = pubDate;
		this.link = link;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getPubDate(){
		return pubDate;
	}
	
	public String getLink(){
		return link;
	}
	
	@Override
	public String toString(){
		return title + " " + pubDate + " " + link;
	}
}
