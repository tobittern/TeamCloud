import cn.hutool.http.HttpUtil;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.EulerCommunityApplication;
import com.euler.community.domain.dto.CodeDto;
import com.euler.community.domain.dto.GiftBagCdkDto;
import com.euler.community.domain.dto.GiftBagDto;
import com.euler.community.mapper.DynamicMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(classes = EulerCommunityApplication.class)
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private DynamicMapper dynamicMapper;
    @Test
    public void restEsAdd() throws IOException {

    }


    @Test
    public void  testCache(){
        String key= Constants.BASE_KEY+"testKey:001";
        IdNameDto idNameDto=new IdNameDto();
        RedisUtils.setCacheObject(key,null);
    }
    @Test
    public void  testGetCache(){
        String key= Constants.BASE_KEY+"testKey:001";
        IdNameDto idNameDto=        RedisUtils.getCacheObject(key);
    }

    @Test
    public  void  testFileRead(){
        List<CodeDto> codeList = new ArrayList<>();
        String filePath="https://sdk-static.eulertu.cn/2022/06/09/c25a2c743ef045b2a9ed98017ca3feee.xlsx";
            try {
                //对文件进行解析
                byte[] fileByte = HttpUtil.downloadBytes(filePath);
                InputStream input = new ByteArrayInputStream(fileByte);
                var ss=ExcelUtil.importExcel(input, CodeDto.class,true);
                codeList = ss.getList();
                if (input != null)
                    input.close();
            } catch (Exception e) {
                log.error("读取文件异常", e);
            }
      log.info("res:{}", JsonHelper.toJson(codeList));

    }
}

