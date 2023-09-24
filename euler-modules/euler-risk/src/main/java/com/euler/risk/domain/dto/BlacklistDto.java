package com.euler.risk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("黑名单对象")
public class BlacklistDto extends PageQuery {

    /**
     * 黑名单类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）
     */
    @ApiModelProperty(value = "类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）")
    private String type;

    /**
     * 类型对应的目标内容
     */
    @ApiModelProperty(value = "类型对应的目标内容")
    private String target;

    /**
     * 封号类型（1:使用外挂 2:发布广告信息 3:发布淫秽色情信息 4:发布违法违规信息 5:对他人谩骂侮辱 6:发布虚假信息 7:违反九区玩家服务协议 8:其他）
     */
    @ApiModelProperty(value = "类型（1:使用外挂 2:发布广告信息 3:发布淫秽色情信息 4:发布违法违规信息 5:对他人谩骂侮辱 6:发布虚假信息 7:违反九区玩家服务协议 8:其他）")
    private String banType;

    /**
     * 封号开始时间
     */
    @ApiModelProperty(value = "封号开始时间")
    private String startTime;

    /**
     * 封号截止时间
     */
    @ApiModelProperty(value = "封号截止时间")
    private String endTime;

}
