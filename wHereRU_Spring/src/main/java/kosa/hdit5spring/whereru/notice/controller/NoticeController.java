package kosa.hdit5spring.whereru.notice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kosa.hdit5spring.whereru.notice.service.InAppMessagingService;
import kosa.hdit5spring.whereru.notice.service.NoticeService;

@RestController
public class NoticeController {

	@Autowired
	NoticeService noticeService;
	@Autowired
	InAppMessagingService inAppMessagingService;
	
	@GetMapping("sendRequest")
	public void sendRequest() {
		
//		List<String> tokenList = new ArrayList<String>();
//		tokenList.add("exmGr566QyK-xaWtBhRwwX:APA91bFvrFLt95pdmrrea_Qa_ZqH3P-AdW7R-NGspGKFdDPbvfQR60a3u0WwfA2GD_ZFEverBKDopMiUqbEwtaEur_XhtYBiY0DVz7Arn0sDGg8au8SSk-AI2zUhC00b5S3ohSu_ZPWu");
//		tokenList.add("dYKxoE9vSSaiapn3qoG3oZ:APA91bEQ5yoT9MS7S5a1xIzhhsMZyEI4-7kLWLmDUticIrpZHc84OddkJ_8FG7Zs-ygkPNSBBzDgh3z_NvfUXwVvEVhDGqqGfyL9d7alwZ2aqiGmcYkHGuN8fA0dTd_-nKmsdF6PEaVy");
//		tokenList.add("ezYV150HQni7HzCP3e2_BP:APA91bHH1FlIXD-0g_IX06WNkzaRz_aCkvVOO4RkDa2IbFJfyG9oAtf5gv-6sF8XRmCj-0fMVggcntMG45DmSTCoqBQtsTYPpDtSWdiUDwxtR3T_hWLvQi0uVWStDYU-y5e3M5i8o1gm");
//		tokenList.add("dwZTkMA2SfGmBSxr8iIpZN:APA91bEpImjL2GSUxcdPiRz-K6IV6zg5uXfCypVi33t2GOZvFASPrJxqmJYc_2W27UrJxLLDpm_rzYmNpIQ5oPMfvimnMXQkO6GZI9qgstd6ODRW-Khra9RA0M-DWJx80xj-lhjtZA0D");
//		tokenList.add("eZ5WjrdPTwa8VJZj20OBPB:APA91bEYWavvWMYlf-rjqWlMA1mtPV3n2ImUTNpe4HQ1e89_9td26OS1tLS7f_fvNKo0yaEKXWbOYg4KtVmRv7WQd4vgD6KHL-gsjhHyYhBvazSqLjKFHMPC8r5nnIOkNsAij35f6xK1");
//		tokenList.add("dxVdbWRjTnGyRtQvhdZdcF:APA91bGShwmUTiNNdeNuZnHZEJPTctL7D9JnozRzRQcngWQmAGkuC7Tah8okUjvCpZyLtstCasVDP6vPGLvBW_fv3WStcuDPQsdaaPI7xMn3h9_qdBAnehcfy3PNMU-MilRUD-ZBsLuR");
//		noticeService.requestToFCM(tokenList);
		inAppMessagingService.sendMessage("dwZTkMA2SfGmBSxr8iIpZN:APA91bEpImjL2GSUxcdPiRz-K6IV6zg5uXfCypVi33t2GOZvFASPrJxqmJYc_2W27UrJxLLDpm_rzYmNpIQ5oPMfvimnMXQkO6GZI9qgstd6ODRW-Khra9RA0M-DWJx80xj-lhjtZA0D", "test", "test");
	}
	
}
