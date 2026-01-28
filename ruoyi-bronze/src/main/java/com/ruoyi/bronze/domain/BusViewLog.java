package com.ruoyi.bronze.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 用户浏览记录对象 bus_view_log
 *
 * @author ruoyi
 */
@Data
public class BusViewLog {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID (游客可为空或记录IP) */
    @Excel(name = "用户ID")
    private Long userId;

    /** 藏品ID */
    @Excel(name = "藏品ID")
    private Long bronzeId;

    /** 浏览时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "浏览时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date viewTime;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddr;

    /** 藏品名称 (非数据库字段, 用于展示) */
    private String bronzeName;

    /** 用户名称 (非数据库字段, 用于展示) */
    private String userName;
}
