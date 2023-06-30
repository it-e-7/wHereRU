package kosa.hdit5spring.whereru.notice.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	NoticeMapper noticeMapper;
	
	@Override
	public List<String> getTokenList(int userSeq) {
		List<String> tokenList = noticeMapper.getTokenList(userSeq);
		return tokenList;
	}
	
	@Override
	public void requestToFCM(List<String> tokenList) {

		for(String token : tokenList) {
			Thread t = new Thread(()->{
			 try {
				 	System.out.println(token);
		            String url = "https://fcm.googleapis.com/fcm/send";

		            // FCM서버키
		            String serverKey = "AAAAQGwJreo:APA91bERH85R8sckereChqMrm1niq1MQh7qXOEXSESjpvn5eDPzt72z_1JT114p5IFv90z8dAeHJ88l62__SIKpkXuVdoDU1QVWbMgGna96_K297YIuEB9_A0OtX0lfiN1cAFtFswkuE";

		            // 요청보낼타겟디바이스토큰
		            String targetToken = token;

		            // 바디
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
		                
		            } else {
		                
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			});
			t.start();
		}
		
	}
}
