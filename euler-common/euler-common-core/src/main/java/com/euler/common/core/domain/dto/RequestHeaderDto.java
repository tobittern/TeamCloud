package com.euler.common.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeaderDto implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String appId;
    private Long ticks;
    private String sign;
    private String nonce;
    private String packageCode;
    private String device;
    private String platform;
    private String version;
    private DeviceInfoDto deviceInfo;
    private String userAgent;

}
