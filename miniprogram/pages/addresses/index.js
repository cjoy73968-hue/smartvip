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
    addresses: [],
    isSelectMode: false
  },

  onLoad(options) {
    if (options.select === 'true') {
      this.setData({ isSelectMode: true })
    }
    this.loadAddresses()
  },

  async loadAddresses() {
    try {
      const res = await api.getAddresses()
      this.setData({ addresses: res.data || [] })
    } catch (err) {
      console.error(err)
    }
  },

  onAddressSelect(e) {
    const addressId = e.currentTarget.dataset.id
    if (this.data.isSelectMode) {
      const selectedAddress = this.data.addresses.find(a => a.id === addressId)
      wx.setStorageSync('selectedAddress', selectedAddress)
      wx.navigateBack()
    }
  },

  async onSetDefault(e) {
    const addressId = e.currentTarget.dataset.id
    try {
      await api.setDefaultAddress(addressId)
      wx.showToast({ title: '设置成功' })
      this.loadAddresses()
    } catch (err) {
      wx.showToast({ title: '设置失败', icon: 'none' })
    }
  },

  async onDelete(e) {
    const addressId = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认删除',
      content: '确定删除该地址吗？',
      success: async res => {
        if (res.confirm) {
          try {
            await api.deleteAddress(addressId)
            wx.showToast({ title: '删除成功' })
            this.loadAddresses()
          } catch (err) {
            wx.showToast({ title: '删除失败', icon: 'none' })
          }
        }
      }
    })
  },

  onAddAddress() {
    wx.navigateTo({ url: '/pages/address-edit/index' })
  }
})