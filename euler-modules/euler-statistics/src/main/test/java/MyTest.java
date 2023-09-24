import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.statistics.EulerStatisticsApplication;
import com.euler.statistics.service.IPaymentKeepStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@Slf4j
@SpringBootTest(classes = EulerStatisticsApplication.class)
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private IPaymentKeepStatisticsService paymentKeepStatService;

    @Test
    public void testPaymentKeep() {
        Date date = new Date();
        FillDataDto dto = new FillDataDto();
        dto.setBatchNo(DateUtil.format(date, DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date)).setEndTime(DateUtil.endOfDay(date));
        paymentKeepStatService.fillPaymentKeepData(dto);
    }

}
