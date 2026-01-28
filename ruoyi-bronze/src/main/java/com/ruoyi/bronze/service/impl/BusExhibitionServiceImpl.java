package com.ruoyi.bronze.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bronze.mapper.BusExhibitionMapper;
import com.ruoyi.bronze.mapper.BusExhibitionBronzeMapper;
import com.ruoyi.bronze.mapper.BusBronzeWareMapper;
import com.ruoyi.bronze.domain.BusExhibition;
import com.ruoyi.bronze.domain.BusExhibitionBronze;
import com.ruoyi.bronze.service.IBusExhibitionService;

/**
 * 数字展览Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BusExhibitionServiceImpl implements IBusExhibitionService {
    @Autowired
    private BusExhibitionMapper busExhibitionMapper;

    @Autowired
    private BusExhibitionBronzeMapper busExhibitionBronzeMapper;

    @Autowired
    private BusBronzeWareMapper busBronzeWareMapper;

    /**
     * 查询数字展览
     *
     * @param id 数字展览主键
     * @return 数字展览
     */
    @Override
    public BusExhibition selectBusExhibitionById(Long id) {
        BusExhibition exhibition = busExhibitionMapper.selectBusExhibitionById(id);
        if (exhibition != null) {
            // 查询关联的藏品列表
            exhibition.setBronzeWareList(busBronzeWareMapper.selectBronzeWareByExhibitionId(id));
        }
        return exhibition;
    }

    /**
     * 查询数字展览列表
     *
     * @param busExhibition 数字展览
     * @return 数字展览
     */
    @Override
    public List<BusExhibition> selectBusExhibitionList(BusExhibition busExhibition) {
        return busExhibitionMapper.selectBusExhibitionList(busExhibition);
    }

    /**
     * 新增数字展览
     *
     * @param busExhibition 数字展览
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBusExhibition(BusExhibition busExhibition) {
        int rows = busExhibitionMapper.insertBusExhibition(busExhibition);
        // 新增展览与藏品关联
        insertExhibitionBronze(busExhibition);
        return rows;
    }

    /**
     * 修改数字展览
     *
     * @param busExhibition 数字展览
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBusExhibition(BusExhibition busExhibition) {
        // 删除原有关联
        busExhibitionBronzeMapper.deleteByExhibitionId(busExhibition.getId());
        // 新增展览与藏品关联
        insertExhibitionBronze(busExhibition);
        return busExhibitionMapper.updateBusExhibition(busExhibition);
    }

    /**
     * 批量删除数字展览
     *
     * @param ids 需要删除的数字展览主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBusExhibitionByIds(Long[] ids) {
        // 删除关联
        for (Long id : ids) {
            busExhibitionBronzeMapper.deleteByExhibitionId(id);
        }
        return busExhibitionMapper.deleteBusExhibitionByIds(ids);
    }

    /**
     * 删除数字展览信息
     *
     * @param id 数字展览主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBusExhibitionById(Long id) {
        // 删除关联
        busExhibitionBronzeMapper.deleteByExhibitionId(id);
        return busExhibitionMapper.deleteBusExhibitionById(id);
    }

    /**
     * 新增展览与藏品关联
     *
     * @param busExhibition 展览对象
     */
    private void insertExhibitionBronze(BusExhibition busExhibition) {
        Long[] bronzeIds = busExhibition.getBronzeIds();
        if (bronzeIds != null && bronzeIds.length > 0) {
            List<BusExhibitionBronze> list = new ArrayList<>();
            for (Long bronzeId : bronzeIds) {
                BusExhibitionBronze eb = new BusExhibitionBronze();
                eb.setExhibitionId(busExhibition.getId());
                eb.setBronzeId(bronzeId);
                list.add(eb);
            }
            busExhibitionBronzeMapper.batchInsertExhibitionBronze(list);
        }
    }
}
