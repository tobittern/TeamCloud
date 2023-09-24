import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.statistics.EulerStatisticsApplication;
import com.euler.statistics.service.IRechargeStatService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@Slf4j
@SpringBootTest(classes = EulerStatisticsApplication.class)
@RunWith(SpringRunner.class)
public class MyTestStat {

  @Autowired
  private IRechargeStatService rechargeStatService;

  @Test
  public void testRecharge() {
    Date date = new Date();
    FillDataDto fillRechargeDataDto = new FillDataDto();
    fillRechargeDataDto.setBatchNo(DateUtil.format(date, DatePattern.PURE_DATETIME_MS_PATTERN))
        .setBeginTime(DateUtil.beginOfDay(date)).setEndTime(DateUtil.endOfDay(date));
    rechargeStatService.fillRechargeData(fillRechargeDataDto);

  }

  @Test
  public void testSign() {
    String headerJson = "{\"appId\":\"13133806950\",\"device\":\"0\",\"deviceInfo\":null,\"nonce\":\"9ff5151c544862fdd3a3437f1de0bc6a\",\"packageCode\":\"\",\"platform\":\"3\",\"sign\":\"165510e700bb1ce2ead5ec0f2ae689a0\",\"ticks\":1663663961948,\"version\":\"\"}";
    String reqData = "{\"createBy\":\"1\",\"createTime\":\"2022-09-07 11:23:03\",\"updateBy\":null,\"updateTime\":null,\"params\":{},\"dictId\":160,\"dictName\":\"SDK弹框类型\",\"dictType\":\"sdk_popup_type\",\"status\":\"0\",\"remark\":null}";
    String appsecret = "asfasfa1213212aasdfsdfa";
    String url = "/system/dict/type";
    genSign(JsonHelper.toObject(headerJson, RequestHeaderDto.class), reqData, appsecret, url);

  }

  private String genSign(RequestHeaderDto requestHeaderDto, String reqData, String appSecret, String url) {
    Map reqMap = JsonUtils.parseMap(reqData);
    Map<String, String> treeMap = new TreeMap<>();
    if (reqMap != null)
      treeMap.putAll(reqMap);

    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append(requestHeaderDto.getAppId());
    if (treeMap != null) {
      for (var entry : treeMap.entrySet()) {
        Object valueObject = entry.getValue();
        String value = null;

        if (valueObject instanceof LinkedHashMap) {
          // 特殊对象不参与签名
        } else if (valueObject instanceof List) {
          // 集合不参与签名
        } else {
          value = Convert.toStr(valueObject);
          // 空值不参与签名
          if (StringUtils.isNotBlank(value)) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(value);

          }
        }
      }
    }
    stringBuilder.append(requestHeaderDto.getTicks());
    stringBuilder.append(appSecret);
    stringBuilder.append(requestHeaderDto.getNonce());
    stringBuilder.append(requestHeaderDto.getPackageCode());

    stringBuilder.append(requestHeaderDto.getDevice());
    stringBuilder.append(requestHeaderDto.getPlatform());
    stringBuilder.append(requestHeaderDto.getVersion());

    String sign = SecureUtil.md5(stringBuilder.toString());
    log.info("请求地址：{}，header：{}，reqData:{}，待加密字符串：{}，加密后sign：{}", url, JsonHelper.toJson(requestHeaderDto), reqData,
        stringBuilder.toString(), sign);

    return sign;
  }

}
