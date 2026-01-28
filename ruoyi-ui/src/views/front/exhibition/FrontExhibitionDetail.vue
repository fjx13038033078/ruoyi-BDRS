<template>
  <div class="exhibition-detail" v-loading="loading">
    <div class="detail-container" v-if="exhibition">
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/front' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/front/exhibition' }">数字展览</el-breadcrumb-item>
        <el-breadcrumb-item>{{ exhibition.title }}</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 展览头部 -->
      <div class="exhibition-header">
        <div class="header-cover">
          <img :src="getImageUrl(exhibition.coverImg)" :alt="exhibition.title" />
          <div class="header-status" :class="getStatusClass(exhibition.status)">
            {{ getStatusText(exhibition.status) }}
          </div>
        </div>
        <div class="header-info">
          <h1 class="info-title">{{ exhibition.title }}</h1>
          <p class="info-theme" v-if="exhibition.theme">{{ exhibition.theme }}</p>
          <p class="info-date">
            <i class="el-icon-date"></i>
            {{ formatDate(exhibition.startDate) }} - {{ formatDate(exhibition.endDate) }}
          </p>
          <p class="info-desc" v-if="exhibition.description">{{ exhibition.description }}</p>
        </div>
      </div>

      <!-- 展览藏品 -->
      <div class="exhibition-items" v-if="exhibition.bronzeWareList && exhibition.bronzeWareList.length > 0">
        <h2 class="section-title">
          <i class="el-icon-goods"></i> 展览藏品
          <span class="item-count">共 {{ exhibition.bronzeWareList.length }} 件</span>
        </h2>
        <div class="bronze-grid">
          <div
            v-for="item in exhibition.bronzeWareList"
            :key="item.id"
            class="bronze-card"
            @click="goDetail(item.id)"
          >
            <div class="card-image">
              <img :src="getImageUrl(item.images)" :alt="item.name" />
            </div>
            <div class="card-info">
              <h3 class="card-title">{{ item.name }}</h3>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getExhibition } from '@/api/bronze/exhibition'

export default {
  name: 'FrontExhibitionDetail',
  data() {
    return {
      loading: false,
      exhibition: null
    }
  },
  created() {
    this.loadDetail()
  },
  methods: {
    loadDetail() {
      const id = this.$route.params.id
      this.loading = true
      getExhibition(id).then(response => {
        this.exhibition = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getImageUrl(img) {
      if (!img) return require('@/assets/images/profile.jpg')
      const firstImg = img.split(',')[0]
      return firstImg.startsWith('http') ? firstImg : process.env.VUE_APP_BASE_API + firstImg
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
      this.$router.push('/front/bronze/' + id)
    }
  }
}
</script>

<style lang="scss" scoped>
.exhibition-detail {
  .breadcrumb {
    margin-bottom: 20px;
  }

  .exhibition-header {
    display: flex;
    gap: 30px;
    background: #fff;
    padding: 30px;
    border-radius: 12px;
    margin-bottom: 24px;

    @media (max-width: 800px) {
      flex-direction: column;
    }

    .header-cover {
      flex: 0 0 400px;
      height: 280px;
      position: relative;
      border-radius: 8px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .header-status {
        position: absolute;
        top: 16px;
        left: 16px;
        padding: 6px 16px;
        border-radius: 4px;
        font-size: 14px;

        &.ongoing { background: #67c23a; color: #fff; }
        &.ended { background: #909399; color: #fff; }
        &.upcoming { background: #e6a23c; color: #fff; }
      }
    }

    .header-info {
      flex: 1;

      .info-title {
        font-size: 28px;
        color: #2c1810;
        font-family: "楷体", "KaiTi", serif;
        margin-bottom: 16px;
      }

      .info-theme {
        font-size: 16px;
        color: #8b7355;
        margin-bottom: 16px;
      }

      .info-date {
        font-size: 14px;
        color: #a89070;
        margin-bottom: 20px;
      }

      .info-desc {
        line-height: 1.8;
        color: #4a4a4a;
      }
    }
  }

  .exhibition-items {
    background: #fff;
    padding: 30px;
    border-radius: 12px;

    .section-title {
      font-size: 20px;
      color: #2c1810;
      margin-bottom: 24px;
      display: flex;
      align-items: center;
      gap: 10px;

      i { color: #d4af37; }

      .item-count {
        font-size: 14px;
        color: #a89070;
        font-weight: normal;
        margin-left: auto;
      }
    }

    .bronze-grid {
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      gap: 20px;

      @media (max-width: 1200px) { grid-template-columns: repeat(4, 1fr); }
      @media (max-width: 900px) { grid-template-columns: repeat(3, 1fr); }
      @media (max-width: 600px) { grid-template-columns: repeat(2, 1fr); }
    }

    .bronze-card {
      background: #f9f5ef;
      border-radius: 8px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(44, 24, 16, 0.12);
      }

      .card-image {
        height: 140px;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }

      .card-info {
        padding: 12px;

        .card-title {
          font-size: 14px;
          color: #2c1810;
          text-align: center;
        }
      }
    }
  }
}
</style>
