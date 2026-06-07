<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="withdrawals">
    <h2>提现管理</h2>
    <el-card>
      <el-table :data="withdrawals" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="amount" label="金额" />
        <el-table-column prop="createTime" label="申请时间" />
        <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag>{{ getStatus(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" size="small" type="primary" @click="handleConfirm(row)">
              确认
            </el-button>
            <el-button v-if="row.status === 1" size="small" type="success" @click="handleComplete(row)">
              完成打款
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const withdrawals = ref([])

const loadWithdrawals = async () => {
  try {
    const res = await api.getWithdrawals()
    if (res.data.code === 200) {
      withdrawals.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载提现列表失败')
  }
}

const handleConfirm = async (row) => {
  try {
    await api.confirmWithdrawal(row.id)
    ElMessage.success('确认成功')
    loadWithdrawals()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleComplete = async (row) => {
  try {
    await api.completeWithdrawal(row.id)
    ElMessage.success('完成打款')
    loadWithdrawals()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const getStatus = (status) => {
  const map = { 0: '申请中', 1: '已确认', 2: '已完成' }
  return map[status] || '未知'
}

onMounted(() => {
  loadWithdrawals()
})
</script>

<style scoped>
.withdrawals {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}
</style>