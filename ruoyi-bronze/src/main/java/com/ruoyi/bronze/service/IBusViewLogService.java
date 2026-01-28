package com.ruoyi.bronze.service;

import java.util.List;
import com.ruoyi.bronze.domain.BusViewLog;

/**
 * 用户浏览记录Service接口
 *
 * @author ruoyi
 */
public interface IBusViewLogService {
    /**
     * 查询用户浏览记录
     *
     * @param id 用户浏览记录主键
     * @return 用户浏览记录
     */
    public BusViewLog selectBusViewLogById(Long id);

    /**
     * 查询用户浏览记录列表
     *
     * @param busViewLog 用户浏览记录
     * @return 用户浏览记录集合
     */
    public List<BusViewLog> selectBusViewLogList(BusViewLog busViewLog);

    /**
     * 新增用户浏览记录
     *
     * @param busViewLog 用户浏览记录
     * @return 结果
     */
    public int insertBusViewLog(BusViewLog busViewLog);

    /**
     * 批量删除用户浏览记录
     *
     * @param ids 需要删除的用户浏览记录主键集合
     * @return 结果
     */
    public int deleteBusViewLogByIds(Long[] ids);

    /**
     * 删除用户浏览记录信息
     *
     * @param id 用户浏览记录主键
     * @return 结果
     */
    public int deleteBusViewLogById(Long id);

    /**
     * 记录浏览并增加浏览量
     *
     * @param userId 用户ID (可为空)
     * @param bronzeId 藏品ID
     * @param ipAddr IP地址
     * @return 结果
     */
    public int recordView(Long userId, Long bronzeId, String ipAddr);

    /**
     * 查询用户的浏览历史
     *
     * @param userId 用户ID
     * @return 用户浏览记录集合
     */
    public List<BusViewLog> selectUserViewHistory(Long userId);
}
