package com.euler.sdk.api.domain.dto;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 封号查询对象
 *
 * @author euler
 * @date 2022-08-23
 */

@Data
@ApiModel("封号查询对象")
public class SearchBanDto extends PageQuery {

    /**
     * 封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备  6按照一批用户ID
     */
    @ApiModelProperty(value = "封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备 6按照一批用户ID 7同一设备编码", required = true)
    @NotNull(message = "封号查询类型不能为空", groups = {AddGroup.class})
    private Integer searchType;

    /**
     * 搜索的key
     */
    @ApiModelProperty(value = "搜索的key", required = true)
    @NotBlank(message = "搜索的key不能为空", groups = {AddGroup.class})
    private String searchKey;

    /**
     * 设备编号 1 mac  2oaid 3 imei  4 android 5 uuid 6 idfa 7 pushId
     */
    @ApiModelProperty(value = "设备编号", required = true)
    private Integer equipmentNum;



}
