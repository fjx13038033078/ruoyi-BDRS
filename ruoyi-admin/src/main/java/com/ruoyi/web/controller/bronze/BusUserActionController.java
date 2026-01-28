package com.ruoyi.web.controller.bronze;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.bronze.domain.BusUserAction;
import com.ruoyi.bronze.service.IBusUserActionService;

/**
 * 用户互动行为Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/bronze/action")
public class BusUserActionController extends BaseController {
    @Autowired
    private IBusUserActionService busUserActionService;

    /**
     * 查询用户互动行为列表
     */
    @PreAuthorize("@ss.hasPermi('bronze:action:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusUserAction busUserAction) {
        startPage();
        List<BusUserAction> list = busUserActionService.selectBusUserActionList(busUserAction);
        return getDataTable(list);
    }

    /**
     * 获取用户互动行为详细信息
     */
    @PreAuthorize("@ss.hasPermi('bronze:action:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(busUserActionService.selectBusUserActionById(id));
    }

    /**
     * 删除用户互动行为
     */
    @PreAuthorize("@ss.hasPermi('bronze:action:remove')")
    @Log(title = "用户互动行为", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(busUserActionService.deleteBusUserActionByIds(ids));
    }

    /**
     * 收藏/取消收藏藏品
     */
    @Log(title = "收藏藏品", businessType = BusinessType.INSERT)
    @PostMapping("/favorite/{bronzeId}")
    public AjaxResult toggleFavorite(@PathVariable("bronzeId") Long bronzeId) {
        Long userId = SecurityUtils.getUserId();
        int result = busUserActionService.toggleFavorite(userId, bronzeId);
        return success(result == 1 ? "收藏成功" : "取消收藏成功");
    }

    /**
     * 点赞/取消点赞藏品
     */
    @Log(title = "点赞藏品", businessType = BusinessType.INSERT)
    @PostMapping("/like/{bronzeId}")
    public AjaxResult toggleLike(@PathVariable("bronzeId") Long bronzeId) {
        Long userId = SecurityUtils.getUserId();
        int result = busUserActionService.toggleLike(userId, bronzeId);
        return success(result == 1 ? "点赞成功" : "取消点赞成功");
    }

    /**
     * 评分藏品
     */
    @Log(title = "评分藏品", businessType = BusinessType.INSERT)
    @PostMapping("/rate/{bronzeId}")
    public AjaxResult rateBronze(@PathVariable("bronzeId") Long bronzeId, @RequestParam("score") Double score) {
        if (score < 1 || score > 5) {
            return error("评分必须在1-5之间");
        }
        Long userId = SecurityUtils.getUserId();
        busUserActionService.rateBronze(userId, bronzeId, score);
        return success("评分成功");
    }

    /**
     * 获取我的收藏列表
     */
    @GetMapping("/favorites")
    public AjaxResult myFavorites() {
        Long userId = SecurityUtils.getUserId();
        return success(busUserActionService.selectUserFavorites(userId));
    }

    /**
     * 检查是否收藏
     */
    @GetMapping("/check/favorite/{bronzeId}")
    public AjaxResult checkFavorite(@PathVariable("bronzeId") Long bronzeId) {
        Long userId = SecurityUtils.getUserId();
        return success(busUserActionService.checkFavorite(userId, bronzeId));
    }

    /**
     * 检查是否点赞
     */
    @GetMapping("/check/like/{bronzeId}")
    public AjaxResult checkLike(@PathVariable("bronzeId") Long bronzeId) {
        Long userId = SecurityUtils.getUserId();
        return success(busUserActionService.checkLike(userId, bronzeId));
    }

    /**
     * 获取我对某藏品的评分
     */
    @GetMapping("/score/{bronzeId}")
    public AjaxResult getMyScore(@PathVariable("bronzeId") Long bronzeId) {
        Long userId = SecurityUtils.getUserId();
        return success(busUserActionService.getUserScore(userId, bronzeId));
    }
}
