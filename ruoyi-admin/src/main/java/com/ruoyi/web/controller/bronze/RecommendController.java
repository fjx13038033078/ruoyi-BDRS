package com.ruoyi.web.controller.bronze;

import com.ruoyi.bronze.domain.BusBronzeWare;
import com.ruoyi.bronze.recommend.RecommendService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐系统Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/bronze/recommend")
public class RecommendController extends BaseController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 获取个性化推荐列表
     * 
     * @param size 推荐数量（默认8）
     * @return 推荐的青铜器列表
     */
    @GetMapping("/list")
    public AjaxResult getRecommendations(@RequestParam(defaultValue = "8") Integer size) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            // 未登录用户，userId为null
        }
        
        List<BusBronzeWare> recommendations = recommendService.getRecommendations(userId, size);
        return AjaxResult.success(recommendations);
    }

    /**
     * 获取冷启动推荐（热门推荐）
     * 
     * @param size 推荐数量（默认12）
     * @return 热门青铜器列表
     */
    @GetMapping("/hot")
    public AjaxResult getHotRecommendations(@RequestParam(defaultValue = "12") Integer size) {
        List<BusBronzeWare> recommendations = recommendService.getColdStartRecommendations(size);
        return AjaxResult.success(recommendations);
    }
}
