package com.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;



public class DeploymentObjects {
	String url = "http://10.138.77.40:8001";
	
	public int postHttpRequest(String url, String payloadString) throws ClientProtocolException, IOException {
		System.out.println("Deployment url is: "+url);
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		
//	    ArrayList<BasicNameValuePair> postParameters;
//	    postParameters = new ArrayList<BasicNameValuePair>();
//	    postParameters.add(new BasicNameValuePair("regionName", "Hyderabad"));
//	    postParameters.add(new BasicNameValuePair("tenantName", "default"));
//	    postParameters.add(new BasicNameValuePair("serviceName", "test"));
//        post.setEntity(new UrlEncodedFormEntity(postParameters));
//	    
		StringEntity entity = new StringEntity(payloadString);
		post.setEntity(entity);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(post);
		System.out.println("Post request "+response.getStatusLine().getStatusCode());
		
		return response.getStatusLine().getStatusCode();
	}
	
	public int patchHttpRequest(String url,String payloadString) throws ClientProtocolException, IOException {
		System.out.println("Deployment url is: "+url);
		HttpPatch patch = new HttpPatch(url);
		patch.setHeader("Content-Type", "application/json-patch+json");
		StringEntity entity = new StringEntity(payloadString);
		patch.setEntity(entity);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(patch);
		System.out.println("Patch request "+response.getStatusLine().getStatusCode());
		return response.getStatusLine().getStatusCode();
	}
	
	
	
	public int deleteHttpRequest(String url) throws ClientProtocolException, IOException {
		HttpDelete delete = new HttpDelete(url);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(delete);
		System.out.println("Delete request "+response.getStatusLine().getStatusCode());
		return response.getStatusLine().getStatusCode();
	}
	
