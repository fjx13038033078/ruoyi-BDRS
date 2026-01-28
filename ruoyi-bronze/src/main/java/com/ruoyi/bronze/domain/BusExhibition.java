package com.ruoyi.bronze.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数字展览对象 bus_exhibition
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusExhibition extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 展览ID */
    private Long id;

    /** 展览名称 */
    @Excel(name = "展览名称")
    private String title;

    /** 展览主题 */
    @Excel(name = "展览主题")
    private String theme;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 封面图片 */
    private String coverImg;

    /** 展览简介 */
    private String description;

    /** 状态 (0进行中 1已结束 2筹备中) */
    @Excel(name = "状态", readConverterExp = "0=进行中,1=已结束,2=筹备中")
    private String status;

    /** 删除标志 */
    private String delFlag;

    /** 关联的藏品列表 (非数据库字段) */
    private List<BusBronzeWare> bronzeWareList;

    /** 关联的藏品ID列表 (非数据库字段, 用于新增/修改时传参) */
    private Long[] bronzeIds;
}
