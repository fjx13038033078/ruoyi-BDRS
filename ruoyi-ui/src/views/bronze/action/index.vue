<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="藏品ID" prop="bronzeId">
        <el-input
          v-model="queryParams.bronzeId"
          placeholder="请输入藏品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="行为类型" prop="actionType">
        <el-select v-model="queryParams.actionType" placeholder="请选择行为类型" clearable>
          <el-option label="收藏" value="1" />
          <el-option label="点赞" value="2" />
          <el-option label="评分" value="3" />
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['bronze:action:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="actionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="用户名" align="center" prop="userName" width="120" />
      <el-table-column label="藏品ID" align="center" prop="bronzeId" width="80" />
      <el-table-column label="藏品名称" align="center" prop="bronzeName" :show-overflow-tooltip="true" />
      <el-table-column label="行为类型" align="center" prop="actionType" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.actionType === '1' ? 'warning' : scope.row.actionType === '2' ? 'danger' : 'success'">
            {{ scope.row.actionType === '1' ? '收藏' : scope.row.actionType === '2' ? '点赞' : '评分' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="评分" align="center" prop="score" width="100">
        <template slot-scope="scope">
          <el-rate
            v-if="scope.row.actionType === '3' && scope.row.score"
            v-model="scope.row.score"
            disabled
            :max="5"
            :allow-half="true"
            style="display: inline-block"
          />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bronze:action:remove']"
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
  </div>
</template>

<script>
import { listAction, delAction } from "@/api/bronze/action";

export default {
  name: "UserAction",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户行为表格数据
      actionList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        bronzeId: undefined,
        actionType: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户行为列表 */
    getList() {
      this.loading = true;
      listAction(this.queryParams).then(response => {
        this.actionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
      this.multiple = !selection.length;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除用户行为编号为"' + ids + '"的数据项？').then(function() {
        return delAction(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
