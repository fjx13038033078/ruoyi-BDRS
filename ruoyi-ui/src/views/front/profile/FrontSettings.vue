<template>
  <div class="front-settings">
    <div class="page-header">
      <h1 class="page-title">账号设置</h1>
      <p class="page-subtitle">管理您的个人信息和密码</p>
    </div>

    <div class="settings-content">
      <!-- 个人信息卡片 -->
      <div class="settings-card">
        <div class="card-header">
          <h3>个人信息</h3>
        </div>
        <div class="card-body">
          <div class="avatar-section">
            <div class="avatar-wrapper">
              <img :src="userInfo.avatar" alt="avatar" />
            </div>
            <div class="avatar-info">
              <p class="user-name">{{ userInfo.userName }}</p>
              <p class="user-role">{{ roleGroup }}</p>
              <p class="user-date">注册于 {{ userInfo.createTime }}</p>
            </div>
          </div>

          <el-form ref="infoForm" :model="infoForm" :rules="infoRules" label-width="80px" class="info-form">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="infoForm.nickName" maxlength="30" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="手机号码" prop="phonenumber">
              <el-input v-model="infoForm.phonenumber" maxlength="11" placeholder="请输入手机号码" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="infoForm.email" maxlength="50" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="infoForm.sex">
                <el-radio label="0">男</el-radio>
                <el-radio label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitInfo">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 修改密码卡片 -->
      <div class="settings-card">
        <div class="card-header">
          <h3>修改密码</h3>
        </div>
        <div class="card-body">
          <el-form ref="pwdForm" :model="pwdForm" :rules="pwdRules" label-width="80px" class="pwd-form">
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" placeholder="请输入新密码" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitPwd">修改密码</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 返回按钮 -->
      <div class="back-btn">
        <el-button @click="$router.push('/front/profile')">
          <i class="el-icon-arrow-left"></i> 返回个人中心
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getUserProfile, updateUserProfile, updateUserPwd } from '@/api/system/user'

export default {
  name: 'FrontSettings',
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.pwdForm.newPassword !== value) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      userInfo: {},
      roleGroup: '',
      infoForm: {
        nickName: '',
        phonenumber: '',
        email: '',
        sex: '0'
      },
      pwdForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      infoRules: {
        nickName: [{ required: true, message: '昵称不能为空', trigger: 'blur' }],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phonenumber: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      },
      pwdRules: {
        oldPassword: [{ required: true, message: '旧密码不能为空', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '新密码不能为空', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { validator: equalToPassword, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadProfile()
  },
  methods: {
    loadProfile() {
      getUserProfile().then(response => {
        this.userInfo = response.data
        this.roleGroup = response.roleGroup
        const user = response.data
        this.infoForm = {
          nickName: user.nickName,
          phonenumber: user.phonenumber,
          email: user.email,
          sex: user.sex
        }
        // 处理头像
        if (!this.userInfo.avatar || this.userInfo.avatar === '') {
          this.userInfo.avatar = require('@/assets/images/profile.jpg')
        } else if (!this.userInfo.avatar.startsWith('http')) {
          this.userInfo.avatar = process.env.VUE_APP_BASE_API + this.userInfo.avatar
        }
      })
    },
    submitInfo() {
      this.$refs.infoForm.validate(valid => {
        if (valid) {
          updateUserProfile(this.infoForm).then(() => {
            this.$message.success('个人信息修改成功')
            // 更新store中的用户名
            this.$store.commit('SET_NAME', this.infoForm.nickName)
          })
        }
      })
    },
    submitPwd() {
      this.$refs.pwdForm.validate(valid => {
        if (valid) {
          updateUserPwd(this.pwdForm.oldPassword, this.pwdForm.newPassword).then(() => {
            this.$message.success('密码修改成功')
            this.pwdForm = { oldPassword: '', newPassword: '', confirmPassword: '' }
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.front-settings {
  max-width: 700px;
  margin: 0 auto;

  .page-header {
    text-align: center;
    margin-bottom: 30px;

    .page-title {
      font-size: 28px;
      color: #2c1810;
      font-family: "楷体", "KaiTi", serif;
      margin-bottom: 8px;
    }

    .page-subtitle {
      color: #8b7355;
      font-size: 14px;
    }
  }

  .settings-card {
    background: #fff;
    border-radius: 12px;
    margin-bottom: 20px;
    overflow: hidden;

    .card-header {
      padding: 18px 24px;
      border-bottom: 1px solid #f0e6d3;

      h3 {
        font-size: 18px;
        color: #2c1810;
        margin: 0;
      }
    }

    .card-body {
      padding: 24px;
    }
  }

  .avatar-section {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 30px;
    padding-bottom: 24px;
    border-bottom: 1px solid #f0e6d3;

    .avatar-wrapper {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      overflow: hidden;
      border: 3px solid #d4af37;
      flex-shrink: 0;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .avatar-info {
      .user-name {
        font-size: 20px;
        font-weight: 500;
        color: #2c1810;
        margin: 0 0 4px 0;
      }

      .user-role {
        font-size: 14px;
        color: #d4af37;
        margin: 0 0 4px 0;
      }

      .user-date {
        font-size: 12px;
        color: #a89070;
        margin: 0;
      }
    }
  }

  .info-form, .pwd-form {
    max-width: 480px;
  }

  .back-btn {
    text-align: center;
    margin-top: 10px;
  }
}
</style>
