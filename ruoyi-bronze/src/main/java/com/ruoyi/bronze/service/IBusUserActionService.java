package com.ruoyi.bronze.service;

import java.util.List;
import com.ruoyi.bronze.domain.BusUserAction;

/**
 * 用户互动行为Service接口
 *
 * @author ruoyi
 */
public interface IBusUserActionService {
    /**
     * 查询用户互动行为
     *
     * @param id 用户互动行为主键
     * @return 用户互动行为
     */
    public BusUserAction selectBusUserActionById(Long id);

    /**
     * 查询用户互动行为列表
     *
     * @param busUserAction 用户互动行为
     * @return 用户互动行为集合
     */
    public List<BusUserAction> selectBusUserActionList(BusUserAction busUserAction);

    /**
     * 新增用户互动行为
     *
     * @param busUserAction 用户互动行为
     * @return 结果
     */
    public int insertBusUserAction(BusUserAction busUserAction);

    /**
     * 修改用户互动行为
     *
     * @param busUserAction 用户互动行为
     * @return 结果
     */
    public int updateBusUserAction(BusUserAction busUserAction);

    /**
     * 批量删除用户互动行为
     *
     * @param ids 需要删除的用户互动行为主键集合
     * @return 结果
     */
    public int deleteBusUserActionByIds(Long[] ids);

    /**
     * 删除用户互动行为信息
     *
     * @param id 用户互动行为主键
     * @return 结果
     */
    public int deleteBusUserActionById(Long id);

    /**
     * 收藏/取消收藏藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return 1=已收藏, 0=已取消收藏
     */
    public int toggleFavorite(Long userId, Long bronzeId);

    /**
     * 点赞/取消点赞藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return 1=已点赞, 0=已取消点赞
     */
    public int toggleLike(Long userId, Long bronzeId);

    /**
     * 评分藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @param score 评分
     * @return 结果
     */
    public int rateBronze(Long userId, Long bronzeId, Double score);

    /**
     * 查询用户的收藏列表
     *
     * @param userId 用户ID
     * @return 用户互动行为集合
     */
    public List<BusUserAction> selectUserFavorites(Long userId);

    /**
     * 检查用户是否收藏了某藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return true=已收藏
     */
    public boolean checkFavorite(Long userId, Long bronzeId);

    /**
     * 检查用户是否点赞了某藏品
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return true=已点赞
     */
    public boolean checkLike(Long userId, Long bronzeId);

    /**
     * 获取用户对某藏品的评分
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @return 评分, 如果未评分返回null
     */
    public Double getUserScore(Long userId, Long bronzeId);

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
    public int recordUserAction(Long userId, Long bronzeId, String actionType, Double score);
}
