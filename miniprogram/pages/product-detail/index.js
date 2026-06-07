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
    product: null,
    address: null,
    quantity: 1
  },

  onLoad(options) {
    this.loadProduct(options.id)
    this.loadDefaultAddress()
  },

  async loadProduct(id) {
    try {
      const res = await api.getProduct(id)
      this.setData({ product: res.data })
    } catch (err) {
      console.error(err)
    }
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

  onQuantityChange(e) {
    this.setData({ quantity: parseInt(e.detail.value) })
  },

  async onPurchase() {
    if (!this.data.address) {
      wx.showToast({ title: '请选择收货地址', icon: 'none' })
      return
    }

    wx.showLoading({ title: '创建订单...' })
    try {
      const res = await api.createOrder(this.data.product.id, this.data.address.id, this.data.quantity)
      wx.showToast({ title: '购买成功' })
      wx.navigateBack()
    } catch (err) {
      wx.showToast({ title: err.message || '购买失败', icon: 'none' })
    } finally {
      wx.hideLoading()
    }
  }
})