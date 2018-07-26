package com.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.ini4j.Ini;

import com.cluster.ClusterDetails;
import com.database.PostgreSQL;
import com.jcraft.jsch.Session;

public class DeployService extends ServiceDeployments implements Runnable{
	private HashMap<String, Object> hm;
	private String servicename;
	static PostgreSQL psql =new PostgreSQL();
//	ClusterDetails cd = new ClusterDetails();
//	Ini ini = cd.readconfig("/home/tcs/mddp_misc/darwin-misc-service.conf");
//	HashMap clusterdetails = cd.getClusterDetails();
//	String Url = (String) clusterdetails.get("k8s.master.api");

	
	public DeployService(String servicename,HashMap<String, Object> hm) {
		this.servicename=servicename;
		this.hm=hm;
	}

	public void deploy(String servicename, HashMap<String, Object> hm) {
		ClusterDetails cd = new ClusterDetails();
		Ini ini = cd.readconfig("/home/tcs/mddp_misc/darwin-misc-service.conf");
		HashMap details = cd.getDBDetails(ini);
		DeploymentObjects obj=new DeploymentObjects();
		try {
			obj.postHttpRequest((String)details.get("deployment.endpoint")+hm.get("uri"), hm.get("JSONObect").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("**************Request is routed and deployment is initiated*****************");
	}

	public boolean checkdeploy(String servicename) {
		Connection con = psql.connection();
		ArrayList al= psql.executeCommand(con,"SELECT status FROM service where servicename='"+servicename+"'");
		System.out.println("Status of the service deployment: "+al);
    	if(al.contains("deployed")){
    		return true;
    	}
    	else
    		System.out.println("Service is not deployed. Scheduled the deployment");
    		return false;

	}
	
//	public boolean createK8sNamespace(String tenantName) throws ClientProtocolException, IOException {
//		
//		boolean ns_flag = checkIfTenantNSExists(tenantName);
//		if (ns_flag) {
//			return true;
//		} else {
//			String payloadString = "{\"apiVersion\":\"v1\",\"kind\":\"Namespace\",\"metadata\":" + "{\"name\":\""
//					+ tenantName + "\"}}";
//			DeploymentObjects obj=new DeploymentObjects();
//			int status = obj.postHttpRequest(Url, payloadString);
//			if (status == 200 || status == 201) {
//				System.out.println("Namespace " + tenantName + " created in K8s");
//
//				//ServiceCreation.namespaces.put(tenantName);
//				//System.out.println("Microservice components deployed so far "+ServiceCreation.createdObjects);
//				//ServiceCreation.createdObjects.put("namespaces", ServiceCreation.namespaces);
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}
//	
//	public boolean checkIfTenantNSExists(String tenant) throws IOException {
//		String url_ns = Url + tenant + "/";
//		URL url = new URL(url_ns);
//		HttpURLConnection con = (HttpURLConnection) url.openConnection();
//		con.setRequestMethod("GET");
//		int status = con.getResponseCode();
//		if (status == 200 || status == 201)
//			return true;
//		else
//			return false;
//	}
//	
	@Override
	public void run() {
		deploy(servicename,hm);
	}

}
