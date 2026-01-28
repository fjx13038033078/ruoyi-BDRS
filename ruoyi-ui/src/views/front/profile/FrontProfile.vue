<template>
  <div class="front-profile">
    <div class="page-header">
      <h1 class="page-title">个人中心</h1>
    </div>

    <div class="profile-content">
      <div class="profile-card">
        <div class="user-avatar">
          <img :src="avatar" alt="avatar" />
        </div>
        <h2 class="user-name">{{ name }}</h2>
        <p class="user-role">{{ roleText }}</p>
      </div>

      <div class="profile-menu">
        <div class="menu-item" @click="$router.push('/front/favorite')">
          <i class="el-icon-star-off"></i>
          <span>我的收藏</span>
          <i class="el-icon-arrow-right"></i>
        </div>
        <div class="menu-item" @click="$router.push('/user/profile')">
          <i class="el-icon-user"></i>
          <span>账号设置</span>
          <i class="el-icon-arrow-right"></i>
        </div>
        <div class="menu-item" @click="handleLogout">
          <i class="el-icon-switch-button"></i>
          <span>退出登录</span>
          <i class="el-icon-arrow-right"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'FrontProfile',
  computed: {
    ...mapGetters(['avatar', 'name', 'roles']),
    roleText() {
      if (this.roles && this.roles.includes('admin')) return '管理员'
      if (this.roles && this.roles.includes('staff')) return '工作人员'
      return '普通用户'
    }
  },
  methods: {
    handleLogout() {
      this.$confirm('确定要退出登录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/front'
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.front-profile {
  max-width: 600px;
  margin: 0 auto;

  .page-header {
    text-align: center;
    margin-bottom: 30px;

    .page-title {
      font-size: 28px;
      color: #2c1810;
      font-family: "楷体", "KaiTi", serif;
    }
  }

  .profile-card {
    background: #fff;
    border-radius: 12px;
    padding: 40px;
    text-align: center;
    margin-bottom: 20px;

    .user-avatar {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      margin: 0 auto 16px;
      overflow: hidden;
      border: 3px solid #d4af37;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .user-name {
      font-size: 24px;
      color: #2c1810;
      margin-bottom: 8px;
    }

    .user-role {
      color: #8b7355;
    }
  }

  .profile-menu {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;

    .menu-item {
      display: flex;
      align-items: center;
      padding: 16px 20px;
      cursor: pointer;
      transition: background 0.2s;

      &:not(:last-child) {
        border-bottom: 1px solid #f0e6d3;
      }

      &:hover {
        background: #faf8f5;
      }

      i:first-child {
        font-size: 20px;
        color: #d4af37;
        margin-right: 12px;
      }

      span {
        flex: 1;
        color: #2c1810;
      }

      i:last-child {
        color: #c0c4cc;
      }
    }
  }
}
</style>
