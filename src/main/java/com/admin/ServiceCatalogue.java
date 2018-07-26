package com.admin;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.service.DeployService;
import com.service.Yaml2Json;
import com.database.PostgreSQL;
import com.service.DALService;

@EnableAutoConfiguration
@RestController
public class ServiceCatalogue {
	HashMap hm1 = new HashMap();
	DeployService ds = new DeployService(null, null);
	PostgreSQL psql = new PostgreSQL();
	Yaml2Json y2j=new Yaml2Json();
	String deploymentStatus = "not deployed";
	
	@Autowired
	public RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	@RequestMapping(value = "/MiscService/getServicesOffered", method = RequestMethod.GET) 
	public String servicesAvailable(){
		Connection con = psql.connection();
		ArrayList al= psql.executeCommand(con,"SELECT servicename FROM service");
		return al.toString();
	}
	
	@RequestMapping("/endpoints")
	public @ResponseBody
	Object showEndpointsAction() throws SQLException
	{
			//System.out.println(requestMappingHandlerMapping.getHandlerMethods().keySet().stream().map(t->(t.getMethodsCondition().getMethods().toArray()[0])));
			Object[] ob;
			ob = requestMappingHandlerMapping.getHandlerMethods().keySet().stream().map(t ->
            (t.getMethodsCondition().getMethods().size() == 0 ? "GET" : t.getMethodsCondition().getMethods().toArray()[0]) + " " +                    
            t.getPatternsCondition().getPatterns().toArray()[0]
     ).toArray();
			ArrayList apis = new ArrayList();
			for(int i=0; i<ob.length;i++)
				apis.add(ob[0].toString().split(" ")[1]);
			System.out.println(apis);
	        return ob;
	 }
	
	@RequestMapping(value = "/admin/test", method = RequestMethod.GET) 
	public String test(){
		return "hello";
		
	}
	
	@RequestMapping(value = "/MiscService/addService", method = RequestMethod.POST) 
	public String servicesAvailable(@RequestBody String servicename,String tenantid){
		Connection con = psql.connection();
		psql.insert(con, tenantid, servicename,deploymentStatus);
		return "Service added to catalogue";
		
	}	
	
	@RequestMapping(value = "/MiscService/deployMonitoringService", method = RequestMethod.POST)
	public String deployMonitoringService(@RequestBody HashMap<String,String> service) {
		//validations
		if(service.get("servicename").equals("MS") && !service.containsKey("filename"))
			return "Missing configuration file for monitoring service!!";
		if(!service.containsKey("tenantName"))
			return "Mising tenantname!";
		if(!service.containsKey("regionName"))
			return "Missing RegionName";
		if(!service.containsKey("servicename"))
			return "Please specify the service to be deployed";
		if(service.get("servicename").equals("MS") && service.containsKey("filename") && service.containsKey("tenantName") && service.containsKey("regionName")) {
		
			System.out.println("Configuraion file: "+ service.get("filename"));
			JSONObject obj = new JSONObject();
			StringBuffer yamlstring = y2j.readyaml(service.get("filename"));
			obj = y2j.yaml2json(yamlstring.toString());
			Boolean value = ds.checkdeploy(service.get("servicename"));
			HashMap<String, Object> hm = new HashMap<String, Object>();

			if (value == true) {
				hm.put("Service", "deployed");
				return hm.toString();
			}
			else { 	
				hm.put("JSONObect", obj);
				hm.put("uri","/darwin/platform/monitor?tenantName="+service.get("tenantName")+"&regionName="+service.get("regionName")+"&serviceName=test");
				Thread t = new Thread(new DeployService("MS",hm));
				t.start();
				return hm.toString();
			}
		}
		else
			return "Please check the inputs given!";
	}
	
	@RequestMapping(value = "/MiscService/deploymentStatus", method = RequestMethod.POST)
	public String deploymentStatus(@RequestBody JSONObject obj) {
		String status;
		try {
			status = obj.get("status").toString();
			if (status == "deployed") 
				deploymentStatus = "deployed";
			else 
				deploymentStatus = "not deployed";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "Status is set";
	}
	
	@RequestMapping(value = "/MiscService/deployDAL", method = RequestMethod.POST)
	public String deployDAL(@RequestBody HashMap<String,String> service) {
		if(!service.containsKey("servicename"))
			return "Please specify the service to be deployed";
		if(service.get("servicename").equals("DAL") && !service.containsKey("filename"))
			return "Please provide input file for deploying DAL";
		if(service.get("servicename").equals("DAL") && service.containsKey("filename")) {
		
			System.out.println("Configuration file :"+service.get("filename"));
			JSONObject obj = new JSONObject();
			StringBuffer yamlstring = y2j.readyaml(service.get("filename"));
			obj = y2j.yaml2json(yamlstring.toString());
			String[] inputvalues= new String[] {"region","tenantName","serviceName","networkName","replicas","image","port","tracing","command"};
			for(int i=0;i<inputvalues.length;i++) {
				if(!obj.has(inputvalues[i]))
					return "Please provide " +inputvalues[i]+" to deploy DAL";
			}
			Boolean value = ds.checkdeploy(service.get("servicename"));
			HashMap<String, Object> hm = new HashMap<String, Object>();

			if (value == true) {
				hm.put("Service", "deployed");
				return hm.toString();
			}
			else { 	
				hm.put("JSONObect", obj);
				hm.put("uri","/darwin/dal/deploy");
				Thread t = new Thread(new DeployService("DAL",hm));
				t.start();
				return hm.toString();
		}
		}
		else
			return "Please check the inputs";
			
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceCatalogue.class, args);
	}

}
