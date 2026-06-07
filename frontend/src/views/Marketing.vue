<template>
<!--
  Copyright (C) 2024 会员管理系统项目

  本项目采用 AGPL-3.0 开源协议开源。

  您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
  也必须按照 AGPL-3.0 协议开源您的修改版本。

  完整协议文本请参见 LICENSE 文件。
-->
  <div class="marketing">
    <h2>营销推送</h2>
    <el-card>
      <el-form label-width="120px">
        <el-form-item label="推送类型">
          <el-select v-model="form.type" style="width: 200px">
            <el-option label="优惠活动" value="promotion" />
            <el-option label="新品通知" value="new_product" />
            <el-option label="福利活动" value="benefit" />
            <el-option label="续费提醒" value="renewal" />
          </el-select>
        </el-form-item>
        <el-form-item label="推送范围">
          <el-radio-group v-model="form.scope">
            <el-radio label="all">全部会员</el-radio>
            <el-radio label="active">有效会员</el-radio>
            <el-radio label="expired">已到期会员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="消息标题">
          <el-input v-model="form.title" style="width: 400px" />
        </el-form-item>
        <el-form-item label="消息内容">
          <el-input v-model="form.content" type="textarea" style="width: 400px" rows="5" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSend">发送</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const form = ref({
  type: 'promotion',
  scope: 'all',
  title: '',
  content: ''
})

const handleSend = () => {
  if (!form.value.title || !form.value.content) {
    ElMessage.warning('请填写标题和内容')
    return
  }
  ElMessage.success('发送功能开发中')
}
</script>

<style scoped>
.marketing {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}
</style>