<template>
  <div class="front-layout">
    <!-- 顶部导航栏 -->
    <header class="front-header">
      <div class="header-container">
        <div class="logo" @click="$router.push('/front')">
          <img src="@/assets/logo/logo.png" alt="logo" class="logo-img" v-if="false" />
          <span class="logo-title">青铜器数字博物馆</span>
        </div>
        <nav class="nav-menu">
          <router-link to="/front" class="nav-item" exact>
            <i class="el-icon-s-home"></i> 首页
          </router-link>
          <router-link to="/front/bronze" class="nav-item">
            <i class="el-icon-goods"></i> 青铜器库
          </router-link>
          <router-link to="/front/exhibition" class="nav-item">
            <i class="el-icon-picture-outline"></i> 数字展览
          </router-link>
          <router-link to="/front/favorite" class="nav-item" v-if="isLogin">
            <i class="el-icon-star-off"></i> 我的收藏
          </router-link>
        </nav>
        <div class="user-info">
          <template v-if="isLogin">
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                <img :src="avatar" class="user-avatar" />
                <span class="user-name">{{ name }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile">
                  <i class="el-icon-user"></i> 个人中心
                </el-dropdown-item>
                <el-dropdown-item command="admin" v-if="isAdmin">
                  <i class="el-icon-setting"></i> 管理后台
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <i class="el-icon-switch-button"></i> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="login-btn">登录</router-link>
            <router-link to="/register" class="register-btn">注册</router-link>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="front-main">
      <router-view />
    </main>

    <!-- 底部 -->
    <footer class="front-footer">
      <div class="footer-container">
        <p>青铜器数字推荐系统 &copy; {{ new Date().getFullYear() }}</p>
        <p>传承华夏文明 · 弘扬青铜文化</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'FrontLayout',
  computed: {
    ...mapGetters(['avatar', 'name', 'roles']),
    isLogin() {
      return this.$store.getters.token
    },
    isAdmin() {
      return this.roles && (this.roles.includes('admin') || this.roles.includes('staff'))
    }
  },
  methods: {
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/front/profile')
          break
        case 'admin':
          this.$router.push('/index')
          break
        case 'logout':
          this.$store.dispatch('LogOut').then(() => {
            location.href = '/front'
          })
          break
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f0e6;
}

// 顶部导航
.front-header {
  background: linear-gradient(135deg, #2c1810 0%, #4a2c17 50%, #654321 100%);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
  position: sticky;
  top: 0;
  z-index: 100;

  .header-container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 20px;
    height: 70px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .logo {
    display: flex;
    align-items: center;
    cursor: pointer;

    .logo-img {
      height: 45px;
      margin-right: 12px;
    }

    .logo-title {
      font-size: 24px;
      font-weight: bold;
      color: #d4af37;
      font-family: "楷体", "KaiTi", serif;
      letter-spacing: 3px;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
    }
  }

  .nav-menu {
    display: flex;
    gap: 8px;

    .nav-item {
      color: #e8dcc8;
      text-decoration: none;
      padding: 10px 20px;
      border-radius: 4px;
      transition: all 0.3s ease;
      font-size: 16px;
      display: flex;
      align-items: center;
      gap: 6px;

      i {
        font-size: 18px;
      }

      &:hover {
        background-color: rgba(212, 175, 55, 0.2);
        color: #d4af37;
      }

      &.router-link-active {
        background-color: rgba(212, 175, 55, 0.3);
        color: #d4af37;
        font-weight: 500;
      }
    }
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 15px;

    .user-dropdown {
      display: flex;
      align-items: center;
      cursor: pointer;
      color: #e8dcc8;

      .user-avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        margin-right: 8px;
        border: 2px solid #d4af37;
      }

      .user-name {
        margin-right: 5px;
      }
    }

    .login-btn, .register-btn {
      padding: 8px 20px;
      border-radius: 4px;
      text-decoration: none;
      font-size: 14px;
      transition: all 0.3s ease;
    }

    .login-btn {
      color: #d4af37;
      border: 1px solid #d4af37;

      &:hover {
        background-color: #d4af37;
        color: #2c1810;
      }
    }

    .register-btn {
      background-color: #d4af37;
      color: #2c1810;

      &:hover {
        background-color: #c9a227;
      }
    }
  }
}

// 主内容区
.front-main {
  flex: 1;
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px 20px;
  width: 100%;
  box-sizing: border-box;
}

// 底部
.front-footer {
  background: linear-gradient(135deg, #2c1810 0%, #4a2c17 100%);
  color: #e8dcc8;
  padding: 30px 20px;
  text-align: center;

  .footer-container {
    max-width: 1400px;
    margin: 0 auto;

    p {
      margin: 8px 0;
      font-size: 14px;

      &:last-child {
        color: #d4af37;
        font-family: "楷体", "KaiTi", serif;
        letter-spacing: 2px;
      }
    }
  }
}
</style>
