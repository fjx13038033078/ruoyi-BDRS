package com.ruoyi.bronze.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bronze.mapper.BusBronzeWareMapper;
import com.ruoyi.bronze.domain.BusBronzeWare;
import com.ruoyi.bronze.service.IBusBronzeWareService;

/**
 * 青铜器藏品Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BusBronzeWareServiceImpl implements IBusBronzeWareService {
    @Autowired
    private BusBronzeWareMapper busBronzeWareMapper;

    /**
     * 查询青铜器藏品
     *
     * @param id 青铜器藏品主键
     * @return 青铜器藏品
     */
    @Override
    public BusBronzeWare selectBusBronzeWareById(Long id) {
        return busBronzeWareMapper.selectBusBronzeWareById(id);
    }

    /**
     * 查询青铜器藏品列表
     *
     * @param busBronzeWare 青铜器藏品
     * @return 青铜器藏品
     */
    @Override
    public List<BusBronzeWare> selectBusBronzeWareList(BusBronzeWare busBronzeWare) {
        return busBronzeWareMapper.selectBusBronzeWareList(busBronzeWare);
    }

    /**
     * 新增青铜器藏品
     *
     * @param busBronzeWare 青铜器藏品
     * @return 结果
     */
    @Override
    public int insertBusBronzeWare(BusBronzeWare busBronzeWare) {
        return busBronzeWareMapper.insertBusBronzeWare(busBronzeWare);
    }

    /**
     * 修改青铜器藏品
     *
     * @param busBronzeWare 青铜器藏品
     * @return 结果
     */
    @Override
    public int updateBusBronzeWare(BusBronzeWare busBronzeWare) {
        return busBronzeWareMapper.updateBusBronzeWare(busBronzeWare);
    }

    /**
     * 批量删除青铜器藏品
     *
     * @param ids 需要删除的青铜器藏品主键
     * @return 结果
     */
    @Override
    public int deleteBusBronzeWareByIds(Long[] ids) {
        return busBronzeWareMapper.deleteBusBronzeWareByIds(ids);
    }

    /**
     * 删除青铜器藏品信息
     *
     * @param id 青铜器藏品主键
     * @return 结果
     */
    @Override
    public int deleteBusBronzeWareById(Long id) {
        return busBronzeWareMapper.deleteBusBronzeWareById(id);
    }

    /**
     * 增加浏览量
     *
     * @param id 青铜器藏品主键
     * @return 结果
     */
    @Override
    public int incrementViewCount(Long id) {
        return busBronzeWareMapper.incrementViewCount(id);
    }

    /**
     * 根据展览ID查询藏品列表
     *
     * @param exhibitionId 展览ID
     * @return 藏品列表
     */
    @Override
    public List<BusBronzeWare> selectBronzeWareByExhibitionId(Long exhibitionId) {
        return busBronzeWareMapper.selectBronzeWareByExhibitionId(exhibitionId);
    }

    /**
     * 查询热门青铜器藏品列表 (按浏览量倒序)
     *
     * @param limit 查询数量
     * @return 热门青铜器藏品集合
     */
    @Override
    public List<BusBronzeWare> selectHotBronzeList(int limit) {
        return busBronzeWareMapper.selectHotBronzeList(limit);
    }
}
