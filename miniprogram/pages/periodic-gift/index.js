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
    selectedGiftId: null,
    gifts: []
  },

  onLoad() {
    this.loadGifts()
  },

  async loadGifts() {
    try {
      const res = await api.getProducts()
      this.setData({ gifts: res.data || [] })
    } catch (err) {
      console.error(err)
    }
  },

  onGiftSelect(e) {
    const giftId = e.currentTarget.dataset.id
    this.setData({ selectedGiftId: giftId })
  },

  async onConfirm() {
    if (!this.data.selectedGiftId) {
      wx.showToast({ title: '请选择礼品', icon: 'none' })
      return
    }

    wx.showLoading({ title: '保存中...' })
    try {
      await api.setPeriodicGift(this.data.selectedGiftId)
      wx.showToast({ title: '保存成功' })
      wx.navigateBack()
    } catch (err) {
      wx.showToast({ title: '保存失败', icon: 'none' })
    } finally {
      wx.hideLoading()
    }
  }
})