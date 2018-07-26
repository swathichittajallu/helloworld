package com.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.admin.ServiceCatalogue;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCatalogue.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void performInitialActivities() {
	    deployServiceMesh();
	}
	
	public void deployServiceMesh() {
			
	}	
}
