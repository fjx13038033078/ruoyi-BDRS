<template>
  <div class="front-exhibition">
    <div class="page-header">
      <h1 class="page-title">数字展览</h1>
      <p class="page-subtitle">沉浸式体验青铜文化的魅力</p>
    </div>

    <!-- 展览列表 -->
    <div class="exhibition-list" v-loading="loading">
      <div class="exhibition-grid">
        <div v-for="item in exhibitionList" :key="item.id" class="exhibition-card" @click="goDetail(item.id)">
          <div class="card-cover">
            <img :src="getImageUrl(item.coverImg)" :alt="item.title" />
            <div class="card-status" :class="getStatusClass(item.status)">
              {{ getStatusText(item.status) }}
            </div>
          </div>
          <div class="card-content">
            <h3 class="card-title">{{ item.title }}</h3>
            <p class="card-theme" v-if="item.theme">{{ item.theme }}</p>
            <p class="card-date">
              <i class="el-icon-date"></i>
              {{ formatDate(item.startDate) }} - {{ formatDate(item.endDate) }}
            </p>
            <p class="card-desc" v-if="item.description">{{ item.description }}</p>
          </div>
        </div>
      </div>

      <div class="empty-state" v-if="!loading && exhibitionList.length === 0">
        <i class="el-icon-picture-outline"></i>
        <p>暂无展览</p>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        background
        :current-page="queryParams.pageNum"
        :page-size="queryParams.pageSize"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import { listExhibition } from '@/api/bronze/exhibition'

export default {
  name: 'FrontExhibition',
  data() {
    return {
      loading: false,
      exhibitionList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 9
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listExhibition(this.queryParams).then(response => {
        this.exhibitionList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handlePageChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    getImageUrl(img) {
      if (!img) return require('@/assets/images/profile.jpg')
      return img.startsWith('http') ? img : process.env.VUE_APP_BASE_API + img
    },
    formatDate(date) {
      return date ? date.substring(0, 10) : ''
    },
    getStatusClass(status) {
      return { '0': 'ongoing', '1': 'ended', '2': 'upcoming' }[status] || ''
    },
    getStatusText(status) {
      return { '0': '进行中', '1': '已结束', '2': '即将开展' }[status] || ''
    },
    goDetail(id) {
      this.$router.push('/front/exhibition/' + id)
    }
  }
}
</script>

<style lang="scss" scoped>
.front-exhibition {
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

  .exhibition-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 24px;

    @media (max-width: 1000px) { grid-template-columns: repeat(2, 1fr); }
    @media (max-width: 600px) { grid-template-columns: 1fr; }
  }

  .exhibition-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(44, 24, 16, 0.1);
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-6px);
      box-shadow: 0 12px 24px rgba(44, 24, 16, 0.15);
    }

    .card-cover {
      position: relative;
      height: 200px;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .card-status {
        position: absolute;
        top: 12px;
        left: 12px;
        padding: 4px 12px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: 500;

        &.ongoing { background: #67c23a; color: #fff; }
        &.ended { background: #909399; color: #fff; }
        &.upcoming { background: #e6a23c; color: #fff; }
      }
    }

    .card-content {
      padding: 20px;

      .card-title {
        font-size: 18px;
        color: #2c1810;
        margin-bottom: 10px;
        font-weight: 500;
      }

      .card-theme {
        font-size: 14px;
        color: #8b7355;
        margin-bottom: 10px;
      }

      .card-date {
        font-size: 13px;
        color: #a89070;
        margin-bottom: 10px;
      }

      .card-desc {
        font-size: 14px;
        color: #666;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 60px;
    color: #a89070;

    i { font-size: 64px; margin-bottom: 16px; }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 30px;
  }
}
</style>
