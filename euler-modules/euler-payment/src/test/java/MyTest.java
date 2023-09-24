import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.payment.bean.AliComplaintQueryDto;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.factory.PayFactory;
import com.euler.common.payment.utils.WxPayHtmlUtils;
import com.euler.payment.EulerPaymentApplication;
import com.euler.payment.api.domain.PayOrderVo;
import com.euler.payment.domain.dto.BusinessCallbackDto;
import com.euler.payment.service.IBusinessOrderService;
import com.euler.payment.service.IComplaintService;
import com.euler.payment.service.IPayOrderService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@SpringBootTest(classes = EulerPaymentApplication.class)
@RunWith(SpringRunner.class)
public class MyTest {

  @Autowired
  private IBusinessOrderService businessOrderService;
  @Autowired
  private IPayOrderService payOrderService;
  @Autowired
  private IComplaintService complaintService;

  @Test
  public void testOrder() {
    String outTradeNo = "G202204091031153500000";

    var payOrder = payOrderService.getById(outTradeNo);
    String businessOrderId = outTradeNo.substring(1);
    var businessOrder = businessOrderService.getById(businessOrderId);
    log.info("payOrder:{},businessOrder:{}", JsonUtils.toJsonString(payOrder), JsonUtils.toJsonString(businessOrder));

  }

  @Test
  public void testSumOrder() {
    var res = businessOrderService.getMemberOrderStatInfo(15L, 0, new BigDecimal("0.51"));
    log.info("res:{}", JsonUtils.toJsonString(res));

  }

  @Test
  public void testCallback() {
    String url = "http://192.168.2.72:8080/payment/order/test";
    String secret = "";

    // 商品id，会员id，订单id
    BusinessCallbackDto callbackDto = new BusinessCallbackDto();
    callbackDto.setEventType(BusinessCallbackDto.EVENT_PAY)
        .setGoodsId("123")
        .setMemberId(102L)
        .setPayOrderId("G2021235556411025");
    String jsonRes = JsonUtils.toJsonString(callbackDto);
    try {
      RSA rsa = new RSA(null, secret);
      jsonRes = rsa.encryptBcd(jsonRes, KeyType.PublicKey);

    } catch (Exception e) {
      log.info("通知业务方加密失败,明文通知", e);
    }

    String res = HttpUtil.post(url, JsonUtils.toJsonString(R.ok(jsonRes)), 3000);
    log.info("res:{}", res);

  }

  @Test
  public void Callback() {
    String url = "https://test-logpyh10bt.5151app.com/hwx2/payment";
    String secret = "";

    // 商品id，会员id，订单id
    BusinessCallbackDto callbackDto = new BusinessCallbackDto();
    callbackDto.setEventType(BusinessCallbackDto.EVENT_PAY)
        .setNotifyId("N20220424163021125")
        .setOutTradeNo("T202456780569")
        .setGoodsId("3456")
        .setBody("测试回调订单内容").setSubject("测试回调订单标题").setGoodsImg("").setGoodsNum(1).setOrderAmount(BigDecimal.ONE)
        .setMemberId(152L)
        .setStatus("TRADE_SUCCESS").setExtData("提交时的扩展数据")
        .setPayOrderId("P20220424163021123");
    String jsonRes = JsonHelper.toJson(callbackDto);
    String enRes = "";
    try {
      RSA rsa = new RSA(null, secret);
      enRes = rsa.encryptBase64(jsonRes, KeyType.PublicKey);

    } catch (Exception e) {
      log.info("通知业务方加密失败,明文通知", e);
    }
    String ss = JsonHelper.toJson(R.ok("", enRes));
    String res = HttpUtil.post(url, ss, 3000);
    log.info("jsonRes:{}", jsonRes);
    log.info("发送内容:{}", ss);
    log.info("返回内容:{}", res);

  }

  @Test
  public void testDate() {
    String htmlBody = "";

    String result2 = WxPayHtmlUtils.getAliH5Url(htmlBody);

    log.info("res:{}", result2);

  }

  @Test
  public void testComplain() {
    // var complaintService =
    // PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
    // String url = "https://test.eulertu.cn/payment/complaint/wxNotify";
    // var flag = complaintService.createNotifyUrl(url);
    // log.info("res:{}", flag);

  }

  @Test
  public void testUpdateComplain() {
    // var complaintService =
    // PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
    // String url = "https://test.eulertu.cn/payment/complaint/wxNotify";
    // var flag = complaintService.updateNotifyUrl(url);
    // log.info("res:{}", flag);

  }

  @Test
  public void testQueryComplain() {
    // var complaintService =
    // PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
    // //String url = "http://39.97.174.165:6494/payment/complaint/wxNotify";
    // var flag = complaintService.queryNotifyUrl();
    // log.info("res:{}", flag);
    //

  }

  @Autowired
  private IComplaintService iComplaintService;

  @Test
  public void testInsertInfo() {
    // String comId = "200000020220821100019806693";
    // Boolean aBoolean = iComplaintService.insertComplaintInfo(comId);
    // var complaintService =
    // PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
    // //String url = "http://39.97.174.165:6494/payment/complaint/wxNotify";
    // var flag = complaintService.queryNotifyUrl();
    // log.info("res:{}", aBoolean);

  }

  @Test
  public void testNotifyOrder() {
    List<PayOrderVo> payOrderVos = payOrderService.queryGameNotifyFailList(100);

  }

  @Test
  public void testAliComplainList() {
    var aliComplaintService = PayFactory.getAliComplaintServiceType(PayTypeEnumd.AliComplain.getValue());

    AliComplaintQueryDto aliComplaintQueryDto = new AliComplaintQueryDto();
    aliComplaintQueryDto.setBegin_time("2022-02-14 14:23:12")
        .setEnd_time("2022-10-18 14:00:00").setPage_size(10).setPage_num(1);
    var flag = aliComplaintService.getComplainList(aliComplaintQueryDto);

    complaintService.insertAliComplaintBatch(flag);

    log.info("res:{}", flag);

  }

  @Test
  public void testAliComplainInfo() {
    var aliComplaintService = PayFactory.getAliComplaintServiceType(PayTypeEnumd.AliComplain.getValue());

    var flag = aliComplaintService.getComplainInfo("2022101000102000000007819197");

    complaintService.insertAliComplaintStr(flag);

    log.info("res:{}", flag);

  }
}
