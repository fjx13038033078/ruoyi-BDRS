package com.ruoyi.bronze.mapper;

import java.util.List;
import com.ruoyi.bronze.domain.BusExhibitionBronze;

/**
 * 展览藏品关联Mapper接口
 *
 * @author ruoyi
 */
public interface BusExhibitionBronzeMapper {
    /**
     * 批量新增展览藏品关联
     *
     * @param list 展览藏品关联列表
     * @return 结果
     */
    public int batchInsertExhibitionBronze(List<BusExhibitionBronze> list);

    /**
     * 根据展览ID删除关联
     *
     * @param exhibitionId 展览ID
     * @return 结果
     */
    public int deleteByExhibitionId(Long exhibitionId);

    /**
     * 根据藏品ID删除关联
     *
     * @param bronzeId 藏品ID
     * @return 结果
     */
    public int deleteByBronzeId(Long bronzeId);

    /**
     * 根据展览ID查询关联的藏品ID列表
     *
     * @param exhibitionId 展览ID
     * @return 藏品ID列表
     */
    public List<Long> selectBronzeIdsByExhibitionId(Long exhibitionId);
}
