package com.euler.risk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 封号列业务对象 banlist
 *
 * @author euler
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("封号列业务对象")
public class BanlistBo extends BaseEntity {

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键", required = true)
    @NotNull(message = "自增主键不能为空", groups = {EditGroup.class})
    private Integer id;

    /**
     * 平台 1安卓 2IOS 3H5
     */
    @ApiModelProperty(value = "平台 1安卓 2IOS 3H5", required = true)
    private String platform;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long memberId;

    /**
     * 用户IDS
     */
    @ApiModelProperty(value = "用户IDS", required = true)
    private String memberIds;

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer gameId;

    /**
     * 封号开始时间
     */
    @ApiModelProperty(value = "封号开始时间", required = true)
    @NotNull(message = "封号开始时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private String startTime;

    /**
     * 封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁
     */
    @ApiModelProperty(value = "封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁", required = true)
    @NotNull(message = "封号截止时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private String endTime;

    /**
     * 封号类型
     */
    @ApiModelProperty(value = "封号类型", required = true)
    @NotBlank(message = "封号类型不能为空", groups = {AddGroup.class})
    private String banType;

    /**
     * 封号原因
     */
    @ApiModelProperty(value = "封号原因", required = true)
    @NotBlank(message = "封号原因不能为空", groups = {AddGroup.class})
    private String reason;

    /**
     * 封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备
     */
    @ApiModelProperty(value = "封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备 6统一设备编码", required = true)
    @NotNull(message = "封号查询类型不能为空", groups = {AddGroup.class})
    private Integer searchType;

    /**
     * 搜索的key
     */
    @ApiModelProperty(value = "搜索的key", required = true)
    private String searchKey;

    /**
     * 设备编号 1 mac  2oaid 3 imei  4 android 5 uuid 6 idfa 7 pushId
     */
    @ApiModelProperty(value = "设备编号", required = true)
    private Integer equipmentNum;

    /**
     * 是否加入黑名单
     */
    @ApiModelProperty(value = "是否加入黑名单", required = true)
    private Integer isJoin = 0;


}
