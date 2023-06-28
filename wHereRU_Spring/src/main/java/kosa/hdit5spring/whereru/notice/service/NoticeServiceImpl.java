package kosa.hdit5spring.whereru.notice.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	@Override
	public void requestToFCM(List<String> tokenList) {

		
		for(String token : tokenList) {
			 try {
				 	System.out.println(token);
		            String url = "https://fcm.googleapis.com/fcm/send";

		            // FCM ���� Ű
		            String serverKey = "AAAAQGwJreo:APA91bERH85R8sckereChqMrm1niq1MQh7qXOEXSESjpvn5eDPzt72z_1JT114p5IFv90z8dAeHJ88l62__SIKpkXuVdoDU1QVWbMgGna96_K297YIuEB9_A0OtX0lfiN1cAFtFswkuE";

		            // ���� ��� ����̽��� FCM ��ū
		            String targetToken = token;

		            // �޽��� ������
		            String message = "{ \"to\": \"" + targetToken + "\",\"priority\": \"high\", \"notification\": { \"title\": \"HI!!\", \"body\": \"Can you look at this??\" } }";
		            System.out.println(message);
		            URL obj = new URL(url);
		            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		            // Set request method
		            conn.setRequestMethod("POST");

		            // Set request headers
		            conn.setRequestProperty("Content-Type", "application/json");
		            conn.setRequestProperty("Authorization", "key=" + serverKey);

		            // Enable output stream and write the message data
		            conn.setDoOutput(true);
		            OutputStream os = conn.getOutputStream();
		            os.write(message.getBytes());
		            os.flush();
		            os.close();

		            int responseCode = conn.getResponseCode();
		            System.out.println("Response Code: " + responseCode);
		            
		            if (responseCode == HttpURLConnection.HTTP_OK) {
		                //������
		            } else {
		                // ���н�
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		}
		
	}
}
