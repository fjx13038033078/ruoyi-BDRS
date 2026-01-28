package com.ruoyi.bronze.service;

import java.util.List;
import com.ruoyi.bronze.domain.BusExhibition;

/**
 * 数字展览Service接口
 *
 * @author ruoyi
 */
public interface IBusExhibitionService {
    /**
     * 查询数字展览
     *
     * @param id 数字展览主键
     * @return 数字展览
     */
    public BusExhibition selectBusExhibitionById(Long id);

    /**
     * 查询数字展览列表
     *
     * @param busExhibition 数字展览
     * @return 数字展览集合
     */
    public List<BusExhibition> selectBusExhibitionList(BusExhibition busExhibition);

    /**
     * 新增数字展览
     *
     * @param busExhibition 数字展览
     * @return 结果
     */
    public int insertBusExhibition(BusExhibition busExhibition);

    /**
     * 修改数字展览
     *
     * @param busExhibition 数字展览
     * @return 结果
     */
    public int updateBusExhibition(BusExhibition busExhibition);

    /**
     * 批量删除数字展览
     *
     * @param ids 需要删除的数字展览主键集合
     * @return 结果
     */
    public int deleteBusExhibitionByIds(Long[] ids);

    /**
     * 删除数字展览信息
     *
     * @param id 数字展览主键
     * @return 结果
     */
    public int deleteBusExhibitionById(Long id);
}
