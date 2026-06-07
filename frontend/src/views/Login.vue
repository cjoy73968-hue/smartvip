<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="login-container">
    <el-card class="login-card">
      <h2>商家后台登录</h2>
      <el-form @submit.prevent="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '../api'

const username = ref('')
const password = ref('')
const loading = ref(false)
const router = useRouter()

const handleLogin = async () => {
  if (!username.value || !password.value) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await api.login(username.value, password.value)
    if (res.data.code === 200) {
      localStorage.setItem('merchant_token', 'mock-token')
      localStorage.setItem('merchant_user', JSON.stringify(res.data.data))
      router.push('/dashboard')
    } else {
      ElMessage.error(res.data.message || '登录失败')
    }
  } catch (err) {
    ElMessage.error('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>