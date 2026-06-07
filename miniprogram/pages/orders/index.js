/*
 * Copyright (C) 2024 会员管理系统项目
 *
 * 本项目采用 AGPL-3.0 开源协议开源。
 *
 * 您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
 * 也必须按照 AGPL-3.0 协议开源您的修改版本。
 *
 * 完整协议文本请参见 LICENSE 文件。
 */
const api = require('../../api/api.js')

Page({
  data: {
    orders: [],
    activeTab: 'all'
  },

  onLoad() {
    this.loadOrders()
  },

  onShow() {
    this.loadOrders()
  },

  async loadOrders() {
    try {
      const res = await api.getOrders()
      this.setData({ orders: res.data || [] })
    } catch (err) {
      console.error(err)
    }
  },

  onTabChange(e) {
    this.setData({ activeTab: e.detail.value })
    this.loadOrders()
  },

  getStatusText(status) {
    const map = { 0: '待发货', 1: '已发货', 2: '已完成' }
    return map[status] || '未知'
  },

  getStatusType(status) {
    const map = { 0: 'warning', 1: 'primary', 2: 'success' }
    return map[status] || 'default'
  }
})