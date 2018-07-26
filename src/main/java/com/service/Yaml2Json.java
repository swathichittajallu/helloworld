package com.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;


public class Yaml2Json {
	public StringBuffer readyaml(String filename) {
		File file =new File(filename);
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader (file));
			String st;
			StringBuffer sb= new StringBuffer();
			while((st = br.readLine())!=null)
				sb.append(st+"\n");
			return sb;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONObject yaml2json(String yamlstring) {
		Yaml yaml= new Yaml();
	    @SuppressWarnings("unchecked")
		Map<String,Object> map= (Map<String, Object>) yaml.load(yamlstring);
	    JSONObject jsonObject=new JSONObject(map);
		return jsonObject;
	}
}
