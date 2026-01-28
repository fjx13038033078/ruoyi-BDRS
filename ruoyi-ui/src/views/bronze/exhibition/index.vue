<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="展览名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入展览名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="展览主题" prop="theme">
        <el-input
          v-model="queryParams.theme"
          placeholder="请输入展览主题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="进行中" value="0" />
          <el-option label="已结束" value="1" />
          <el-option label="筹备中" value="2" />
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
          v-hasPermi="['bronze:exhibition:add']"
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
          v-hasPermi="['bronze:exhibition:edit']"
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
          v-hasPermi="['bronze:exhibition:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['bronze:exhibition:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="exhibitionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />
      <el-table-column label="封面" align="center" prop="coverImg" width="100">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.coverImg" :src="scope.row.coverImg" :width="50" :height="50" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="展览名称" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="展览主题" align="center" prop="theme" :show-overflow-tooltip="true" />
      <el-table-column label="开始时间" align="center" prop="startDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : scope.row.status === '1' ? 'info' : 'warning'">
            {{ scope.row.status === '0' ? '进行中' : scope.row.status === '1' ? '已结束' : '筹备中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="120">
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
            v-hasPermi="['bronze:exhibition:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bronze:exhibition:remove']"
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

    <!-- 添加或修改数字展览对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="展览名称" prop="title">
              <el-input v-model="form.title" placeholder="请输入展览名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="展览主题" prop="theme">
              <el-input v-model="form.theme" placeholder="请输入展览主题" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endDate">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="封面图片" prop="coverImg">
              <image-upload v-model="form.coverImg" :limit="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">进行中</el-radio>
                <el-radio label="1">已结束</el-radio>
                <el-radio label="2">筹备中</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="关联藏品" prop="bronzeIds">
              <el-select
                v-model="form.bronzeIds"
                multiple
                filterable
                remote
                reserve-keyword
                placeholder="请选择关联藏品"
                :remote-method="searchBronze"
                :loading="bronzeLoading"
                style="width: 100%"
              >
                <el-option
                  v-for="item in bronzeOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="展览简介" prop="description">
              <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入展览简介" />
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
import { listExhibition, getExhibition, delExhibition, addExhibition, updateExhibition } from "@/api/bronze/exhibition";
import { listWare } from "@/api/bronze/ware";

export default {
  name: "Exhibition",
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
      // 数字展览表格数据
      exhibitionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 藏品选项
      bronzeOptions: [],
      // 藏品加载
      bronzeLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        theme: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "展览名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.loadBronzeOptions();
  },
  methods: {
    /** 查询数字展览列表 */
    getList() {
      this.loading = true;
      listExhibition(this.queryParams).then(response => {
        this.exhibitionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 加载藏品选项 */
    loadBronzeOptions() {
      listWare({ pageNum: 1, pageSize: 100 }).then(response => {
        this.bronzeOptions = response.rows;
      });
    },
    /** 搜索藏品 */
    searchBronze(query) {
      if (query !== '') {
        this.bronzeLoading = true;
        listWare({ name: query, pageNum: 1, pageSize: 50 }).then(response => {
          this.bronzeOptions = response.rows;
          this.bronzeLoading = false;
        });
      }
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
        title: undefined,
        theme: undefined,
        startDate: undefined,
        endDate: undefined,
        coverImg: undefined,
        description: undefined,
        status: "0",
        bronzeIds: []
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
      this.title = "添加数字展览";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getExhibition(id).then(response => {
        this.form = response.data;
        // 处理关联藏品ID
        if (this.form.bronzeWareList && this.form.bronzeWareList.length > 0) {
          this.form.bronzeIds = this.form.bronzeWareList.map(item => item.id);
          // 将已选藏品加入选项
          this.bronzeOptions = [...this.bronzeOptions, ...this.form.bronzeWareList];
          // 去重
          const map = new Map();
          this.bronzeOptions.forEach(item => map.set(item.id, item));
          this.bronzeOptions = [...map.values()];
        }
        this.open = true;
        this.title = "修改数字展览";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateExhibition(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addExhibition(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除数字展览编号为"' + ids + '"的数据项？').then(function() {
        return delExhibition(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('bronze/exhibition/export', {
        ...this.queryParams
      }, `exhibition_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
