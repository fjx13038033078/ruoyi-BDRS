package com.ruoyi.bronze.service;

import java.util.List;
import com.ruoyi.bronze.domain.BusBronzeWare;

/**
 * 青铜器藏品Service接口
 *
 * @author ruoyi
 */
public interface IBusBronzeWareService {
    /**
     * 查询青铜器藏品
     *
     * @param id 青铜器藏品主键
     * @return 青铜器藏品
     */
    public BusBronzeWare selectBusBronzeWareById(Long id);

    /**
     * 查询青铜器藏品列表
     *
     * @param busBronzeWare 青铜器藏品
     * @return 青铜器藏品集合
     */
    public List<BusBronzeWare> selectBusBronzeWareList(BusBronzeWare busBronzeWare);

    /**
     * 新增青铜器藏品
     *
     * @param busBronzeWare 青铜器藏品
     * @return 结果
     */
    public int insertBusBronzeWare(BusBronzeWare busBronzeWare);

    /**
     * 修改青铜器藏品
     *
     * @param busBronzeWare 青铜器藏品
     * @return 结果
     */
    public int updateBusBronzeWare(BusBronzeWare busBronzeWare);

    /**
     * 批量删除青铜器藏品
     *
     * @param ids 需要删除的青铜器藏品主键集合
     * @return 结果
     */
    public int deleteBusBronzeWareByIds(Long[] ids);

    /**
     * 删除青铜器藏品信息
     *
     * @param id 青铜器藏品主键
     * @return 结果
     */
    public int deleteBusBronzeWareById(Long id);

    /**
     * 增加浏览量
     *
     * @param id 青铜器藏品主键
     * @return 结果
     */
    public int incrementViewCount(Long id);

    /**
     * 根据展览ID查询藏品列表
     *
     * @param exhibitionId 展览ID
     * @return 藏品列表
     */
    public List<BusBronzeWare> selectBronzeWareByExhibitionId(Long exhibitionId);

    /**
     * 查询热门青铜器藏品列表 (按浏览量倒序)
     *
     * @param limit 查询数量
     * @return 热门青铜器藏品集合
     */
    public List<BusBronzeWare> selectHotBronzeList(int limit);
}
