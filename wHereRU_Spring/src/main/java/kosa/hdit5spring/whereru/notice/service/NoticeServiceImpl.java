package kosa.hdit5spring.whereru.notice.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.notice.mapper.NoticeMapper;
import kosa.hdit5spring.whereru.notice.vo.LocationVO;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	NoticeMapper noticeMapper;
	
	@Override
	public String getToken(String userId) {
		String token = noticeMapper.getToken(userId);
		return token;
	}
	
//	@Override
////	public List<String> getTokenList(int userSeq) {
////		List<String> tokenList = noticeMapper.getTokenList(userSeq);
////		return tokenList;
////	}
	//내 위치 정보 가져오기
	
	//다른 사람 위치정보 가져오기-토큰으로
	
	@Override
	public void sendingToOne(ChatVO chatvo) {	
		String token = getToken(chatvo.getChatReceiver());
		 try {
	            String url = "https://fcm.googleapis.com/fcm/send";

	            // FCM서버키
	            String serverKey = "AAAAQGwJreo:APA91bERH85R8sckereChqMrm1niq1MQh7qXOEXSESjpvn5eDPzt72z_1JT114p5IFv90z8dAeHJ88l62__SIKpkXuVdoDU1QVWbMgGna96_K297YIuEB9_A0OtX0lfiN1cAFtFswkuE";

	            // 요청보낼타겟디바이스토큰
	            String targetToken = token;

	            // 바디설정
	            String message = "{ \"to\": \"" + targetToken + "\",\"priority\": \"high\", \"notification\": { \"title\": \""+chatvo.getChatSender()+"\", \"body\": \""+chatvo.getChatContent()+"\" } }";
	            System.out.println(message);
	            URL obj = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

	            // 요청방식
	            conn.setRequestMethod("POST");

	            // 헤더설정
	            conn.setRequestProperty("Content-Type", "application/json");
	            conn.setRequestProperty("Authorization", "key=" + serverKey);

	            // 스트림생성 및 전송
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
		}
	@Override
	public void sendingToAll(List<String> tokenList) {
		
		for(String token : tokenList) {
			Thread t = new Thread(()->{
			 try {
		            String url = "https://fcm.googleapis.com/fcm/send";

		            // FCM서버키
		            String serverKey = "AAAAQGwJreo:APA91bERH85R8sckereChqMrm1niq1MQh7qXOEXSESjpvn5eDPzt72z_1JT114p5IFv90z8dAeHJ88l62__SIKpkXuVdoDU1QVWbMgGna96_K297YIuEB9_A0OtX0lfiN1cAFtFswkuE";

		            // 요청보낼타겟디바이스토큰
		           String targetToken = token;

		            // 바디설정
		            String message = "{ \"to\": \"" + targetToken + "\",\"priority\": \"high\", \"notification\": { \"title\": \"도움이 필요한 사람이 생겼어요!\", \"body\": \"당신의 따뜻한 손길이 필요해요!!\" } }";
		            System.out.println(message);
		            URL obj = new URL(url);
		            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		            // 요청방식
		            conn.setRequestMethod("POST");

		            // 헤더설정
		            conn.setRequestProperty("Content-Type", "application/json");
		            conn.setRequestProperty("Authorization", "key=" + serverKey);

		            // 스트림생성 및 전송
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
			} );
			t.start();
	}		
	}	
}
