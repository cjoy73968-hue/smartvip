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
    address: null,
    amount: 200,
    showAddressPicker: false
  },

  onLoad() {
    this.loadDefaultAddress()
  },

  async loadDefaultAddress() {
    try {
      const res = await api.getAddresses()
      const defaultAddr = res.data.find(a => a.isDefault) || res.data[0]
      this.setData({ address: defaultAddr })
    } catch (err) {
      console.error(err)
    }
  },

  onSelectAddress() {
    wx.navigateTo({ url: '/pages/addresses/index?select=true' })
  },

  onRechargeOptionTap(e) {
    const amount = e.currentTarget.dataset.amount
    this.setData({ amount })
  },

  async onRecharge() {
    if (!this.data.address) {
      wx.showToast({ title: '请选择收货地址', icon: 'none' })
      return
    }

    wx.showLoading({ title: '创建充值...' })
    try {
      const res = await api.recharge(this.data.amount)
      if (res.data.payUrl) {
        wx.navigateTo({ url: res.data.payUrl })
      } else {
        wx.showToast({ title: '充值成功' })
      }
    } catch (err) {
      wx.showToast({ title: '充值失败', icon: 'none' })
    } finally {
      wx.hideLoading()
    }
  }
})