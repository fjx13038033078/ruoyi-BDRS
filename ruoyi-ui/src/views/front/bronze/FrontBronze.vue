<template>
  <div class="front-bronze">
    <div class="page-header">
      <h1 class="page-title">青铜器库</h1>
      <p class="page-subtitle">探索中华青铜文明的璀璨瑰宝</p>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-section">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="名称">
          <el-input v-model="queryParams.name" placeholder="搜索藏品名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="朝代">
          <el-select v-model="queryParams.dynasty" placeholder="全部朝代" clearable>
            <el-option v-for="dict in dict.type.bus_dynasty" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="器型">
          <el-select v-model="queryParams.category" placeholder="全部器型" clearable>
            <el-option v-for="dict in dict.type.bus_category" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="用途">
          <el-select v-model="queryParams.usageType" placeholder="全部用途" clearable>
            <el-option v-for="dict in dict.type.bus_usage" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <i class="el-icon-search"></i> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <i class="el-icon-refresh"></i> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 藏品列表 -->
    <div class="bronze-list" v-loading="loading">
      <div class="bronze-grid">
        <div v-for="item in bronzeList" :key="item.id" class="bronze-card" @click="goDetail(item.id)">
          <div class="card-image">
            <img :src="getImageUrl(item.images)" :alt="item.name" />
            <div class="card-overlay">
              <span>查看详情</span>
            </div>
            <div class="card-badge">
              <i class="el-icon-view"></i> {{ item.viewCount || 0 }}
            </div>
          </div>
          <div class="card-info">
            <h3 class="card-title">{{ item.name }}</h3>
            <p class="card-meta">
              <span v-if="item.dynasty">{{ getDictLabel('bus_dynasty', item.dynasty) }}</span>
              <span v-if="item.category">{{ getDictLabel('bus_category', item.category) }}</span>
            </p>
            <p class="card-place" v-if="item.excavationPlace">
              <i class="el-icon-location-outline"></i> {{ item.excavationPlace }}
            </p>
          </div>
        </div>
      </div>

      <div class="empty-state" v-if="!loading && bronzeList.length === 0">
        <i class="el-icon-box"></i>
        <p>暂无相关藏品</p>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        background
        :current-page="queryParams.pageNum"
        :page-sizes="[12, 24, 36, 48]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import { listWare } from '@/api/bronze/ware'

export default {
  name: 'FrontBronze',
  dicts: ['bus_dynasty', 'bus_category', 'bus_usage'],
  data() {
    return {
      loading: false,
      bronzeList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        name: undefined,
        dynasty: undefined,
        category: undefined,
        usageType: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listWare(this.queryParams).then(response => {
        this.bronzeList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 12,
        name: undefined,
        dynasty: undefined,
        category: undefined,
        usageType: undefined
      }
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handlePageChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    getImageUrl(images) {
      if (!images) return require('@/assets/images/profile.jpg')
      const firstImg = images.split(',')[0]
      return firstImg.startsWith('http') ? firstImg : process.env.VUE_APP_BASE_API + firstImg
    },
    getDictLabel(dictType, value) {
      const dict = this.dict.type[dictType]
      if (dict) {
        const item = dict.find(d => d.value === value)
        return item ? item.label : value
      }
      return value
    },
    goDetail(id) {
      this.$router.push('/front/bronze/' + id)
    }
  }
}
</script>

<style lang="scss" scoped>
.front-bronze {
  .page-header {
    text-align: center;
    margin-bottom: 30px;

    .page-title {
      font-size: 32px;
      color: #2c1810;
      font-family: "楷体", "KaiTi", serif;
      margin-bottom: 10px;
    }

    .page-subtitle {
      color: #8b7355;
      font-size: 16px;
    }
  }

  .filter-section {
    background: #fff;
    padding: 24px;
    border-radius: 12px;
    margin-bottom: 24px;
    box-shadow: 0 2px 8px rgba(44, 24, 16, 0.08);

    .filter-form {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
  }

  .bronze-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24px;

    @media (max-width: 1200px) { grid-template-columns: repeat(3, 1fr); }
    @media (max-width: 900px) { grid-template-columns: repeat(2, 1fr); }
    @media (max-width: 600px) { grid-template-columns: 1fr; }
  }

  .bronze-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(44, 24, 16, 0.1);
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-6px);
      box-shadow: 0 12px 24px rgba(44, 24, 16, 0.15);
      .card-overlay { opacity: 1; }
      .card-image img { transform: scale(1.05); }
    }

    .card-image {
      position: relative;
      height: 200px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.3s ease;
      }

      .card-overlay {
        position: absolute;
        top: 0; left: 0; right: 0; bottom: 0;
        background: rgba(44, 24, 16, 0.6);
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s ease;

        span {
          color: #d4af37;
          font-size: 16px;
          border: 1px solid #d4af37;
          padding: 8px 20px;
          border-radius: 20px;
        }
      }

      .card-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        background: rgba(44, 24, 16, 0.8);
        color: #d4af37;
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 12px;
      }
    }

    .card-info {
      padding: 16px;

      .card-title {
        font-size: 16px;
        color: #2c1810;
        margin-bottom: 8px;
        font-weight: 500;
      }

      .card-meta {
        display: flex;
        gap: 10px;
        margin-bottom: 6px;

        span {
          font-size: 12px;
          color: #8b7355;
          background: #f5f0e6;
          padding: 2px 8px;
          border-radius: 4px;
        }
      }

      .card-place {
        font-size: 12px;
        color: #a89070;
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 60px;
    color: #a89070;

    i { font-size: 64px; margin-bottom: 16px; }
    p { font-size: 16px; }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 30px;
  }
}
</style>
