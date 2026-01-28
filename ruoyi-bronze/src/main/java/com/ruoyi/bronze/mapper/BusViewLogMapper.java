package com.ruoyi.bronze.mapper;

import java.util.List;
import com.ruoyi.bronze.domain.BusViewLog;

/**
 * 用户浏览记录Mapper接口
 *
 * @author ruoyi
 */
public interface BusViewLogMapper {
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
     * 删除用户浏览记录
     *
     * @param id 用户浏览记录主键
     * @return 结果
     */
    public int deleteBusViewLogById(Long id);

    /**
     * 批量删除用户浏览记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusViewLogByIds(Long[] ids);

    /**
     * 查询用户的浏览历史
     *
     * @param userId 用户ID
     * @return 用户浏览记录集合
     */
    public List<BusViewLog> selectUserViewHistory(Long userId);

    /**
     * 统计藏品的浏览人数
     *
     * @param bronzeId 藏品ID
     * @return 浏览人数
     */
    public int countViewersByBronzeId(Long bronzeId);
}
