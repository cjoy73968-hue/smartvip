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
const api = {
  baseUrl: 'http://localhost:8080/api',

  async request(url, options = {}) {
    const token = wx.getStorageSync('token')
    const header = options.header || {}
    if (token) {
      header['Authorization'] = `Bearer ${token}`
    }

    return new Promise((resolve, reject) => {
      wx.request({
        url: this.baseUrl + url,
        ...options,
        header,
        success: res => {
          if (res.statusCode === 200 && res.data.code === 200) {
            resolve(res.data)
          } else {
            reject(res.data)
          }
        },
        fail: err => reject(err)
      })
    })
  },

  login(code) {
    return this.request('/user/login', { method: 'POST', data: { code } })
  },

  bindPhone(phone, code) {
    return this.request('/user/phone/bind', { method: 'POST', data: { phone, code } })
  },

  getUserInfo() {
    return this.request('/user/info')
  },

  getBalance() {
    return this.request('/user/balance')
  },

  getMembershipStatus() {
    return this.request('/membership/status')
  },

  purchaseMembership() {
    return this.request('/membership/purchase', { method: 'POST' })
  },

  toggleAutoSubscribe(enable) {
    return this.request('/membership/auto-subscribe', { method: 'PUT', data: { enable } })
  },

  recharge(amount) {
    return this.request('/recharge/create', { method: 'POST', data: { amount } })
  },

  getRechargeLogs() {
    return this.request('/recharge/history')
  },

  getProducts() {
    return this.request('/gift/list')
  },

  createOrder(giftId, addressId) {
    return this.request('/order/create', { method: 'POST', data: { giftId, addressId } })
  },

  getOrders() {
    return this.request('/order/list')
  },

  getAddresses() {
    return this.request('/address/list')
  },

  createAddress(data) {
    return this.request('/address/create', { method: 'POST', data })
  },

  setDefaultAddress(id) {
    return this.request(`/address/set-default/${id}`, { method: 'PUT' })
  },

  deleteAddress(id) {
    return this.request(`/address/delete/${id}`, { method: 'DELETE' })
  },

  getPeriodicGift() {
    return this.request('/user/periodic-gift')
  },

  setPeriodicGift(giftId) {
    return this.request('/user/periodic-gift', { method: 'PUT', data: { giftId } })
  },

  getReferralCode() {
    return this.request('/user/referral-code')
  },

  getReferralStats() {
    return this.request('/rebate/stats')
  },

  createWithdrawRequest(amount) {
    return this.request('/withdrawal/create', { method: 'POST', data: { amount } })
  }
}

module.exports = api