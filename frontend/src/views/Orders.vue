<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="orders">
    <h2>订单管理</h2>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="消费订单" name="consumption">
        <el-table :data="consumptionOrders" border>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="productName" label="商品" />
          <el-table-column prop="amount" label="金额" />
          <el-table-column prop="createTime" label="下单时间" />
          <el-table-column label="状态">
            <template #default="{ row }">
              <el-tag>{{ getDeliverStatus(row.deliverStatus) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="派送订单" name="delivery">
        <el-table :data="deliveryOrders" border>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="giftName" label="礼品" />
          <el-table-column prop="priceDiff" label="差价" />
          <el-table-column prop="createTime" label="下单时间" />
          <el-table-column label="类型">
            <template #default="{ row }">
              <el-tag :type="row.orderType === 1 ? 'success' : 'primary'">
                {{ row.orderType === 1 ? '周期派送' : '一次性' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态">
            <template #default="{ row }">
              <el-tag>{{ getDeliveryStatus(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button v-if="row.status === 1" size="small" type="primary" @click="confirmDeliver(row)">
                确认送达
              </el-button>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const activeTab = ref('consumption')
const consumptionOrders = ref([])
const deliveryOrders = ref([])

const loadConsumptionOrders = async () => {
  try {
    const res = await api.getOrders()
    if (res.data.code === 200) {
      consumptionOrders.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载消费订单失败')
  }
}

const loadDeliveryOrders = async () => {
  try {
    const res = await api.getDeliveryOrders()
    if (res.data.code === 200) {
      deliveryOrders.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载派送订单失败')
  }
}

const confirmDeliver = async (row) => {
  try {
    await api.confirmDelivery(row.id)
    ElMessage.success('确认送达成功')
    loadDeliveryOrders()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const getDeliverStatus = (status) => {
  const map = { 0: '待发货', 1: '已发货', 2: '已完成' }
  return map[status] || '未知'
}

const getDeliveryStatus = (status) => {
  const map = { 0: '待发货', 1: '已发货', 2: '已完成' }
  return map[status] || '未知'
}

onMounted(() => {
  loadConsumptionOrders()
  loadDeliveryOrders()
})
</script>

<style scoped>
.orders {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}
</style>