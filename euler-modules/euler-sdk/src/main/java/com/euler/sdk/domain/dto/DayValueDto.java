package com.euler.sdk.domain.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("折线图dto")
public class DayValueDto {

    /** key **/
    private String key;

    /** value **/
    private BigDecimal value;

    /** change **/
    private String change;

    /** flag **/
    private String flag;
}
