package com.euler.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NewIncrOrderUserVo implements Serializable {
    /**
     * string 是一个 dateLabel+packcode+game_id组成的字符串
     */
    private String storageKey;
    private List<Long> storageMemberIds;
    private List<String> storageRoleIds;
    private List<String> storageServerIds;
}
