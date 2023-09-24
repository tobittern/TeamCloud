package com.euler.sdk.domain.dto;

import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.sdk.domain.bo.GameUserManagementBo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameUserAddDto {
    private GameUserManagementBo gameUserManagementBo;
    private RequestHeaderDto headerDto;
    private  String ip;
}
