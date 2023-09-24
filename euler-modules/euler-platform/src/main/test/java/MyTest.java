import com.euler.common.core.utils.JsonUtils;
import com.euler.common.sms.SmsHelper;
import com.euler.platform.EulerPlatformApplication;
import com.euler.platform.config.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = EulerPlatformApplication.class)
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private SmsHelper smsHelper;
    @Autowired
    private WebConfig webConfig;

    @Test
    public  void testSendCode(){
        Map<String,String> codeMap=new HashMap<>();
        codeMap.put("code","254689");
        Boolean flag = smsHelper.sendSms("13716127206", webConfig.getSignName(), webConfig.getSmsTemplateCode(), JsonUtils.toJsonString(codeMap));

    }
}

