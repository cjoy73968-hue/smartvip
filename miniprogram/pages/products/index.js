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
    products: [],
    categories: [],
    currentCategory: null
  },

  onLoad() {
    this.loadProducts()
  },

  async loadProducts() {
    try {
      const res = await api.getProducts()
      this.setData({ products: res.data || [] })
    } catch (err) {
      console.error(err)
    }
  },

  onProductTap(e) {
    const productId = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/product-detail/index?id=${productId}` })
  }
})