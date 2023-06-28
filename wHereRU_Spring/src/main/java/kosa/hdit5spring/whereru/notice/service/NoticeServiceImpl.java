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

		            // FCM 서버 키
		            String serverKey = "AAAAQGwJreo:APA91bERH85R8sckereChqMrm1niq1MQh7qXOEXSESjpvn5eDPzt72z_1JT114p5IFv90z8dAeHJ88l62__SIKpkXuVdoDU1QVWbMgGna96_K297YIuEB9_A0OtX0lfiN1cAFtFswkuE";

		            // 수신 대상 디바이스의 FCM 토큰
		            String targetToken = token;

		            // 메시지 데이터
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
		                //성공시
		            } else {
		                // 실패시
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		}
		
	}
}
