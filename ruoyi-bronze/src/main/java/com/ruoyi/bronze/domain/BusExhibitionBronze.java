package com.ruoyi.bronze.domain;

import lombok.Data;

/**
 * 展览藏品关联对象 bus_exhibition_bronze
 *
 * @author ruoyi
 */
@Data
public class BusExhibitionBronze {
    /** 展览ID */
    private Long exhibitionId;

    /** 藏品ID */
    private Long bronzeId;
}
