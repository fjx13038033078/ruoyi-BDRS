package com.ruoyi.bronze.mapper;

import java.util.List;
import com.ruoyi.bronze.domain.BusUserAction;
import org.apache.ibatis.annotations.Param;

/**
 * 用户互动行为Mapper接口
 *
 * @author ruoyi
 */
public interface BusUserActionMapper {
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
     * 删除用户互动行为
     *
     * @param id 用户互动行为主键
     * @return 结果
     */
    public int deleteBusUserActionById(Long id);

    /**
     * 批量删除用户互动行为
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusUserActionByIds(Long[] ids);

    /**
     * 查询用户对某藏品的特定行为
     *
     * @param userId 用户ID
     * @param bronzeId 藏品ID
     * @param actionType 行为类型
     * @return 用户互动行为
     */
    public BusUserAction selectByUserAndBronzeAndType(@Param("userId") Long userId,
                                                       @Param("bronzeId") Long bronzeId,
                                                       @Param("actionType") String actionType);

    /**
     * 查询用户的收藏列表
     *
     * @param userId 用户ID
     * @return 用户互动行为集合
     */
    public List<BusUserAction> selectUserFavorites(Long userId);

    /**
     * 统计藏品的收藏数
     *
     * @param bronzeId 藏品ID
     * @return 收藏数
     */
    public int countFavoritesByBronzeId(Long bronzeId);

    /**
     * 统计藏品的点赞数
     *
     * @param bronzeId 藏品ID
     * @return 点赞数
     */
    public int countLikesByBronzeId(Long bronzeId);

    /**
     * 计算藏品的平均评分
     *
     * @param bronzeId 藏品ID
     * @return 平均评分
     */
    public Double avgScoreByBronzeId(Long bronzeId);
}
