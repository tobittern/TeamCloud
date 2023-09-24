package com.euler.risk.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 黑名单视图对象 blacklist
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@ApiModel("黑名单视图对象")
@ExcelIgnoreUnannotated
public class BlacklistVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ApiModelProperty("自增主键")
    private Integer id;

    /**
     * 类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）
     */
    @ApiModelProperty("类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）")
    private String type;

    /**
     * 类型对应的目标内容
     */
    @ApiModelProperty("类型对应的目标内容")
    private String target;

    /**
     * 封号类型（1:使用外挂 2:发布广告信息 3:发布淫秽色情信息 4:发布违法违规信息 5:对他人谩骂侮辱 6:发布虚假信息 7:违反九区玩家服务协议 8:其他）
     */
    @ApiModelProperty(value = "类型（1:使用外挂 2:发布广告信息 3:发布淫秽色情信息 4:发布违法违规信息 5:对他人谩骂侮辱 6:发布虚假信息 7:违反九区玩家服务协议 8:其他）")
    private String banType;

    /**
     * 封号开始时间
     */
    @ApiModelProperty("封号开始时间")
    private Date startTime;

    /**
     * 封号截止时间
     */
    @ApiModelProperty("封号截止时间")
    private Date endTime;

    /**
     * 封号原因
     */
    @ApiModelProperty("封号原因")
    private String reason;

    /**
     * 封停周期
     */
    @ApiModelProperty("封停周期")
    private String banTime;

}
