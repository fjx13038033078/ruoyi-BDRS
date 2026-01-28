package com.ruoyi.bronze.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 青铜器藏品对象 bus_bronze_ware
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusBronzeWare extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 藏品名称 */
    @Excel(name = "藏品名称")
    private String name;

    /** 朝代 (字典值: bus_dynasty) */
    @Excel(name = "朝代")
    private String dynasty;

    /** 器型 (字典值: bus_category, 如鼎、尊) */
    @Excel(name = "器型")
    private String category;

    /** 用途 (字典值: bus_usage, 如礼器、兵器) */
    @Excel(name = "用途")
    private String usageType;

    /** 纹饰类型 */
    @Excel(name = "纹饰类型")
    private String pattern;

    /** 出土地 */
    @Excel(name = "出土地")
    private String excavationPlace;

    /** 馆藏编号 */
    @Excel(name = "馆藏编号")
    private String collectionNo;

    /** 详细介绍 */
    private String intro;

    /** 图片地址 (逗号分隔或JSON) */
    private String images;

    /** 状态 (0在展 1库藏 2修复中) */
    @Excel(name = "状态", readConverterExp = "0=在展,1=库藏,2=修复中")
    private String status;

    /** 浏览量 (热度统计) */
    @Excel(name = "浏览量")
    private Long viewCount;

    /** 删除标志 (0代表存在 2代表删除) */
    private String delFlag;
}
