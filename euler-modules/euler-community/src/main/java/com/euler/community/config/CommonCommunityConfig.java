package com.euler.community.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig.community")
public class CommonCommunityConfig {
    // 评论字段的最大长度
    private int maxCommentsLength;
    // 动态title字段的最大长度
    private int maxDynamicTitleLength;
    // 默认的动态图片标题文案
    private String defaultDynamicPictureTitle;
    // 默认的动态视频标题文案
    private String defaultDynamicVideoTitle;
    //有拍云前缀
    private  String yunDomain;
    // 是否自动审核 1 自动审核  0 不自动审核
    private int isOpenAudit;

}
