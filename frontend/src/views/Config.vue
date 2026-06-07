<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="config">
    <h2>系统配置</h2>
    <el-card>
      <el-form label-width="150px">
        <el-form-item label="返利比例">
          <el-input v-model="config.rebateRate" style="width: 200px">
            <template #append>%</template>
          </el-input>
          <el-button type="primary" size="small" @click="updateRebateRate">保存</el-button>
        </el-form-item>
        <el-form-item label="会员价格">
          <el-input v-model="config.membershipPrice" style="width: 200px" />
          <el-button type="primary" size="small" @click="updateMembershipPrice">保存</el-button>
        </el-form-item>
        <el-form-item label="会员有效期（天）">
          <el-input v-model="config.membershipDays" style="width: 200px" />
        </el-form-item>
        <el-form-item label="派送周期">
          <el-select v-model="config.deliveryCycle" style="width: 200px">
            <el-option label="每日" value="daily" />
            <el-option label="每周" value="weekly" />
            <el-option label="每月" value="monthly" />
            <el-option label="每季度" value="quarterly" />
          </el-select>
        </el-form-item>
        <el-form-item label="配送城市">
          <el-input v-model="config.deliveryCity" type="textarea" style="width: 400px" />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const config = ref({
  rebateRate: '5',
  membershipPrice: '299',
  membershipDays: 365,
  deliveryCycle: 'monthly',
  deliveryCity: ''
})

const loadConfig = async () => {
  try {
    const res = await api.getConfig()
    if (res.data.code === 200 && res.data.data) {
      config.value = {
        rebateRate: String(Number(res.data.data.rebateRate) * 100),
        membershipPrice: String(res.data.data.membershipPrice),
        membershipDays: res.data.data.membershipDays,
        deliveryCycle: res.data.data.deliveryCycle,
        deliveryCity: res.data.data.deliveryCity
      }
    }
  } catch (e) {
    ElMessage.error('加载配置失败')
  }
}

const updateRebateRate = async () => {
  try {
    await api.updateRebateRate(config.value.rebateRate)
    ElMessage.success('更新成功')
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const updateMembershipPrice = async () => {
  try {
    await api.updateMembershipPrice(config.value.membershipPrice)
    ElMessage.success('更新成功')
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.config {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}
</style>