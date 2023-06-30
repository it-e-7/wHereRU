package kosa.hdit5spring.whereru.notice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InAppMessagingImpl implements InAppMessagingService {

	    private final String serviceAccountKeyPath = "src/main/resources/firebase/whereru-10bf0-firebase-adminsdk-jvb03-e141c2e13a.json";
	    
	    //초기화
	    @Override
	    public void initializeFirebase() throws IOException {
	    	
	    	
	    	Resource resource = new ClassPathResource("firebase/whereru-10bf0-firebase-adminsdk-jvb03-e141c2e13a.json");
	    	File file = resource.getFile();	    	
	        FirebaseOptions options = new FirebaseOptions.Builder()
	                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(file.getAbsolutePath())))
	                .build();
	        FirebaseApp.initializeApp(options);
	    }
	    
	    @Override
	    public void sendMessage(String deviceToken, String title, String body) {
	    	System.out.println("method실행");
	        Message message = Message.builder()
	                	.setToken(deviceToken)
	                	.setNotification(Notification.builder()
	                					.setTitle(title)
	                					.setBody(body)
	                					.build())
	                	.build();

	        try {
	        	initializeFirebase();
	        	String result = FirebaseMessaging.getInstance().send(message);
	        	System.out.println("결과"+result);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	

}
