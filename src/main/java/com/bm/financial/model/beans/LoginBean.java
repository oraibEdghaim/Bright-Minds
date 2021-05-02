package com.bm.financial.model.beans;

public class LoginBean {

	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		LoginBean anotherObj = (LoginBean)obj;
		if(this.username.equals(anotherObj.getUsername()) && this.username.equals(anotherObj.getPassword())) {
			return true;
		}
		return false;
	}
	
	
}
