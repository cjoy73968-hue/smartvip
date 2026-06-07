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
    referralCode: '',
    referralCount: 0,
    totalRebate: '0.00',
    pendingRebate: '0.00',
    rebateLogs: []
  },

  onLoad() {
    this.loadReferralData()
  },

  async loadReferralData() {
    try {
      const [codeRes, statsRes] = await Promise.all([
        api.getReferralCode(),
        api.getReferralStats()
      ])

      this.setData({
        referralCode: codeRes.data.code,
        referralCount: statsRes.data.totalCount || 0,
        totalRebate: statsRes.data.totalRebate || '0.00',
        pendingRebate: statsRes.data.pendingAmount || '0.00',
        rebateLogs: statsRes.data.logs || []
      })
    } catch (err) {
      console.error(err)
    }
  },

  onShareAppMessage() {
    return {
      title: '邀请好友加入会员',
      path: `/pages/register/index?referralCode=${this.data.referralCode}`
    }
  },

  onShareTimeline() {
    return {
      title: '邀请好友加入会员',
      path: `/pages/register/index?referralCode=${this.data.referralCode}`
    }
  },

  async onGeneratePoster() {
    wx.showToast({ title: '海报生成功能开发中', icon: 'none' })
  }
})