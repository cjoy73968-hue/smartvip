<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="products">
    <h2>商品管理</h2>
    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="handleCreate">新增商品</el-button>
      </div>

      <el-table :data="products" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="price" label="价格" />
        <el-table-column prop="stock" label="库存" />
        <el-table-column label="默认礼品">
          <template #default="{ row }">
            <el-tag :type="row.isDefaultGift === 1 ? 'success' : 'info'">
              {{ row.isDefaultGift === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag :type="row.isShelf === 1 ? 'success' : 'warning'">
              {{ row.isShelf === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../api'

const products = ref([])

const loadProducts = async () => {
  try {
    const res = await api.getGifts()
    if (res.data.code === 200) {
      products.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载商品列表失败')
  }
}

const handleCreate = () => {
  ElMessage.info('新增商品功能开发中')
}

const handleEdit = (row) => {
  ElMessage.info('编辑商品功能开发中')
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该商品?', '提示')
    await api.deleteGift(row.id)
    ElMessage.success('删除成功')
    loadProducts()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadProducts()
})
</script>

<style scoped>
.products {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 15px;
}
</style>