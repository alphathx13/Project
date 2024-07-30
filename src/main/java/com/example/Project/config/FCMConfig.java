//package com.example.Project.config;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//
//@Component
//public class FCMConfig {
//	
//	String gcpCredentialFile = "project-firebase.json";
//	FirebaseOptions options;
//	
//	private FirebaseApp createFirebaseApp() {
//	    ClassPathResource resource = new ClassPathResource(gcpCredentialFile);
//	    
//	    try (InputStream is = resource.getInputStream()) {
//	    	options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(is)).build();
//	    } catch (IOException e) {
//	    	// error
//	    }
//
//	    return FirebaseApp.initializeApp(options);
//	}
//}
