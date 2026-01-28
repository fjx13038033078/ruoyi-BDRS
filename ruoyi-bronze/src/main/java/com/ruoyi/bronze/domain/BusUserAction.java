package com.ruoyi.bronze.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 用户互动行为对象 bus_user_action
 *
 * @author ruoyi
 */
@Data
public class BusUserAction {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 藏品ID */
    @Excel(name = "藏品ID")
    private Long bronzeId;

    /** 行为类型 (1收藏 2点赞 3评分) */
    @Excel(name = "行为类型", readConverterExp = "1=收藏,2=点赞,3=评分")
    private String actionType;

    /** 评分数值 (仅当类型为评分时有效, 1-5分) */
    @Excel(name = "评分")
    private BigDecimal score;

    /** 行为发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "行为时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 藏品名称 (非数据库字段, 用于展示) */
    private String bronzeName;

    /** 用户名称 (非数据库字段, 用于展示) */
    private String userName;
}
