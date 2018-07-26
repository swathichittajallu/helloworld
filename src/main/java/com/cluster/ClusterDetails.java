package com.cluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.springframework.boot.SpringApplication;

import com.admin.ServiceCatalogue;

public class ClusterDetails {
	Ini ini = new Ini();
	public static void main(String args[]) {
		ClusterDetails cd = new ClusterDetails();
		String filename= args[0];
		//String filename= "/home/tcs/mddp_misc/darwin-misc-service.conf";
		SpringApplication.run(ServiceCatalogue.class, args);

	}

	public Ini readconfig(String string) {
	try {
			ini.load(new FileReader (string));
			return ini;
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return ini;
	}
	
	public static HashMap<String, String> getClusterDetails(Ini ini) {
			HashMap<String, String> hm = new HashMap<String, String>();
			Section section = ini.get("Regions");
			hm.put("masterapi", section.get("k8s.master.api"));
			int numofworkernodes;
			numofworkernodes = (section.get("docker.api.list").split(",").length)-1;
			for (int i=0; i<numofworkernodes;i++) {
			hm.put(("workernode"+i),section.get("docker.api.list").split(",")[i+1]);
			}
			return hm;
		}
		
	public static HashMap<String, Object> getDBDetails(Ini ini) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		Section section = ini.get("database");
		hm.put("database", section.get("db.name"));
		hm.put("db.username", section.get("db.username"));
		hm.put("db.password",section.get("db.password"));
		hm.put("deployment.endpoint",section.get("service.deployment.endpoint"));
		
		return hm;
	}
	
	public static HashMap<String, Object> getQueueDetails(Ini ini) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		Section section = ini.get("Rabbit-MQ");
		hm.put("queuehostip", section.get("rabbitmq.host.ip"));
		hm.put("queuehostusername", section.get("rabbitmq.host.username"));
		hm.put("queuehostpassword",section.get("rabbitmq.host.password"));
		return hm;
	}
	
	}

