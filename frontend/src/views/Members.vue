<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="members">
    <h2>会员管理</h2>
    <el-card>
      <el-form inline>
        <el-form-item label="搜索">
          <el-input v-model="searchKeyword" placeholder="昵称/手机号/推荐码" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchStatus" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="冻结" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="members" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="referralCode" label="推荐码" />
        <el-table-column prop="rechargeBalance" label="充值余额" />
        <el-table-column prop="rebateBalance" label="返利余额" />
        <el-table-column label="会员状态">
          <template #default="{ row }">
            <el-tag :type="isMembershipActive(row) ? 'success' : 'info'">
              {{ isMembershipActive(row) ? '有效' : '已到期' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="账户状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '冻结' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" :type="row.status === 1 ? 'danger' : 'success'" @click="toggleFreeze(row)">
              {{ row.status === 1 ? '冻结' : '解冻' }}
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

const members = ref([])
const searchKeyword = ref('')
const searchStatus = ref(null)

const loadMembers = async () => {
  try {
    const res = await api.getMembers({ keyword: searchKeyword.value, status: searchStatus.value })
    if (res.data.code === 200) {
      members.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载会员列表失败')
  }
}

const handleSearch = () => {
  loadMembers()
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能开发中')
}

const toggleFreeze = async (row) => {
  try {
    if (row.status === 1) {
      await api.freezeMember(row.id)
    } else {
      await api.unfreezeMember(row.id)
    }
    ElMessage.success('操作成功')
    loadMembers()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const isMembershipActive = (row) => {
  if (!row.membershipExpire) return false
  return new Date(row.membershipExpire) > new Date()
}

onMounted(() => {
  loadMembers()
})
</script>

<style scoped>
.members {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}
</style>