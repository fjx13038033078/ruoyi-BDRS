package com.ruoyi.web.controller.bronze;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.bronze.domain.BusBronzeWare;
import com.ruoyi.bronze.service.IBusBronzeWareService;

/**
 * 青铜器藏品Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/bronze/ware")
public class BusBronzeWareController extends BaseController {
    @Autowired
    private IBusBronzeWareService busBronzeWareService;

    /**
     * 查询青铜器藏品列表
     */
    @PreAuthorize("@ss.hasPermi('bronze:ware:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusBronzeWare busBronzeWare) {
        startPage();
        List<BusBronzeWare> list = busBronzeWareService.selectBusBronzeWareList(busBronzeWare);
        return getDataTable(list);
    }

    /**
     * 导出青铜器藏品列表
     */
    @PreAuthorize("@ss.hasPermi('bronze:ware:export')")
    @Log(title = "青铜器藏品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusBronzeWare busBronzeWare) {
        List<BusBronzeWare> list = busBronzeWareService.selectBusBronzeWareList(busBronzeWare);
        ExcelUtil<BusBronzeWare> util = new ExcelUtil<BusBronzeWare>(BusBronzeWare.class);
        util.exportExcel(response, list, "青铜器藏品数据");
    }

    /**
     * 获取青铜器藏品详细信息
     */
    @PreAuthorize("@ss.hasPermi('bronze:ware:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(busBronzeWareService.selectBusBronzeWareById(id));
    }

    /**
     * 新增青铜器藏品
     */
    @PreAuthorize("@ss.hasPermi('bronze:ware:add')")
    @Log(title = "青铜器藏品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusBronzeWare busBronzeWare) {
        busBronzeWare.setCreateBy(getUsername());
        return toAjax(busBronzeWareService.insertBusBronzeWare(busBronzeWare));
    }

    /**
     * 修改青铜器藏品
     */
    @PreAuthorize("@ss.hasPermi('bronze:ware:edit')")
    @Log(title = "青铜器藏品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusBronzeWare busBronzeWare) {
        busBronzeWare.setUpdateBy(getUsername());
        return toAjax(busBronzeWareService.updateBusBronzeWare(busBronzeWare));
    }

    /**
     * 删除青铜器藏品
     */
    @PreAuthorize("@ss.hasPermi('bronze:ware:remove')")
    @Log(title = "青铜器藏品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(busBronzeWareService.deleteBusBronzeWareByIds(ids));
    }

    /**
     * 根据展览ID查询藏品列表
     */
    @GetMapping("/exhibition/{exhibitionId}")
    public AjaxResult listByExhibition(@PathVariable("exhibitionId") Long exhibitionId) {
        return success(busBronzeWareService.selectBronzeWareByExhibitionId(exhibitionId));
    }
}
