package com.service;

import java.util.HashMap;

public class DALService {
	public String servicename;
	public String value;
	public String value1;
	
	public DALService( String servicename) {
		this.servicename = servicename;
	}
	
	public String getService() {
		return servicename;
	}
	
	public void setService(String servicename) {
		this.servicename=servicename;
		
	}
	
	public String getvalue() {
		return value;
	}
	
	public String getvalue1() {
		return value1;
	}
	
	public void setvalue(String value) {
		this.value=value;
	}
	
	public void setvalue1(String value) {
		this.value=value;
	}
}
