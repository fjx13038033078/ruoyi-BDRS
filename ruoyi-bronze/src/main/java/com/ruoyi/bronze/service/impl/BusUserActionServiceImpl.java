package com.ruoyi.bronze.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bronze.mapper.BusUserActionMapper;
import com.ruoyi.bronze.domain.BusUserAction;
import com.ruoyi.bronze.service.IBusUserActionService;

/**
 * 用户互动行为Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BusUserActionServiceImpl implements IBusUserActionService {
    /** 收藏类型 */
    private static final String ACTION_TYPE_FAVORITE = "1";
    /** 点赞类型 */
    private static final String ACTION_TYPE_LIKE = "2";
    /** 评分类型 */
    private static final String ACTION_TYPE_RATE = "3";

    @Autowired
    private BusUserActionMapper busUserActionMapper;

    /**
     * 查询用户互动行为
     *
     * @param id 用户互动行为主键
     * @return 用户互动行为
     */
    @Override
    public BusUserAction selectBusUserActionById(Long id) {
        return busUserActionMapper.selectBusUserActionById(id);
    }

    /**
     * 查询用户互动行为列表
     *
     * @param busUserAction 用户互动行为
     * @return 用户互动行为
     */
    @Override
    public List<BusUserAction> selectBusUserActionList(BusUserAction busUserAction) {
        return busUserActionMapper.selectBusUserActionList(busUserAction);
    }

    /**
     * 新增用户互动行为
     *
     * @param busUserAction 用户互动行为
     * @return 结果
     */
    @Override
    public int insertBusUserAction(BusUserAction busUserAction) {
        return busUserActionMapper.insertBusUserAction(busUserAction);
    }

    /**
     * 修改用户互动行为
     *
     * @param busUserAction 用户互动行为
     * @return 结果
     */
    @Override
    public int updateBusUserAction(BusUserAction busUserAction) {
        return busUserActionMapper.updateBusUserAction(busUserAction);
    }

    /**
     * 批量删除用户互动行为
     *
     * @param ids 需要删除的用户互动行为主键
     * @return 结果
     */
    @Override
    public int deleteBusUserActionByIds(Long[] ids) {
        return busUserActionMapper.deleteBusUserActionByIds(ids);
    }

    /**
     * 删除用户互动行为信息
     *
     * @param id 用户互动行为主键
     * @return 结果
     */
    @Override
    public int deleteBusUserActionById(Long id) {
        return busUserActionMapper.deleteBusUserActionById(id);
    }

    /**
     * 收藏/取消收藏藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return 1=已收藏, 0=已取消收藏
     */
    @Override
    public int toggleFavorite(Long userId, Long bronzeId) {
        return toggleAction(userId, bronzeId, ACTION_TYPE_FAVORITE);
    }

    /**
     * 点赞/取消点赞藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return 1=已点赞, 0=已取消点赞
     */
    @Override
    public int toggleLike(Long userId, Long bronzeId) {
        return toggleAction(userId, bronzeId, ACTION_TYPE_LIKE);
    }

    /**
     * 切换行为状态
     */
    private int toggleAction(Long userId, Long bronzeId, String actionType) {
        BusUserAction existing = busUserActionMapper.selectByUserAndBronzeAndType(userId, bronzeId, actionType);
        if (existing != null) {
            // 已存在则删除 (取消)
            busUserActionMapper.deleteBusUserActionById(existing.getId());
            return 0;
        } else {
            // 不存在则新增
            BusUserAction action = new BusUserAction();
            action.setUserId(userId);
            action.setBronzeId(bronzeId);
            action.setActionType(actionType);
            busUserActionMapper.insertBusUserAction(action);
            return 1;
        }
    }

    /**
     * 评分藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @param score 评分
     * @return 结果
     */
    @Override
    public int rateBronze(Long userId, Long bronzeId, Double score) {
        BusUserAction existing = busUserActionMapper.selectByUserAndBronzeAndType(userId, bronzeId, ACTION_TYPE_RATE);
        if (existing != null) {
            // 已评分则更新
            existing.setScore(BigDecimal.valueOf(score));
            return busUserActionMapper.updateBusUserAction(existing);
        } else {
            // 未评分则新增
            BusUserAction action = new BusUserAction();
            action.setUserId(userId);
            action.setBronzeId(bronzeId);
            action.setActionType(ACTION_TYPE_RATE);
            action.setScore(BigDecimal.valueOf(score));
            return busUserActionMapper.insertBusUserAction(action);
        }
    }

    /**
     * 查询用户的收藏列表
     *
     * @param userId 用户ID
     * @return 用户互动行为集合
     */
    @Override
    public List<BusUserAction> selectUserFavorites(Long userId) {
        return busUserActionMapper.selectUserFavorites(userId);
    }

    /**
     * 检查用户是否收藏了某藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return true=已收藏
     */
    @Override
    public boolean checkFavorite(Long userId, Long bronzeId) {
        return busUserActionMapper.selectByUserAndBronzeAndType(userId, bronzeId, ACTION_TYPE_FAVORITE) != null;
    }

    /**
     * 检查用户是否点赞了某藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return true=已点赞
     */
    @Override
    public boolean checkLike(Long userId, Long bronzeId) {
        return busUserActionMapper.selectByUserAndBronzeAndType(userId, bronzeId, ACTION_TYPE_LIKE) != null;
    }

    /**
     * 获取用户对某藏品的评分
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return 评分, 如果未评分返回null
     */
    @Override
    public Double getUserScore(Long userId, Long bronzeId) {
        BusUserAction action = busUserActionMapper.selectByUserAndBronzeAndType(userId, bronzeId, ACTION_TYPE_RATE);
        return action != null && action.getScore() != null ? action.getScore().doubleValue() : null;
    }

    /**
     * 记录用户行为 (统一入口方法)
     * <p>
     * 行为类型说明:
     * <ul>
     *   <li>1-收藏: 已存在则删除(取消收藏), 不存在则插入</li>
     *   <li>2-点赞: 已存在则删除(取消点赞), 不存在则插入</li>
     *   <li>3-评分: 已存在则更新分数, 不存在则插入</li>
     * </ul>
     *
     * @param userId     用户ID
     * @param bronzeId   藏品ID
     * @param actionType 行为类型 (1收藏 2点赞 3评分)
     * @param score      评分数值 (仅当actionType=3时有效, 范围1-5)
     * @return 操作结果: 1=新增/更新成功, 0=取消成功(收藏/点赞), -1=参数错误
     */
    @Override
    public int recordUserAction(Long userId, Long bronzeId, String actionType, Double score) {
        // 参数校验
        if (userId == null || bronzeId == null || actionType == null) {
            return -1;
        }

        switch (actionType) {
            case ACTION_TYPE_FAVORITE:
                // 收藏: 切换状态
                return toggleAction(userId, bronzeId, ACTION_TYPE_FAVORITE);

            case ACTION_TYPE_LIKE:
                // 点赞: 切换状态
                return toggleAction(userId, bronzeId, ACTION_TYPE_LIKE);

            case ACTION_TYPE_RATE:
                // 评分: 需要score参数
                if (score == null || score < 1 || score > 5) {
                    return -1;
                }
                // 查询是否已评分
                BusUserAction existing = busUserActionMapper.selectByUserAndBronzeAndType(userId, bronzeId, ACTION_TYPE_RATE);
                if (existing != null) {
                    // 已评分则更新
                    existing.setScore(BigDecimal.valueOf(score));
                    busUserActionMapper.updateBusUserAction(existing);
                } else {
                    // 未评分则新增
                    BusUserAction action = new BusUserAction();
                    action.setUserId(userId);
                    action.setBronzeId(bronzeId);
                    action.setActionType(ACTION_TYPE_RATE);
                    action.setScore(BigDecimal.valueOf(score));
                    busUserActionMapper.insertBusUserAction(action);
                }
                return 1;

            default:
                // 未知行为类型
                return -1;
        }
    }
}
