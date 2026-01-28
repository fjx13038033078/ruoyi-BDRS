<template>
  <div class="bronze-detail" v-loading="loading">
    <div class="detail-container" v-if="bronze">
      <!-- 面包屑 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/front' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/front/bronze' }">青铜器库</el-breadcrumb-item>
        <el-breadcrumb-item>{{ bronze.name }}</el-breadcrumb-item>
      </el-breadcrumb>

      <div class="detail-content">
        <!-- 左侧图片 -->
        <div class="detail-image">
          <div class="main-image">
            <img :src="currentImage" :alt="bronze.name" />
          </div>
          <div class="image-list" v-if="imageList.length > 1">
            <div
              v-for="(img, index) in imageList"
              :key="index"
              class="image-thumb"
              :class="{ active: currentImage === img }"
              @click="currentImage = img"
            >
              <img :src="img" />
            </div>
          </div>
        </div>

        <!-- 右侧信息 -->
        <div class="detail-info">
          <h1 class="info-title">{{ bronze.name }}</h1>

          <div class="info-meta">
            <span v-if="bronze.collectionNo" class="meta-item">
              <i class="el-icon-document"></i> {{ bronze.collectionNo }}
            </span>
            <span class="meta-item">
              <i class="el-icon-view"></i> {{ bronze.viewCount || 0 }} 次浏览
            </span>
          </div>

          <div class="info-tags">
            <el-tag v-if="bronze.dynasty" type="warning">{{ getDictLabel('bus_dynasty', bronze.dynasty) }}</el-tag>
            <el-tag v-if="bronze.category" type="success">{{ getDictLabel('bus_category', bronze.category) }}</el-tag>
            <el-tag v-if="bronze.usageType" type="info">{{ getDictLabel('bus_usage', bronze.usageType) }}</el-tag>
          </div>

          <div class="info-list">
            <div class="info-row" v-if="bronze.pattern">
              <span class="info-label">纹饰类型</span>
              <span class="info-value">{{ bronze.pattern }}</span>
            </div>
            <div class="info-row" v-if="bronze.excavationPlace">
              <span class="info-label">出土地点</span>
              <span class="info-value">{{ bronze.excavationPlace }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">展出状态</span>
              <span class="info-value">
                <el-tag :type="bronze.status === '0' ? 'success' : 'info'" size="small">
                  {{ bronze.status === '0' ? '在展' : bronze.status === '1' ? '库藏' : '修复中' }}
                </el-tag>
              </span>
            </div>
          </div>

          <div class="info-actions" v-if="isLogin">
            <el-button
              :type="isFavorite ? 'warning' : 'default'"
              @click="handleFavorite"
              :loading="favoriteLoading"
            >
              <i :class="isFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
              {{ isFavorite ? '已收藏' : '收藏' }}
            </el-button>
            <el-button
              :type="isLiked ? 'danger' : 'default'"
              @click="handleLike"
              :loading="likeLoading"
            >
              <i class="el-icon-thumb"></i>
              {{ isLiked ? '已点赞' : '点赞' }}
            </el-button>
            <el-rate
              v-model="myScore"
              :max="5"
              :allow-half="true"
              show-text
              :texts="['很差', '较差', '一般', '推荐', '力荐']"
              @change="handleRate"
              class="rate-component"
            />
          </div>
        </div>
      </div>

      <!-- 详细介绍 -->
      <div class="detail-intro" v-if="bronze.intro">
        <h2 class="intro-title">
          <i class="el-icon-document"></i> 详细介绍
        </h2>
        <div class="intro-content">{{ bronze.intro }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { getWare } from '@/api/bronze/ware'
import { recordView } from '@/api/bronze/viewlog'
import { toggleFavorite, toggleLike, checkFavorite, checkLike, getMyScore, rateBronze } from '@/api/bronze/action'

export default {
  name: 'FrontBronzeDetail',
  dicts: ['bus_dynasty', 'bus_category', 'bus_usage'],
  data() {
    return {
      loading: false,
      bronze: null,
      currentImage: '',
      imageList: [],
      isFavorite: false,
      isLiked: false,
      myScore: 0,
      favoriteLoading: false,
      likeLoading: false
    }
  },
  computed: {
    isLogin() {
      return this.$store.getters.token
    }
  },
  created() {
    this.loadDetail()
  },
  methods: {
    loadDetail() {
      const id = this.$route.params.id
      this.loading = true

      getWare(id).then(response => {
        this.bronze = response.data
        this.processImages()
        this.loading = false

        // 记录浏览
        recordView(id).catch(() => {})

        // 加载用户交互状态
        if (this.isLogin) {
          this.loadUserActions(id)
        }
      }).catch(() => {
        this.loading = false
      })
    },
    processImages() {
      if (this.bronze.images) {
        this.imageList = this.bronze.images.split(',').map(img => {
          return img.startsWith('http') ? img : process.env.VUE_APP_BASE_API + img
        })
        this.currentImage = this.imageList[0]
      } else {
        this.currentImage = require('@/assets/images/profile.jpg')
      }
    },
    loadUserActions(id) {
      checkFavorite(id).then(res => { this.isFavorite = res.data })
      checkLike(id).then(res => { this.isLiked = res.data })
      getMyScore(id).then(res => { this.myScore = res.data || 0 })
    },
    getDictLabel(dictType, value) {
      const dict = this.dict.type[dictType]
      if (dict) {
        const item = dict.find(d => d.value === value)
        return item ? item.label : value
      }
      return value
    },
    handleFavorite() {
      this.favoriteLoading = true
      toggleFavorite(this.bronze.id).then(() => {
        this.isFavorite = !this.isFavorite
        this.$message.success(this.isFavorite ? '收藏成功' : '已取消收藏')
        this.favoriteLoading = false
      }).catch(() => {
        this.favoriteLoading = false
      })
    },
    handleLike() {
      this.likeLoading = true
      toggleLike(this.bronze.id).then(() => {
        this.isLiked = !this.isLiked
        this.$message.success(this.isLiked ? '点赞成功' : '已取消点赞')
        this.likeLoading = false
      }).catch(() => {
        this.likeLoading = false
      })
    },
    handleRate(value) {
      if (value > 0) {
        rateBronze(this.bronze.id, value).then(() => {
          this.$message.success('评分成功')
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.bronze-detail {
  .breadcrumb {
    margin-bottom: 20px;
  }

  .detail-content {
    display: flex;
    gap: 40px;
    background: #fff;
    padding: 30px;
    border-radius: 12px;
    margin-bottom: 24px;

    @media (max-width: 900px) {
      flex-direction: column;
    }
  }

  .detail-image {
    flex: 0 0 450px;

    .main-image {
      width: 100%;
      height: 400px;
      border-radius: 8px;
      overflow: hidden;
      margin-bottom: 16px;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .image-list {
      display: flex;
      gap: 10px;
      overflow-x: auto;

      .image-thumb {
        width: 80px;
        height: 80px;
        border-radius: 6px;
        overflow: hidden;
        cursor: pointer;
        border: 2px solid transparent;

        &.active {
          border-color: #d4af37;
        }

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
    }
  }

  .detail-info {
    flex: 1;

    .info-title {
      font-size: 28px;
      color: #2c1810;
      font-family: "楷体", "KaiTi", serif;
      margin-bottom: 16px;
    }

    .info-meta {
      display: flex;
      gap: 20px;
      margin-bottom: 20px;
      color: #8b7355;

      .meta-item {
        display: flex;
        align-items: center;
        gap: 6px;
      }
    }

    .info-tags {
      display: flex;
      gap: 10px;
      margin-bottom: 24px;
    }

    .info-list {
      border-top: 1px solid #f0e6d3;
      padding-top: 20px;

      .info-row {
        display: flex;
        margin-bottom: 16px;

        .info-label {
          width: 100px;
          color: #8b7355;
        }

        .info-value {
          flex: 1;
          color: #2c1810;
        }
      }
    }

    .info-actions {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-top: 24px;
      padding-top: 24px;
      border-top: 1px solid #f0e6d3;

      .rate-component {
        margin-left: auto;
      }
    }
  }

  .detail-intro {
    background: #fff;
    padding: 30px;
    border-radius: 12px;

    .intro-title {
      font-size: 20px;
      color: #2c1810;
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      gap: 10px;

      i { color: #d4af37; }
    }

    .intro-content {
      line-height: 1.8;
      color: #4a4a4a;
      white-space: pre-wrap;
    }
  }
}
</style>
