package com.euler.sdk.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 获取钱包dto
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetWalletDto {
    private  Long id;
    private  Integer type;
    private  Integer gameId;
}
