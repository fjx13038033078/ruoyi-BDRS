<template>
  <div class="front-favorite">
    <div class="page-header">
      <h1 class="page-title">我的收藏</h1>
      <p class="page-subtitle">您收藏的青铜器藏品</p>
    </div>

    <div class="favorite-list" v-loading="loading">
      <div class="bronze-grid" v-if="favoriteList.length > 0">
        <div v-for="item in favoriteList" :key="item.id" class="bronze-card" @click="goDetail(item.bronzeId)">
          <div class="card-image">
            <img :src="getImageUrl(item.bronzeName)" :alt="item.bronzeName" />
            <div class="card-overlay">
              <span>查看详情</span>
            </div>
          </div>
          <div class="card-info">
            <h3 class="card-title">{{ item.bronzeName }}</h3>
            <p class="card-time">
              <i class="el-icon-time"></i> 收藏于 {{ formatTime(item.createTime) }}
            </p>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else-if="!loading">
        <i class="el-icon-star-off"></i>
        <p>您还没有收藏任何藏品</p>
        <el-button type="primary" @click="$router.push('/front/bronze')">去浏览藏品</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { myFavorites } from '@/api/bronze/action'

export default {
  name: 'FrontFavorite',
  data() {
    return {
      loading: false,
      favoriteList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      myFavorites().then(response => {
        this.favoriteList = response.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getImageUrl(name) {
      return require('@/assets/images/profile.jpg')
    },
    formatTime(time) {
      return time ? time.substring(0, 10) : ''
    },
    goDetail(id) {
      this.$router.push('/front/bronze/' + id)
    }
  }
}
</script>

<style lang="scss" scoped>
.front-favorite {
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
    }

    .card-image {
      position: relative;
      height: 180px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
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
          border: 1px solid #d4af37;
          padding: 8px 20px;
          border-radius: 20px;
        }
      }
    }

    .card-info {
      padding: 16px;

      .card-title {
        font-size: 16px;
        color: #2c1810;
        margin-bottom: 8px;
      }

      .card-time {
        font-size: 12px;
        color: #a89070;
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 80px;
    color: #a89070;

    i { font-size: 80px; margin-bottom: 20px; color: #d4c5a9; }
    p { font-size: 16px; margin-bottom: 20px; }
  }
}
</style>
