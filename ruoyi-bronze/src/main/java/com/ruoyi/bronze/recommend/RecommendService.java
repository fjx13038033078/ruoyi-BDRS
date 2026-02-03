package com.ruoyi.bronze.recommend;

import com.ruoyi.bronze.domain.BusBronzeWare;
import com.ruoyi.bronze.domain.BusUserAction;
import com.ruoyi.bronze.domain.BusViewLog;
import com.ruoyi.bronze.mapper.BusBronzeWareMapper;
import com.ruoyi.bronze.mapper.BusUserActionMapper;
import com.ruoyi.bronze.mapper.BusViewLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 协同过滤推荐服务
 * 
 * 基于用户的协同过滤算法（User-Based Collaborative Filtering）
 * 核心思想：找到与目标用户行为相似的用户，推荐这些相似用户喜欢但目标用户未接触的物品
 * 
 * @author ruoyi
 */
@Service
public class RecommendService {

    private static final Logger log = LoggerFactory.getLogger(RecommendService.class);

    @Autowired
    private BusUserActionMapper userActionMapper;

    @Autowired
    private BusViewLogMapper viewLogMapper;

    @Autowired
    private BusBronzeWareMapper bronzeWareMapper;

    /** 行为权重配置 */
    private static final double WEIGHT_VIEW = 1.0;      // 浏览权重
    private static final double WEIGHT_FAVORITE = 3.0;  // 收藏权重
    private static final double WEIGHT_LIKE = 2.0;      // 点赞权重
    private static final double WEIGHT_RATE = 4.0;      // 评分权重（基础权重，会乘以评分值）

    /** 相似用户数量 */
    private static final int TOP_SIMILAR_USERS = 20;

    /** 默认推荐数量 */
    private static final int DEFAULT_RECOMMEND_SIZE = 8;

    /** 冷启动时的热门推荐数量 */
    private static final int COLD_START_SIZE = 12;

    /**
     * 获取用户推荐列表
     * 
     * @param userId 用户ID（可为null，表示游客）
     * @param size 推荐数量
     * @return 推荐的青铜器列表
     */
    public List<BusBronzeWare> getRecommendations(Long userId, int size) {
        if (size <= 0) {
            size = DEFAULT_RECOMMEND_SIZE;
        }

        // 如果是游客或新用户（冷启动），返回热门推荐
        if (userId == null) {
            log.info("游客访问，执行冷启动策略");
            return getColdStartRecommendations(size);
        }

        // 获取用户行为数据
        Map<Long, Double> userBehavior = getUserBehaviorVector(userId);
        
        // 冷启动检测：用户没有任何行为记录
        if (userBehavior.isEmpty()) {
            log.info("用户 {} 无行为记录，执行冷启动策略", userId);
            return getColdStartRecommendations(size);
        }

        // 执行协同过滤推荐
        List<BusBronzeWare> recommendations = collaborativeFiltering(userId, userBehavior, size);
        
        // 如果协同过滤结果不足，用热门物品补充
        if (recommendations.size() < size) {
            log.info("协同过滤结果不足，使用热门推荐补充");
            Set<Long> existingIds = recommendations.stream()
                    .map(BusBronzeWare::getId)
                    .collect(Collectors.toSet());
            
            List<BusBronzeWare> hotItems = getHotRecommendations(size - recommendations.size(), existingIds);
            recommendations.addAll(hotItems);
        }

        return recommendations;
    }

