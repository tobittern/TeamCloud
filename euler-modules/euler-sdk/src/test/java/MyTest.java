import cn.binarywang.wx.miniapp.api.WxMaSchemeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.scheme.WxMaGenerateSchemeRequest;
import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.EulerSdkApplication;
import com.euler.sdk.api.domain.Member;
import com.euler.sdk.domain.dto.ActivityDto;
import com.euler.sdk.domain.dto.GetWalletDto;
import com.euler.sdk.domain.vo.ActivityVo;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.service.IActivityService;
import com.euler.sdk.service.ICancellationLogService;
import com.euler.sdk.service.IMemberService;
import com.euler.sdk.service.IWalletService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest(classes = EulerSdkApplication.class)
@RunWith(SpringRunner.class)
public class MyTest {

  @Autowired
  private IWalletService walletService;
  @Autowired
  private IMemberService memberService;

  @Test
  public void testUpdateWallet() {

    walletService.modifyWallet(138L, 1, 1, 1L, RechargeTypeEnum.score, 1, "测试增加积分");
  }

  @Test
  public void testQueryWallet() {
    var res = walletService.queryByMemberId(new GetWalletDto(138L, 1, 0));
    log.info("res:{}", res);
  }

  @DubboReference
  private RemoteGameManagerService remoteGameManagerService;

  @Test
  public void testIdUtil() {
    List<Integer> objects = new ArrayList<>();
    objects.add(3);
    objects.add(4);
    List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(objects);
    log.info("xxx" + JsonUtils.toJsonString(openGameDubboVos));
  }

  @Autowired
  private IActivityService iActivityService;

  @Test
  public void testActivity() {
    ActivityDto activityDto = new ActivityDto();
    TableDataInfo<ActivityVo> activityVoTableDataInfo = iActivityService.queryPageList(activityDto);
    log.info("xxxx" + JsonUtils.toJsonString(activityVoTableDataInfo));
  }

  @Autowired
  private WxMaService wxMaService;

  @SneakyThrows
  @Test
  public void genUrlScheme() {

    String generate = wxMaService.getWxMaSchemeService().generate(WxMaGenerateSchemeRequest
        .newBuilder().expireType(1).expireInterval(30)
        .build());
    log.info("url:{}", generate);
  }

  @Test
  public void testDeIDCard() {

    String aesKey = "0Zo8BQQ8JS4JKZzBq4HAWZndban3pkSL";
    byte[] key = aesKey.getBytes(StandardCharsets.UTF_8);
    AES aes = SecureUtil.aes(key);
    // 身份证解密
    String decryptStr = aes.decryptStr("+4=");

    log.info("idCard:{}", decryptStr);
  }

  @Autowired
  private ICancellationLogService cancellationLogService;

  @Test
  public void testCancellationMember() {
    // var list = cancellationLogService.getOpList();
    // log.info("list:{}", JsonHelper.toJson(list));
    memberService.cannellation(1, 138L);
  }

  @Test
  public void testPassword() {

    // String pasw=BCrypt.hashpw("asd");
    // log.info("ss:{}",pasw);
    // 121232123123
    boolean flag = BCrypt.checkpw("asd", "121232123123");

    log.info("flag:{}", flag);

  }

}
