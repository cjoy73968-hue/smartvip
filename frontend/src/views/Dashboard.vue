<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="dashboard">
    <h2>数据概览</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div class="stat-value">{{ stats.totalMembers }}</div>
          <div class="stat-label">会员总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-value">{{ stats.activeMembers }}</div>
          <div class="stat-label">有效会员</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-value">{{ stats.pendingDeliveries }}</div>
          <div class="stat-label">待发货</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-value">¥{{ stats.totalRecharge }}</div>
          <div class="stat-label">总充值金额</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const stats = ref({
  totalMembers: 0,
  activeMembers: 0,
  pendingDeliveries: 0,
  totalRecharge: 0
})

onMounted(async () => {
  try {
    const res = await api.getOrderStatistics()
    if (res.data.code === 200) {
      stats.value.pendingDeliveries = res.data.data.pendingDelivery || 0
    }
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
  padding: 10px 0;
}

.stat-label {
  text-align: center;
  color: #666;
  font-size: 14px;
}
</style>