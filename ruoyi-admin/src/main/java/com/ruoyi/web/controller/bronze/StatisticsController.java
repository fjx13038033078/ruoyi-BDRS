package com.ruoyi.web.controller.bronze;

import com.ruoyi.bronze.domain.BusBronzeWare;
import com.ruoyi.bronze.domain.BusUserAction;
import com.ruoyi.bronze.domain.BusViewLog;
import com.ruoyi.bronze.mapper.BusBronzeWareMapper;
import com.ruoyi.bronze.mapper.BusUserActionMapper;
import com.ruoyi.bronze.mapper.BusViewLogMapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据统计Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/bronze/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private BusBronzeWareMapper bronzeWareMapper;

    @Autowired
    private BusUserActionMapper userActionMapper;

    @Autowired
    private BusViewLogMapper viewLogMapper;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/dashboard")
    public AjaxResult getDashboardData() {
        Map<String, Object> result = new HashMap<>();

        // 1. 藏品朝代分布
        result.put("dynastyDistribution", getDynastyDistribution());

        // 2. 藏品器型分布
        result.put("categoryDistribution", getCategoryDistribution());

        // 3. 用户行为统计
        result.put("actionStatistics", getActionStatistics());

        // 4. 近7天浏览趋势
        result.put("viewTrend", getViewTrend());

        // 5. 汇总数据
        result.put("summary", getSummaryData());

        return AjaxResult.success(result);
    }

    /**
     * 藏品朝代分布
     */
    private List<Map<String, Object>> getDynastyDistribution() {
        List<BusBronzeWare> allBronze = bronzeWareMapper.selectBusBronzeWareList(new BusBronzeWare());
        Map<String, Long> countByDynasty = allBronze.stream()
                .filter(b -> b.getDynasty() != null && !b.getDynasty().isEmpty())
                .collect(Collectors.groupingBy(BusBronzeWare::getDynasty, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        countByDynasty.forEach((dynasty, count) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", dynasty);
            item.put("value", count);
            result.add(item);
        });
        return result;
    }

    /**
     * 藏品器型分布
     */
    private List<Map<String, Object>> getCategoryDistribution() {
        List<BusBronzeWare> allBronze = bronzeWareMapper.selectBusBronzeWareList(new BusBronzeWare());
        Map<String, Long> countByCategory = allBronze.stream()
                .filter(b -> b.getCategory() != null && !b.getCategory().isEmpty())
                .collect(Collectors.groupingBy(BusBronzeWare::getCategory, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        countByCategory.forEach((category, count) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category);
            item.put("value", count);
            result.add(item);
        });
        return result;
    }

    /**
     * 用户行为统计
     */
    private Map<String, Object> getActionStatistics() {
        List<BusUserAction> allActions = userActionMapper.selectBusUserActionList(new BusUserAction());

        long favoriteCount = allActions.stream().filter(a -> "1".equals(a.getActionType())).count();
        long likeCount = allActions.stream().filter(a -> "2".equals(a.getActionType())).count();
        long rateCount = allActions.stream().filter(a -> "3".equals(a.getActionType())).count();

        Map<String, Object> result = new HashMap<>();
        result.put("favorite", favoriteCount);
        result.put("like", likeCount);
        result.put("rate", rateCount);
        return result;
    }

    /**
     * 近7天浏览趋势
     */
    private Map<String, Object> getViewTrend() {
        List<BusViewLog> allLogs = viewLogMapper.selectBusViewLogList(new BusViewLog());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 获取近7天的日期
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(formatter);
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));

            long count = allLogs.stream()
                    .filter(log -> log.getViewTime() != null &&
                            log.getViewTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date))
                    .count();
            counts.add(count);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("counts", counts);
        return result;
    }

    /**
     * 汇总数据
     */
    private Map<String, Object> getSummaryData() {
        List<BusBronzeWare> allBronze = bronzeWareMapper.selectBusBronzeWareList(new BusBronzeWare());
        List<BusUserAction> allActions = userActionMapper.selectBusUserActionList(new BusUserAction());
        List<BusViewLog> allLogs = viewLogMapper.selectBusViewLogList(new BusViewLog());

        // 计算总浏览量
        long totalViews = allBronze.stream()
                .mapToLong(b -> b.getViewCount() != null ? b.getViewCount() : 0)
                .sum();

        Map<String, Object> result = new HashMap<>();
        result.put("bronzeCount", allBronze.size());
        result.put("totalViews", totalViews);
        result.put("actionCount", allActions.size());
        result.put("userCount", allLogs.stream().map(BusViewLog::getUserId).filter(Objects::nonNull).distinct().count());
        return result;
    }
}
