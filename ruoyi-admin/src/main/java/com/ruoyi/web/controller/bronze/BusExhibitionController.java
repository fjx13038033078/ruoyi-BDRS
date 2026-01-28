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
import com.ruoyi.bronze.domain.BusExhibition;
import com.ruoyi.bronze.service.IBusExhibitionService;

/**
 * 数字展览Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/bronze/exhibition")
public class BusExhibitionController extends BaseController {
    @Autowired
    private IBusExhibitionService busExhibitionService;

    /**
     * 查询数字展览列表
     */
    @PreAuthorize("@ss.hasPermi('bronze:exhibition:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusExhibition busExhibition) {
        startPage();
        List<BusExhibition> list = busExhibitionService.selectBusExhibitionList(busExhibition);
        return getDataTable(list);
    }

    /**
     * 导出数字展览列表
     */
    @PreAuthorize("@ss.hasPermi('bronze:exhibition:export')")
    @Log(title = "数字展览", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusExhibition busExhibition) {
        List<BusExhibition> list = busExhibitionService.selectBusExhibitionList(busExhibition);
        ExcelUtil<BusExhibition> util = new ExcelUtil<BusExhibition>(BusExhibition.class);
        util.exportExcel(response, list, "数字展览数据");
    }

    /**
     * 获取数字展览详细信息
     */
    @PreAuthorize("@ss.hasPermi('bronze:exhibition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(busExhibitionService.selectBusExhibitionById(id));
    }

    /**
     * 新增数字展览
     */
    @PreAuthorize("@ss.hasPermi('bronze:exhibition:add')")
    @Log(title = "数字展览", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusExhibition busExhibition) {
        busExhibition.setCreateBy(getUsername());
        return toAjax(busExhibitionService.insertBusExhibition(busExhibition));
    }

    /**
     * 修改数字展览
     */
    @PreAuthorize("@ss.hasPermi('bronze:exhibition:edit')")
    @Log(title = "数字展览", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusExhibition busExhibition) {
        busExhibition.setUpdateBy(getUsername());
        return toAjax(busExhibitionService.updateBusExhibition(busExhibition));
    }

    /**
     * 删除数字展览
     */
    @PreAuthorize("@ss.hasPermi('bronze:exhibition:remove')")
    @Log(title = "数字展览", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(busExhibitionService.deleteBusExhibitionByIds(ids));
    }
}
