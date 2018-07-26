package com.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSCH {
	//public JSch jsch;
	//public Session session;
	public ChannelExec channel;

	public String username,host,password;
	public JSCH(String username,String host,String password) {
		this.username=username;
		this.host=host;
		this.password=password;
	}
	
	public Session connect() {
		try {
			JSch jsch=new JSch();
			Session session=jsch.getSession(username, host, 22);
			Properties config=new Properties();
			config.put("StrictHostKeyChecking","no");
			session.setConfig(config);
			session.setPassword(password);
			session.connect();
			
		return session;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String executeCommand(Session session,String command) {
		try {
			channel = (ChannelExec) session.openChannel("exec");
			BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			channel.setCommand(command);
			channel.connect();
			String result="",msg="";
			while((msg=br.readLine())!=null) {
				result=result+"\n"+msg;
				System.out.println(msg);}
			int status = channel.getExitStatus();
			channel.disconnect();
			return result;
		} catch (JSchException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
