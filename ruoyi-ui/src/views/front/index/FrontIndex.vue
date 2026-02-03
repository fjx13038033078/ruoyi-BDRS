<template>
  <div class="front-index">
    <!-- 横幅区域 -->
    <section class="banner-section">
      <div class="banner-content">
        <h1 class="banner-title">探索青铜文明</h1>
        <p class="banner-subtitle">穿越千年时光，感受华夏先民的智慧结晶</p>
        <el-button type="primary" class="explore-btn" @click="$router.push('/front/bronze')">
          开始探索 <i class="el-icon-arrow-right"></i>
        </el-button>
      </div>
      <div class="banner-decoration">
        <div class="decoration-line"></div>
        <span class="decoration-text">青铜时代 · 礼乐文明</span>
        <div class="decoration-line"></div>
      </div>
    </section>

    <!-- 猜你喜欢 -->
    <section class="recommend-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-magic-stick"></i>
          猜你喜欢
        </h2>
        <span class="section-subtitle">{{ recommendSubtitle }}</span>
      </div>
      <div class="recommend-content" v-loading="recommendLoading">
        <template v-if="recommendList.length > 0">
          <div class="bronze-grid">
            <div
              v-for="item in recommendList"
              :key="item.id"
              class="bronze-card"
              @click="goDetail(item.id)"
            >
              <div class="card-image">
                <img :src="getImageUrl(item.images)" :alt="item.name" />
                <div class="card-overlay">
                  <span>查看详情</span>
                </div>
                <div class="card-badge recommend-badge" v-if="isLogin && !isColdStart">
                  <i class="el-icon-star-on"></i> 为你推荐
                </div>
              </div>
              <div class="card-info">
                <h3 class="card-title">{{ item.name }}</h3>
                <p class="card-meta">
                  <span v-if="item.dynasty">{{ getDictLabel('bus_dynasty', item.dynasty) }}</span>
                  <span v-if="item.category">{{ getDictLabel('bus_category', item.category) }}</span>
                </p>
              </div>
            </div>
          </div>
        </template>
        <template v-else-if="!recommendLoading">
          <div class="empty-recommend">
            <i class="el-icon-discover"></i>
            <p>暂无推荐内容</p>
            <el-button type="primary" plain @click="$router.push('/front/bronze')">
              去浏览藏品
            </el-button>
          </div>
        </template>
      </div>
    </section>

    <!-- 热门藏品 -->
    <section class="hot-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-s-data"></i>
          热门藏品
        </h2>
        <span class="section-subtitle">最受欢迎的青铜器精品</span>
        <router-link to="/front/bronze" class="view-more">
          查看更多 <i class="el-icon-arrow-right"></i>
        </router-link>
      </div>
      <div class="hot-content" v-loading="hotLoading">
        <div class="bronze-grid">
          <div
            v-for="item in hotList"
            :key="item.id"
            class="bronze-card"
            @click="goDetail(item.id)"
          >
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
      </div>
    </section>

    <!-- 最新展览 -->
    <section class="exhibition-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-picture-outline"></i>
          精彩展览
        </h2>
        <span class="section-subtitle">正在进行的数字展览</span>
        <router-link to="/front/exhibition" class="view-more">
          查看更多 <i class="el-icon-arrow-right"></i>
        </router-link>
      </div>
      <div class="exhibition-content" v-loading="exhibitionLoading">
        <div class="exhibition-grid">
          <div
            v-for="item in exhibitionList"
            :key="item.id"
            class="exhibition-card"
            @click="goExhibition(item.id)"
          >
            <div class="exhibition-cover">
              <img :src="getImageUrl(item.coverImg)" :alt="item.title" />
              <div class="exhibition-status" :class="getStatusClass(item.status)">
                {{ getStatusText(item.status) }}
              </div>
            </div>
            <div class="exhibition-info">
              <h3 class="exhibition-title">{{ item.title }}</h3>
              <p class="exhibition-theme" v-if="item.theme">{{ item.theme }}</p>
              <p class="exhibition-date">
                <i class="el-icon-date"></i>
                {{ formatDate(item.startDate) }} - {{ formatDate(item.endDate) }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { listWare } from '@/api/bronze/ware'
import { listExhibition } from '@/api/bronze/exhibition'
import { getRecommendations, getHotRecommendations } from '@/api/bronze/recommend'

export default {
  name: 'FrontIndex',
  dicts: ['bus_dynasty', 'bus_category', 'bus_usage'],
  data() {
    return {
      // 推荐列表
      recommendList: [],
      recommendLoading: false,
      isColdStart: false, // 是否为冷启动推荐
      // 热门列表
      hotList: [],
      hotLoading: false,
      // 展览列表
      exhibitionList: [],
      exhibitionLoading: false
    }
  },
  computed: {
    isLogin() {
      return this.$store.getters.token
    },
    recommendSubtitle() {
      if (this.isColdStart) {
        return '热门精选推荐'
      }
      return this.isLogin ? '基于您的浏览偏好智能推荐' : '登录后获取个性化推荐'
    }
  },
  created() {
    this.loadRecommendList()
    this.loadHotList()
    this.loadExhibitionList()
  },
  methods: {
    /** 加载推荐列表 */
    loadRecommendList() {
      this.recommendLoading = true
      // 调用推荐API，后端会根据用户登录状态和行为记录返回个性化推荐或冷启动推荐
      getRecommendations(8).then(response => {
        this.recommendList = response.data || []
        // 判断是否为冷启动：未登录或返回的是热门推荐
        this.isColdStart = !this.isLogin
        this.recommendLoading = false
      }).catch(() => {
        // 降级：获取热门推荐
        this.loadColdStartRecommend()
      })
    },
    /** 加载冷启动推荐（降级策略） */
    loadColdStartRecommend() {
      getHotRecommendations(8).then(response => {
        this.recommendList = response.data || []
        this.isColdStart = true
        this.recommendLoading = false
      }).catch(() => {
        this.recommendLoading = false
      })
    },
    /** 加载热门藏品 */
    loadHotList() {
      this.hotLoading = true
      // 按浏览量排序获取前8个
      listWare({ pageNum: 1, pageSize: 8 }).then(response => {
        // 按浏览量排序
        this.hotList = response.rows.sort((a, b) => (b.viewCount || 0) - (a.viewCount || 0))
        this.hotLoading = false
      }).catch(() => {
        this.hotLoading = false
      })
    },
    /** 加载展览列表 */
    loadExhibitionList() {
      this.exhibitionLoading = true
      listExhibition({ pageNum: 1, pageSize: 4, status: '0' }).then(response => {
        this.exhibitionList = response.rows
        this.exhibitionLoading = false
      }).catch(() => {
        this.exhibitionLoading = false
      })
    },
    /** 获取图片URL */
    getImageUrl(images) {
      if (!images) {
        return require('@/assets/images/profile.jpg')
      }
      const firstImg = images.split(',')[0]
      if (firstImg.startsWith('http')) {
        return firstImg
      }
      return process.env.VUE_APP_BASE_API + firstImg
    },
    /** 获取字典标签 */
    getDictLabel(dictType, value) {
      const dict = this.dict.type[dictType]
      if (dict) {
        const item = dict.find(d => d.value === value)
        return item ? item.label : value
      }
      return value
    },
    /** 跳转藏品详情 */
    goDetail(id) {
      this.$router.push('/front/bronze/' + id)
    },
    /** 跳转展览详情 */
    goExhibition(id) {
      this.$router.push('/front/exhibition/' + id)
    },
    /** 格式化日期 */
    formatDate(date) {
      if (!date) return ''
      return date.substring(0, 10)
    },
    /** 获取状态样式 */
    getStatusClass(status) {
      const map = { '0': 'ongoing', '1': 'ended', '2': 'upcoming' }
      return map[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const map = { '0': '进行中', '1': '已结束', '2': '即将开展' }
      return map[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.front-index {
  // 横幅区域
  .banner-section {
    background: linear-gradient(135deg, #2c1810 0%, #4a2c17 50%, #654321 100%);
    border-radius: 12px;
    padding: 60px 40px;
    margin-bottom: 40px;
    text-align: center;
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="50" cy="50" r="40" fill="none" stroke="%23d4af37" stroke-width="0.5" opacity="0.1"/></svg>');
      background-size: 100px 100px;
      opacity: 0.3;
    }

    .banner-content {
      position: relative;
      z-index: 1;
    }

    .banner-title {
      font-size: 48px;
      color: #d4af37;
      font-family: "楷体", "KaiTi", serif;
      letter-spacing: 8px;
      margin-bottom: 16px;
      text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.4);
    }

    .banner-subtitle {
      font-size: 18px;
      color: #e8dcc8;
      margin-bottom: 30px;
      letter-spacing: 2px;
    }

    .explore-btn {
      background: linear-gradient(135deg, #d4af37 0%, #c9a227 100%);
      border: none;
      color: #2c1810;
      font-size: 16px;
      padding: 14px 36px;
      border-radius: 30px;
      font-weight: 500;
      letter-spacing: 2px;

      &:hover {
        background: linear-gradient(135deg, #e5c04b 0%, #d4af37 100%);
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(212, 175, 55, 0.4);
      }
    }

    .banner-decoration {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 40px;
      position: relative;
      z-index: 1;

      .decoration-line {
        width: 100px;
        height: 1px;
        background: linear-gradient(90deg, transparent, #d4af37, transparent);
      }

      .decoration-text {
        color: #d4af37;
        font-size: 14px;
        padding: 0 20px;
        font-family: "楷体", "KaiTi", serif;
        letter-spacing: 4px;
      }
    }
  }

  // 通用区域头部
  .section-header {
    display: flex;
    align-items: center;
    margin-bottom: 24px;
    flex-wrap: wrap;
    gap: 12px;

    .section-title {
      font-size: 24px;
      color: #2c1810;
      font-family: "楷体", "KaiTi", serif;
      display: flex;
      align-items: center;
      gap: 10px;

      i {
        color: #d4af37;
      }
    }

    .section-subtitle {
      color: #8b7355;
      font-size: 14px;
    }

    .view-more {
      margin-left: auto;
      color: #8b7355;
      text-decoration: none;
      font-size: 14px;

      &:hover {
        color: #d4af37;
      }
    }
  }

  // 推荐区域
  .recommend-section {
    margin-bottom: 50px;

    .empty-recommend {
      background: #fff;
      border-radius: 12px;
      padding: 60px 40px;
      text-align: center;
      border: 2px dashed #d4c5a9;

      i {
        font-size: 64px;
        color: #d4c5a9;
        margin-bottom: 20px;
      }

      p {
        color: #8b7355;
        font-size: 16px;
        margin-bottom: 20px;
      }
    }
  }

  // 藏品网格
  .bronze-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24px;

    @media (max-width: 1200px) {
      grid-template-columns: repeat(3, 1fr);
    }

    @media (max-width: 900px) {
      grid-template-columns: repeat(2, 1fr);
    }

    @media (max-width: 600px) {
      grid-template-columns: 1fr;
    }
  }

  // 藏品卡片
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

      .card-overlay {
        opacity: 1;
      }

      .card-image img {
        transform: scale(1.05);
      }
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
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
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
        display: flex;
        align-items: center;
        gap: 4px;

        &.recommend-badge {
          background: linear-gradient(135deg, #d4af37 0%, #c9a227 100%);
          color: #2c1810;
          font-weight: 500;
        }
      }
    }

    .card-info {
      padding: 16px;

      .card-title {
        font-size: 16px;
        color: #2c1810;
        margin-bottom: 8px;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
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
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }

  // 热门区域
  .hot-section {
    margin-bottom: 50px;
  }

  // 展览区域
  .exhibition-section {
    margin-bottom: 30px;
  }

  .exhibition-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;

    @media (max-width: 900px) {
      grid-template-columns: 1fr;
    }
  }

  .exhibition-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(44, 24, 16, 0.1);
    cursor: pointer;
    display: flex;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 24px rgba(44, 24, 16, 0.15);
    }

    .exhibition-cover {
      width: 200px;
      min-height: 160px;
      position: relative;
      flex-shrink: 0;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .exhibition-status {
        position: absolute;
        top: 10px;
        left: 10px;
        padding: 4px 12px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: 500;

        &.ongoing {
          background: #67c23a;
          color: #fff;
        }

        &.ended {
          background: #909399;
          color: #fff;
        }

        &.upcoming {
          background: #e6a23c;
          color: #fff;
        }
      }
    }

    .exhibition-info {
      padding: 20px;
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .exhibition-title {
        font-size: 18px;
        color: #2c1810;
        margin-bottom: 10px;
        font-weight: 500;
      }

      .exhibition-theme {
        font-size: 14px;
        color: #8b7355;
        margin-bottom: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }

      .exhibition-date {
        font-size: 13px;
        color: #a89070;
        display: flex;
        align-items: center;
        gap: 6px;
      }
    }
  }
}
</style>
