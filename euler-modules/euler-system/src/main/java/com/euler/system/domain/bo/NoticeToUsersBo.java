package com.euler.system.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

/**
 * 公告接收业务对象 notice_to_users
 *
 * @author euler
 * @date 2022-04-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("公告接收业务对象")
public class NoticeToUsersBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 公告id
     */
    @ApiModelProperty(value = "公告id", required = true)
    @NotNull(message = "公告id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer noticeId;

    /**
     * 接收人
     */
    @ApiModelProperty(value = "接收人", required = true)
    @NotNull(message = "接收人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long toUserId;

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ApiModelProperty(value = "阅读状态 0:未读 1:已读", required = true)
    @NotBlank(message = "阅读状态 0:未读 1:已读不能为空", groups = { AddGroup.class, EditGroup.class })
    private String readStatus;

}
