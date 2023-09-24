package com.euler.common.core.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class FillDataDto implements Serializable {
    private Date beginTime;
    private Date endTime;
    private String batchNo;
}
