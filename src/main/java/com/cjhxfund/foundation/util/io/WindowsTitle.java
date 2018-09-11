package com.cjhxfund.foundation.util.io;

public class WindowsTitle {

	private String name;
	private int start;
	private int end;
	
	
	public WindowsTitle(String title, int start) {
		this.start = start;
		this.end = start + title.length();
		this.name = title.trim();
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
	
	
}
