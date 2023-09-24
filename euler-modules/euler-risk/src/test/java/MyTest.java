import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.risk.EulerRiskApplication;

import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.domain.dto.BehaviorMqMsgDto;
import com.euler.risk.service.IBehaviorService;
import com.euler.risk.service.ITdDeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = EulerRiskApplication.class)
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private ITdDeviceInfoService deviceInfoService;
    @Autowired
    private IBehaviorService behaviorService;

    @Test
    public void testPublish() {
        RequestHeaderDto headerDto = new RequestHeaderDto();
        DeviceInfoDto dto = new DeviceInfoDto();
        dto.setUuid("uuuId_123232").setPushId("dfdfd").setMobileType("iphone19");
        headerDto.setPlatform("1").setVersion("1.0").setDevice("2").setAppId("n9pb6dbd87bf3583659").setDeviceInfo(dto);


        TdDeviceInfoVo deviceInfoByHeader = deviceInfoService.getDeviceInfoByHeader(headerDto);

        log.info("dd:{}", JsonHelper.toJson(deviceInfoByHeader));

    }

    @Test
    public void pulishMsg(){
        String msg="{\"behaviorType\":{\"code\":\"userLogin\",\"createBy\":\"1\",\"createTime\":\"2022-08-23 16:20:18\",\"delFlag\":\"0\",\"description\":\"统一登录ios_sdk\",\"device\":\"2\",\"id\":4,\"model\":\"用户登录\",\"name\":\"通用登录\",\"params\":{},\"path\":\"/auth/login\",\"platform\":\"1\",\"reflectExpression\":\"loginServiceImpl.getUserInfo\",\"type\":\"0\",\"updateBy\":\"1\",\"updateTime\":\"2022-08-23 16:20:21\"},\"ip\":\"1.192.178.149\",\"mobile\":\"\",\"msgId\":\"S202209081127426050000\",\"requestData\":\"{\\\"code\\\":\\\"rgGrGo9rzNadRK78vqF3-\\\",\\\"id\\\":\\\"eyJvIjoiaU9TIiwiayI6IkVc\\\",\\\"userType\\\":\\\"1\\\",\\\"loginType\\\":\\\"4\\\",\\\"userName\\\":\\\"3705704724\\\"}\",\"requestHeader\":{\"appId\":\"1649212176327\",\"device\":\"2\",\"deviceInfo\":{\"android\":null,\"idfa\":null,\"imei\":null,\"mac\":null,\"mobileType\":\"iPhone10,3\",\"oaid\":null,\"pushId\":null,\"uuid\":\"E169F5EE-8389-4BA3-AF87-3721A88AA0F5\"},\"nonce\":\"a1T131U4V7bNE64G\",\"packageCode\":\"default\",\"platform\":\"1\",\"sign\":\"ac0946c69dbd70612b53b3d0843f4e2a\",\"ticks\":1662607662513,\"token\":null,\"version\":\"1\"},\"userId\":0,\"userName\":\"\"}";
        BehaviorMqMsgDto behaviorMqMsgDto = JsonHelper.toObject(msg, BehaviorMqMsgDto.class);

        if (behaviorMqMsgDto != null) {
            behaviorService.save(behaviorMqMsgDto);
        }

    }
}

