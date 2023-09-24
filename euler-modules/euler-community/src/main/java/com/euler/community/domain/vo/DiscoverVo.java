package com.euler.community.domain.vo;

import cn.hutool.core.lang.Dict;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.domain.dto.IdNameDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 发现配置视图对象 discover
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("发现配置视图对象")
@ExcelIgnoreUnannotated
public class DiscoverVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 模块标题
     */
    @ExcelProperty(value = "模块标题")
    @ApiModelProperty("模块标题")
    private String title;

    /**
     * 模块介绍
     */
    @ExcelProperty(value = "模块介绍")
    @ApiModelProperty("模块介绍")
    private String introduce;

    /**
     * 模块类型, 1纵向编组，2横向编组，3bannner
     */
    @ExcelProperty(value = "模块类型, 1纵向编组，2横向编组，3bannner")
    @ApiModelProperty("模块类型, 1纵向编组，2横向编组，3bannner")
    private String moduleType;

    /**
     * 当前状态，0待启用，1已启用，2已停用
     */
    @ExcelProperty(value = "当前状态，0待启用，1已启用，2已停用")
    @ApiModelProperty("当前状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * 应用系统，1android，2ios,3h5
     */
    @ExcelProperty(value = "应用系统，1android，2ios,3h5")
    @ApiModelProperty("应用系统，1android，2ios,3h5")
    private String applicationSystem;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ExcelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高")
    @ApiModelProperty("显示优先级，默认值0，数字越小，显示级别越高")
    private Integer level;

    /**
     * 具体内容，格式为json格式
     * 类型是1和2的时候，[{"gameId":"101","tag":"热门游戏1"},{"gameId":"102","tag":"热门游戏2"}]
     * 类型是3的时候，[{"bannerGroupId":"201"}]
     */
    @ExcelProperty(value = "具体内容，格式为json格式")
    @ApiModelProperty("具体内容，格式为json格式")
    private String content;

    @ExcelProperty(value = "后端展示的游戏具体内容")
    @ApiModelProperty("后端展示的游戏具体内容")
    private List<Dict> showContent;

    @ExcelProperty(value = "后端展示的banner具体内容")
    @ApiModelProperty("后端展示的banner具体内容")
    private IdNameDto idNameDto;

    @ExcelProperty(value = "前端展示的游戏具体内容")
    @ApiModelProperty("前端展示的游戏具体内容")
    private List<DiscoverShowGameVo> frontShowGame;

    @ExcelProperty(value = "前端展示的具体内容")
    @ApiModelProperty("前端展示的具体内容")
    private List<BannerVo> frontShowBanner;

}
