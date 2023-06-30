package kosa.hdit5spring.whereru.notice.service;

import java.io.IOException;

public interface InAppMessagingService {
	void initializeFirebase() throws IOException;
	void sendMessage(String deviceToken, String title, String body);
	
}
