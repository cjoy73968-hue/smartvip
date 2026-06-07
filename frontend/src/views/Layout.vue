<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="layout">
    <el-container>
      <el-aside width="200px">
        <div class="logo">会员管理</div>
        <el-menu :default-active="$route.path" router>
          <el-menu-item index="/dashboard">
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/members">
            <span>会员管理</span>
          </el-menu-item>
          <el-menu-item index="/orders">
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="/products">
            <span>商品管理</span>
          </el-menu-item>
          <el-menu-item index="/withdrawals">
            <span>提现管理</span>
          </el-menu-item>
          <el-menu-item index="/marketing">
            <span>营销推送</span>
          </el-menu-item>
          <el-menu-item index="/config">
            <span>系统配置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-right">
            <span>{{ user?.nickname || user?.username }}</span>
            <el-button @click="handleLogout" size="small">退出</el-button>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = ref(null)

onMounted(() => {
  const userStr = localStorage.getItem('merchant_user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  }
})

const handleLogout = () => {
  localStorage.removeItem('merchant_token')
  localStorage.removeItem('merchant_user')
  router.push('/login')
}
</script>

<style scoped>
.layout {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}

.el-menu {
  border: none;
  background-color: #304156;
}

.el-menu-item {
  color: #bfcbd9;
}

.el-menu-item:hover,
.el-menu-item.is-active {
  background-color: #263445;
  color: #409eff;
}

.el-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.el-main {
  background-color: #f5f5f5;
}
</style>