package com.ruoyi.bronze.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bronze.mapper.BusViewLogMapper;
import com.ruoyi.bronze.mapper.BusBronzeWareMapper;
import com.ruoyi.bronze.domain.BusViewLog;
import com.ruoyi.bronze.service.IBusViewLogService;

/**
 * 用户浏览记录Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BusViewLogServiceImpl implements IBusViewLogService {
    @Autowired
    private BusViewLogMapper busViewLogMapper;

    @Autowired
    private BusBronzeWareMapper busBronzeWareMapper;

    /**
     * 查询用户浏览记录
     *
     * @param id 用户浏览记录主键
     * @return 用户浏览记录
     */
    @Override
    public BusViewLog selectBusViewLogById(Long id) {
        return busViewLogMapper.selectBusViewLogById(id);
    }

    /**
     * 查询用户浏览记录列表
     *
     * @param busViewLog 用户浏览记录
     * @return 用户浏览记录
     */
    @Override
    public List<BusViewLog> selectBusViewLogList(BusViewLog busViewLog) {
        return busViewLogMapper.selectBusViewLogList(busViewLog);
    }

    /**
     * 新增用户浏览记录
     *
     * @param busViewLog 用户浏览记录
     * @return 结果
     */
    @Override
    public int insertBusViewLog(BusViewLog busViewLog) {
        return busViewLogMapper.insertBusViewLog(busViewLog);
    }

    /**
     * 批量删除用户浏览记录
     *
     * @param ids 需要删除的用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteBusViewLogByIds(Long[] ids) {
        return busViewLogMapper.deleteBusViewLogByIds(ids);
    }

    /**
     * 删除用户浏览记录信息
     *
     * @param id 用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteBusViewLogById(Long id) {
        return busViewLogMapper.deleteBusViewLogById(id);
    }

    /**
     * 记录浏览并增加浏览量
     *
     * @param userId 用户ID (可为空)
     * @param bronzeId 藏品ID
     * @param ipAddr IP地址
     * @return 结果
     */
    @Override
    public int recordView(Long userId, Long bronzeId, String ipAddr) {
        // 记录浏览日志
        BusViewLog viewLog = new BusViewLog();
        viewLog.setUserId(userId);
        viewLog.setBronzeId(bronzeId);
        viewLog.setIpAddr(ipAddr);
        busViewLogMapper.insertBusViewLog(viewLog);

        // 增加藏品浏览量
        busBronzeWareMapper.incrementViewCount(bronzeId);

        return 1;
    }

    /**
     * 查询用户的浏览历史
     *
     * @param userId 用户ID
     * @return 用户浏览记录集合
     */
    @Override
    public List<BusViewLog> selectUserViewHistory(Long userId) {
        return busViewLogMapper.selectUserViewHistory(userId);
    }
}
