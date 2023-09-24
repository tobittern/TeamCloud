package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * banner组对象 banner_group
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("banner_group")
public class BannerGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 应用场景，0发现菜单，1个人中心
     */
    private String applicationType;
    /**
     * banner组名
     */
    private String groupName;
    /**
     * 显示开始时间
     */
    private Date startTime;
    /**
     * 显示结束时间
     */
    private Date endTime;
    /**
     * banner内容，格式为json格式
     */
    private String bannerContent;
    /**
     * banner组中第一个banner的icon
     */
    private String bannerIcon;
    /**
     * banner数量
     */
    private Integer bannerNumber;
    /**
     * 状态，0待启用，1已启用，2已停用
     */
    private String status;
    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}

