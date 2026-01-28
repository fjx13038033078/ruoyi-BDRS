<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="藏品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入藏品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="朝代" prop="dynasty">
        <el-select v-model="queryParams.dynasty" placeholder="请选择朝代" clearable>
          <el-option
            v-for="dict in dict.type.bus_dynasty"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="器型" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择器型" clearable>
          <el-option
            v-for="dict in dict.type.bus_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用途" prop="usageType">
        <el-select v-model="queryParams.usageType" placeholder="请选择用途" clearable>
          <el-option
            v-for="dict in dict.type.bus_usage"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="在展" value="0" />
          <el-option label="库藏" value="1" />
          <el-option label="修复中" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['bronze:ware:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['bronze:ware:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['bronze:ware:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['bronze:ware:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wareList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />
      <el-table-column label="图片" align="center" prop="images" width="100">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.images" :src="scope.row.images" :width="50" :height="50" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="藏品名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="朝代" align="center" prop="dynasty" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_dynasty" :value="scope.row.dynasty"/>
        </template>
      </el-table-column>
      <el-table-column label="器型" align="center" prop="category" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_category" :value="scope.row.category"/>
        </template>
      </el-table-column>
      <el-table-column label="用途" align="center" prop="usageType" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_usage" :value="scope.row.usageType"/>
        </template>
      </el-table-column>
      <el-table-column label="出土地" align="center" prop="excavationPlace" :show-overflow-tooltip="true" />
      <el-table-column label="馆藏编号" align="center" prop="collectionNo" width="120" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : scope.row.status === '1' ? 'info' : 'warning'">
            {{ scope.row.status === '0' ? '在展' : scope.row.status === '1' ? '库藏' : '修复中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="浏览量" align="center" prop="viewCount" width="80" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bronze:ware:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bronze:ware:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改青铜器藏品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="藏品名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入藏品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="馆藏编号" prop="collectionNo">
              <el-input v-model="form.collectionNo" placeholder="请输入馆藏编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="朝代" prop="dynasty">
              <el-select v-model="form.dynasty" placeholder="请选择朝代" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.bus_dynasty"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器型" prop="category">
              <el-select v-model="form.category" placeholder="请选择器型" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.bus_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途" prop="usageType">
              <el-select v-model="form.usageType" placeholder="请选择用途" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.bus_usage"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="纹饰类型" prop="pattern">
              <el-input v-model="form.pattern" placeholder="请输入纹饰类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出土地" prop="excavationPlace">
              <el-input v-model="form.excavationPlace" placeholder="请输入出土地" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">在展</el-radio>
                <el-radio label="1">库藏</el-radio>
                <el-radio label="2">修复中</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="藏品图片" prop="images">
              <image-upload v-model="form.images" :limit="5" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="详细介绍" prop="intro">
              <el-input v-model="form.intro" type="textarea" :rows="4" placeholder="请输入详细介绍" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWare, getWare, delWare, addWare, updateWare } from "@/api/bronze/ware";

export default {
  name: "BronzeWare",
  dicts: ['bus_dynasty', 'bus_category', 'bus_usage'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 青铜器藏品表格数据
      wareList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        dynasty: undefined,
        category: undefined,
        usageType: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "藏品名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询青铜器藏品列表 */
    getList() {
      this.loading = true;
      listWare(this.queryParams).then(response => {
        this.wareList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        dynasty: undefined,
        category: undefined,
        usageType: undefined,
        pattern: undefined,
        excavationPlace: undefined,
        collectionNo: undefined,
        intro: undefined,
        images: undefined,
        status: "0",
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加青铜器藏品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getWare(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改青铜器藏品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateWare(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWare(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除青铜器藏品编号为"' + ids + '"的数据项？').then(function() {
        return delWare(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('bronze/ware/export', {
        ...this.queryParams
      }, `ware_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
