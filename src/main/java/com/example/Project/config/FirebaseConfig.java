package com.example.Project.config;

import java.io.FileInputStream;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig implements WebMvcConfigurer {

	public void init() {
		try {
			FileInputStream serviceAccountFile = new FileInputStream("src/main/resources/project-firebase-adminsdk.json");
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccountFile))
					.setDatabaseUrl("https://example-ac805.firebaseio.com").build();

			FirebaseApp.initializeApp(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}