	public int getHttpRequest(String url) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(get);
		System.out.println("Get request "+response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == 200) {
			ResponseHandler<String> handler = new BasicResponseHandler();
			String body = handler.handleResponse(response);
			System.out.println(body);
		}
		return response.getStatusLine().getStatusCode();
	}
	
	public boolean deployK8sClusterrole (JSONObject obj) {
		String uri = "/apis/rbac.authorization.k8s.io/v1beta1/clusterroles/";
		try {
			String cr_name =  ((JSONObject) obj.get("metadata")).getString("name");
			String cr_url = url+uri;
			int responsecode = postHttpRequest(cr_url,obj.toString());
			System.out.println("ResponseCode for deploying clusterrole " +"\""+cr_name+"\":"+responsecode);
			if(responsecode == 201)  
				System.out.println("Clusterrole "+cr_name+" deployed!");
			if(responsecode == 409) {
				System.out.println("Clusterrole "+cr_name+" already exists");
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteK8sObject (String uri, String objectname) {
		//String uri = "/apis/rbac.authorization.k8s.io/v1beta1/clusterroles/";
		try {
			String cr_url = url+uri+objectname;
			int responsecode = deleteHttpRequest(cr_url);
			if(responsecode == 200)  
				System.out.println(objectname+" deleted!");
			if(responsecode == 404) {
				System.out.println(objectname+" does not exist");
			}
			System.out.println("ResponseCode for deleting object " +"\""+objectname+"\":"+responsecode);
			return true;
		} catch ( IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean getK8sObject (String uri,String objectname) {
		//String uri = "/apis/rbac.authorization.k8s.io/v1beta1/clusterroles/";
		try {
			String cr_url = url+uri+objectname;
			int responsecode = getHttpRequest(cr_url);
			if(responsecode == 200)  
				System.out.println(objectname+" obtained!");
			if(responsecode == 404) {
				System.out.println(objectname+" does not exist");
			}
			System.out.println("ResponseCode for getting object " +"\""+objectname+"\":"+responsecode);
			return true;
		} catch ( IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deployK8sRole (JSONObject obj, String tenantName) {
		String uri = "/apis/rbac.authorization.k8s.io/v1/namespaces/"+tenantName+"/roles";
		try {
			System.out.println(obj);
			String role_name =  ((JSONObject) obj.get("metadata")).getString("name");
			System.out.println(role_name);
			String role_url = url+uri;
			System.out.println(role_url);
			int responsecode = postHttpRequest(role_url,obj.toString());
			System.out.println("ResponseCode for deploying role " +"\""+role_name+"\":"+responsecode);
			if(responsecode == 201) { 
				System.out.println("Role "+role_name+" deployed in "+tenantName+"!");
				return true;
			}
			if(responsecode == 409) {
				System.out.println("Role "+role_name+" already exists in tenant "+tenantName);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean deployK8sRoleBinding (JSONObject obj,String tenantName) {
		String uri = "/apis/rbac.authorization.k8s.io/v1/namespaces/"+tenantName+"/rolebindings";
		try {
			System.out.println(obj);
			String rolebinding_name =  ((JSONObject) obj.get("metadata")).getString("name");
			System.out.println(rolebinding_name);
			String rolebinding_url = url+uri;
			System.out.println(rolebinding_url);
			int responsecode = postHttpRequest(rolebinding_url,obj.toString());
			System.out.println("ResponseCode for deploying rolebinding " +"\""+rolebinding_name+"\":"+responsecode);
			if(responsecode == 201) { 
				System.out.println("Role "+rolebinding_name+" deployed in "+tenantName+"!");
				return true;
			}
			if(responsecode == 409) {
				System.out.println("Rolebinding "+rolebinding_name+" already exists in tenant "+tenantName);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return false;		
	}
	

	public boolean deployK8sClusterRoleBinding (JSONObject obj) {
		String uri = "/apis/rbac.authorization.k8s.io/v1beta1/clusterrolebindings/";
		try {
			String cr_name =  ((JSONObject) obj.get("metadata")).getString("name");
			String cr_url = url+uri;
			int responsecode = postHttpRequest(cr_url,obj.toString());
			System.out.println("ResponseCode for deploying clusterrolebinding " +"\""+cr_name+"\":"+responsecode);
			if(responsecode == 201) {  
				System.out.println("Clusterrolebinding "+cr_name+" deployed!");
				return true;
			}
			if(responsecode == 409) {
				System.out.println("Clusterrolebinding "+cr_name+" already exists");
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean deployK8sVirtualMachines (JSONObject obj, String tenantName, String vm_type) {
		String uri = "/apis/kubevirt.io/v1alpha1/namespaces/"+tenantName+"/"+vm_type;
		try {
			System.out.println(obj);
			String vm_name =  ((JSONObject) obj.get("metadata")).getString("name");
			System.out.println(vm_name);
			String vm_url = url+uri;
			System.out.println(vm_url);
			int responsecode = postHttpRequest(vm_url,obj.toString());
			System.out.println("ResponseCode for deploying "+vm_type +" \""+vm_name+"\":"+responsecode);
			if(responsecode == 201) { 
				System.out.println(vm_type + " "+ vm_name+" deployed in "+tenantName+"!");
				return true;
			}
			if(responsecode == 409) {
				System.out.println(vm_type+ " "+vm_name+" already exists in tenant "+tenantName);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean patchK8sOfflineVirtualMachine (String tenantName, String ovm_name) {
		String uri = "/apis/kubevirt.io/v1alpha1/namespaces/"+tenantName+"/offlinevirtualmachines/"+ovm_name;
		try {
			String vm_url = url+uri;
			System.out.println(vm_url);
			int responsecode = patchHttpRequest(vm_url,"{\"spec\":{\"running\": true}}");
			System.out.println("ResponseCode for deploying virtual machine " +"\""+ovm_name+"\":"+responsecode);
			if(responsecode == 201) { 
				System.out.println("Virtual machine "+ovm_name+" patched in "+tenantName+"!");
				return true;
			}
			if(responsecode == 409) {
				System.out.println("Virtual machine "+ovm_name+" already exists in tenant "+tenantName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	public boolean deployK8sRC (JSONObject obj, String tenantName) {
		String uri = "/api/v1/namespaces/"+tenantName+"/replicationcontrollers";
		try {
			System.out.println(obj);
			String vm_name =  ((JSONObject) obj.get("metadata")).getString("name");
			System.out.println(vm_name);
			String vm_url = url+uri;
			System.out.println(vm_url);
			int responsecode = postHttpRequest(vm_url,obj.toString());
			System.out.println("ResponseCode for deploying Replication Controller " +"\""+vm_name+"\":"+responsecode);
			if(responsecode == 201) { 
				System.out.println("Replication Controller "+vm_name+" deployed in "+tenantName+"!");
				return true;
			}
			if(responsecode == 409) {
				System.out.println("Replication Controller "+vm_name+" already exists in tenant "+tenantName);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	public boolean deployK8sObject (JSONObject obj, String tenantName) {
			System.out.println(obj);
		return false;
		
	}
	
	public static void main (String args[]) {
		DeploymentObjects depobj=new DeploymentObjects();
		/*
		Yaml2Json y2j = new Yaml2Json();
		JSONObject obj=new JSONObject();
		StringBuffer yamlstring = y2j.readyaml("/home/tcs/vm-with-preset.json");
		obj = y2j.yaml2json(yamlstring.toString());
		System.out.println(obj);
		Boolean value = depobj.deployK8sVirtualMachines(obj,"default","offlinevirtualmachines");
		System.out.println(value);
		*/
		//Boolean value = depobj.deployK8sRC(obj,"default");
		

		/*
		String result = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("/home/tcs/deployment.json"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();

			System.out.println(result);
			JSONObject obj = new JSONObject(result);
			JSONArray jsonar = new JSONArray();
			jsonar = (JSONArray) obj.get("k8Objects");
			JSONObject jsonob = new JSONObject();
			for (int i = 0; i < jsonar.length(); i++) {
				jsonob = (JSONObject) jsonar.get(i);
				System.out.println(jsonob);
				String kind = jsonob.getString("kind");
				System.out.println(kind);
				if (kind.equals("VirtualMachine")) {
					Boolean value = depobj.deployK8sVirtualMachines(jsonob, "default", "virtualmachinepresets");
				}
				if (kind.equals("ReplicationController")) {
					Boolean value = depobj.deployK8sRC(jsonob, "default");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		*/
	  Boolean value = depobj.patchK8sOfflineVirtualMachine("auriga", "vm-monika");
	  System.out.println(value);

		
		///apis/rbac.authorization.k8s.io/v1beta1/clusterroles/
		//Boolean value = depobj.deployK8sVirtualMachine(obj,"default");
		//Boolean value = depobj.deleteK8sObject("/apis/rbac.authorization.k8s.io/v1/namespaces/default/rolebindings/","readpods");
		//Boolean value = depobj.getK8sObject("/apis/rbac.authorization.k8s.io/v1/namespaces/default/rolebindings/","readpods");
		//System.out.println(value);

}}
