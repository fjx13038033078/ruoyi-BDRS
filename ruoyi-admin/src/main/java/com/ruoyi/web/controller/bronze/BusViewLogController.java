package com.ruoyi.web.controller.bronze;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.bronze.domain.BusViewLog;
import com.ruoyi.bronze.service.IBusViewLogService;

/**
 * 用户浏览记录Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/bronze/viewlog")
public class BusViewLogController extends BaseController {
    @Autowired
    private IBusViewLogService busViewLogService;

    /**
     * 查询用户浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('bronze:viewlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusViewLog busViewLog) {
        startPage();
        List<BusViewLog> list = busViewLogService.selectBusViewLogList(busViewLog);
        return getDataTable(list);
    }

    /**
     * 获取用户浏览记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('bronze:viewlog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(busViewLogService.selectBusViewLogById(id));
    }

    /**
     * 删除用户浏览记录
     */
    @PreAuthorize("@ss.hasPermi('bronze:viewlog:remove')")
    @Log(title = "用户浏览记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(busViewLogService.deleteBusViewLogByIds(ids));
    }

    /**
     * 记录浏览 (用户查看藏品详情时调用)
     */
    @PostMapping("/record/{bronzeId}")
    public AjaxResult recordView(@PathVariable("bronzeId") Long bronzeId) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            // 游客访问, userId为null
        }
        String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
        busViewLogService.recordView(userId, bronzeId, ipAddr);
        return success();
    }

    /**
     * 获取我的浏览历史
     */
    @GetMapping("/history")
    public AjaxResult myHistory() {
        Long userId = SecurityUtils.getUserId();
        return success(busViewLogService.selectUserViewHistory(userId));
    }
}