    /**
     * 基于用户的协同过滤算法核心实现
     * 
     * @param userId 目标用户ID
     * @param userBehavior 目标用户行为向量
     * @param size 推荐数量
     * @return 推荐列表
     */
    private List<BusBronzeWare> collaborativeFiltering(Long userId, Map<Long, Double> userBehavior, int size) {
        // 1. 获取所有用户的行为向量
        Map<Long, Map<Long, Double>> allUserBehaviors = getAllUserBehaviors();
        
        // 移除目标用户自己
        allUserBehaviors.remove(userId);
        
        if (allUserBehaviors.isEmpty()) {
            log.info("无其他用户行为数据，无法进行协同过滤");
            return new ArrayList<>();
        }

        // 2. 计算目标用户与其他用户的相似度
        List<UserSimilarity> similarities = new ArrayList<>();
        for (Map.Entry<Long, Map<Long, Double>> entry : allUserBehaviors.entrySet()) {
            Long otherUserId = entry.getKey();
            Map<Long, Double> otherBehavior = entry.getValue();
            
            double similarity = calculateCosineSimilarity(userBehavior, otherBehavior);
            if (similarity > 0) {
                similarities.add(new UserSimilarity(otherUserId, similarity));
            }
        }

        // 3. 按相似度排序，取前N个相似用户
        similarities.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));
        List<UserSimilarity> topSimilarUsers = similarities.stream()
                .limit(TOP_SIMILAR_USERS)
                .collect(Collectors.toList());

        if (topSimilarUsers.isEmpty()) {
            log.info("未找到相似用户");
            return new ArrayList<>();
        }

        // 4. 基于相似用户的行为，计算物品推荐分数
        Map<Long, Double> itemScores = new HashMap<>();
        Set<Long> userInteractedItems = userBehavior.keySet();

        for (UserSimilarity simUser : topSimilarUsers) {
            Map<Long, Double> simUserBehavior = allUserBehaviors.get(simUser.getUserId());
            double similarity = simUser.getSimilarity();

            for (Map.Entry<Long, Double> itemEntry : simUserBehavior.entrySet()) {
                Long itemId = itemEntry.getKey();
                Double score = itemEntry.getValue();

                // 跳过用户已经交互过的物品
                if (userInteractedItems.contains(itemId)) {
                    continue;
                }

                // 累加推荐分数 = 相似度 * 评分
                itemScores.merge(itemId, similarity * score, Double::sum);
            }
        }

        // 5. 按推荐分数排序，取前N个
        List<Long> recommendedItemIds = itemScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(size)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (recommendedItemIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 6. 查询物品详情
        return bronzeWareMapper.selectBronzeWareByIds(recommendedItemIds);
    }

    /**
     * 计算两个用户行为向量的余弦相似度
     * 
     * cos(A, B) = (A · B) / (|A| * |B|)
     * 
     * @param vectorA 用户A的行为向量
     * @param vectorB 用户B的行为向量
     * @return 余弦相似度 [0, 1]
     */
    private double calculateCosineSimilarity(Map<Long, Double> vectorA, Map<Long, Double> vectorB) {
        // 找出两个向量的交集（共同交互的物品）
        Set<Long> commonItems = new HashSet<>(vectorA.keySet());
        commonItems.retainAll(vectorB.keySet());

        if (commonItems.isEmpty()) {
            return 0.0;
        }

        // 计算点积
        double dotProduct = 0.0;
        for (Long itemId : commonItems) {
            dotProduct += vectorA.get(itemId) * vectorB.get(itemId);
        }

        // 计算向量模长
        double normA = Math.sqrt(vectorA.values().stream().mapToDouble(v -> v * v).sum());
        double normB = Math.sqrt(vectorB.values().stream().mapToDouble(v -> v * v).sum());

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (normA * normB);
    }

    /**
     * 获取用户的行为向量
     * 将用户的各种行为转换为对物品的偏好分数
     * 
     * @param userId 用户ID
     * @return 物品ID -> 偏好分数
     */
    private Map<Long, Double> getUserBehaviorVector(Long userId) {
        Map<Long, Double> behaviorVector = new HashMap<>();

        // 1. 获取浏览记录
        BusViewLog viewQuery = new BusViewLog();
        viewQuery.setUserId(userId);
        List<BusViewLog> viewLogs = viewLogMapper.selectBusViewLogList(viewQuery);
        
        for (BusViewLog log : viewLogs) {
            behaviorVector.merge(log.getBronzeId(), WEIGHT_VIEW, Double::sum);
        }

        // 2. 获取用户行为（收藏、点赞、评分）
        BusUserAction actionQuery = new BusUserAction();
        actionQuery.setUserId(userId);
        List<BusUserAction> actions = userActionMapper.selectBusUserActionList(actionQuery);

        for (BusUserAction action : actions) {
            Long bronzeId = action.getBronzeId();
            String actionType = action.getActionType();

            switch (actionType) {
                case "1": // 收藏
                    behaviorVector.merge(bronzeId, WEIGHT_FAVORITE, Double::sum);
                    break;
                case "2": // 点赞
                    behaviorVector.merge(bronzeId, WEIGHT_LIKE, Double::sum);
                    break;
                case "3": // 评分
                    double rateScore = action.getScore() != null ? 
                            action.getScore().doubleValue() * WEIGHT_RATE / 5.0 : 0;
                    behaviorVector.merge(bronzeId, rateScore, Double::sum);
                    break;
            }
        }

        return behaviorVector;
    }

    /**
     * 获取所有用户的行为向量
     * 
     * @return 用户ID -> 行为向量
     */
    private Map<Long, Map<Long, Double>> getAllUserBehaviors() {
        Map<Long, Map<Long, Double>> allBehaviors = new HashMap<>();

        // 获取所有浏览记录
        List<BusViewLog> allViewLogs = viewLogMapper.selectBusViewLogList(new BusViewLog());
        for (BusViewLog log : allViewLogs) {
            if (log.getUserId() == null) continue;
            allBehaviors
                    .computeIfAbsent(log.getUserId(), k -> new HashMap<>())
                    .merge(log.getBronzeId(), WEIGHT_VIEW, Double::sum);
        }

        // 获取所有用户行为
        List<BusUserAction> allActions = userActionMapper.selectBusUserActionList(new BusUserAction());
        for (BusUserAction action : allActions) {
            Long userId = action.getUserId();
            Long bronzeId = action.getBronzeId();
            String actionType = action.getActionType();

            Map<Long, Double> userVector = allBehaviors.computeIfAbsent(userId, k -> new HashMap<>());

            switch (actionType) {
                case "1":
                    userVector.merge(bronzeId, WEIGHT_FAVORITE, Double::sum);
                    break;
                case "2":
                    userVector.merge(bronzeId, WEIGHT_LIKE, Double::sum);
                    break;
                case "3":
                    double rateScore = action.getScore() != null ?
                            action.getScore().doubleValue() * WEIGHT_RATE / 5.0 : 0;
                    userVector.merge(bronzeId, rateScore, Double::sum);
                    break;
            }
        }

        return allBehaviors;
    }

    /**
     * 冷启动推荐策略
     * 当用户没有行为记录时，使用混合策略：
     * 1. 热门物品（高浏览量）
     * 2. 高评分物品
     * 3. 最新物品
     * 
     * @param size 推荐数量
     * @return 推荐列表
     */
    public List<BusBronzeWare> getColdStartRecommendations(int size) {
        if (size <= 0) {
            size = COLD_START_SIZE;
        }

        List<BusBronzeWare> recommendations = new ArrayList<>();
        Set<Long> addedIds = new HashSet<>();

        // 1. 获取热门物品（按浏览量排序）- 占50%
        int hotSize = size / 2;
        List<BusBronzeWare> hotItems = bronzeWareMapper.selectHotBronzeList(hotSize);
        for (BusBronzeWare item : hotItems) {
            if (addedIds.add(item.getId())) {
                recommendations.add(item);
            }
        }

        // 2. 获取高评分物品 - 占30%
        int topRatedSize = size * 3 / 10;
        List<BusBronzeWare> topRatedItems = bronzeWareMapper.selectTopRatedBronzeList(topRatedSize);
        for (BusBronzeWare item : topRatedItems) {
            if (addedIds.add(item.getId())) {
                recommendations.add(item);
            }
        }

        // 3. 获取最新物品 - 补充剩余
        int remainSize = size - recommendations.size();
        if (remainSize > 0) {
            List<BusBronzeWare> latestItems = bronzeWareMapper.selectLatestBronzeList(remainSize + 5);
            for (BusBronzeWare item : latestItems) {
                if (addedIds.add(item.getId())) {
                    recommendations.add(item);
                }
                if (recommendations.size() >= size) {
                    break;
                }
            }
        }

        return recommendations.stream().limit(size).collect(Collectors.toList());
    }

    /**
     * 获取热门推荐（排除已有物品）
     * 
     * @param size 推荐数量
     * @param excludeIds 排除的物品ID
     * @return 推荐列表
     */
    private List<BusBronzeWare> getHotRecommendations(int size, Set<Long> excludeIds) {
        List<BusBronzeWare> hotItems = bronzeWareMapper.selectHotBronzeList(size + excludeIds.size());
        return hotItems.stream()
                .filter(item -> !excludeIds.contains(item.getId()))
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * 用户相似度内部类
     */
    private static class UserSimilarity {
        private final Long userId;
        private final double similarity;

        public UserSimilarity(Long userId, double similarity) {
            this.userId = userId;
            this.similarity = similarity;
        }

        public Long getUserId() {
            return userId;
        }

        public double getSimilarity() {
            return similarity;
        }
    }
}
