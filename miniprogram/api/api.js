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
const USE_MOCK = true
const baseUrl = 'http://192.168.1.25:8080'

const mockData = {
  products: [
    { id: 1, name: '蜂蜜礼盒', price: 99.00, imageUrl: 'https://example.com/gift1.jpg', description: '优质蜂蜜礼盒装' },
    { id: 2, name: '小米手环', price: 199.00, imageUrl: 'https://example.com/gift2.jpg', description: '小米手环6 NFC版' },
    { id: 3, name: '床上四件套', price: 299.00, imageUrl: 'https://example.com/gift3.jpg', description: '纯棉床上四件套' },
    { id: 4, name: '蓝牙耳机', price: 399.00, imageUrl: 'https://example.com/gift4.jpg', description: '无线蓝牙耳机' },
    { id: 5, name: '保温杯', price: 59.00, imageUrl: 'https://example.com/gift5.jpg', description: '不锈钢保温杯' }
  ],
  userInfo: { id: 1, openid: 'openid001', nickname: '张三', avatarUrl: 'https://example.com/avatar1.png', referralCode: 'REF001' },
  balanceData: { rechargeBalance: '1000.00', rebateBalance: '50.00' },
  membershipStatus: { isActive: true, membershipExpire: '2027-01-01' },
  orders: [
    { id: 1, productName: '会员年费', amount: 299.00, status: '已完成', createTime: '2026-01-01' },
    { id: 2, productName: '会员年费', amount: 299.00, status: '待发货', createTime: '2026-02-15' }
  ],
  addresses: [
    { id: 1, name: '张三', phone: '13800138001', province: '广东省', city: '深圳市', district: '南山区', detailAddress: '科技园路1号', isDefault: 1 }
  ],
  referralStats: {
    code: 'REF001',
    totalCount: 5,
    totalRebate: '100.00',
    pendingAmount: '50.00',
    logs: []
  }
}

function getProducts() {
  return Promise.resolve({ data: mockData.products })
}

function getProduct(id) {
  return Promise.resolve({ data: mockData.products.find(p => p.id == id) })
}

function getProductDetail(id) {
  return Promise.resolve({ data: mockData.products.find(p => p.id == id) })
}

function login(code) {
  return Promise.resolve({ data: { ...mockData.userInfo, isNewUser: false } })
}

function getBalance() {
  return Promise.resolve({ data: mockData.balanceData })
}

function getMembershipStatus() {
  return Promise.resolve({ data: mockData.membershipStatus })
}

function getOrders() {
  return Promise.resolve({ data: mockData.orders })
}

function getAddresses() {
  return Promise.resolve({ data: mockData.addresses })
}

function getGifts() {
  return Promise.resolve({ data: mockData.products })
}

function register(data) {
  return Promise.resolve({ data: { success: true } })
}

function recharge(amount) {
  return Promise.resolve({ data: { success: true } })
}

function setPeriodicGift(giftId) {
  return Promise.resolve({ data: { success: true } })
}

function setDefaultAddress(id) {
  return Promise.resolve({ data: { success: true } })
}

function deleteAddress(id) {
  return Promise.resolve({ data: { success: true } })
}

function createOrder(productId, addressId, quantity) {
  return Promise.resolve({ data: { success: true } })
}

function getReferralCode() {
  return Promise.resolve({ data: { code: mockData.referralStats.code } })
}

function getReferralStats() {
  return Promise.resolve({ data: mockData.referralStats })
}

function request(url, options = {}) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + url,
      ...options,
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data)
        } else {
          reject(res.data)
        }
      },
      fail: (err) => reject(err)
    })
  })
}

const realApi = {
  getProducts: () => request('/api/gift/list'),
  getProduct: (id) => request(`/api/gift/${id}`),
  getProductDetail: (id) => request(`/api/gift/${id}`),
  login: (code) => request('/api/user/login', { method: 'POST', data: { code } }),
  getBalance: () => request('/api/user/balance'),
  getMembershipStatus: () => request('/api/membership/user/1/status'),
  getOrders: () => request('/api/orders'),
  getAddresses: () => request('/api/addresses'),
  getGifts: () => request('/api/gift/list'),
  register: (data) => request('/api/user/register', { method: 'POST', data }),
  recharge: (amount) => request('/api/recharge', { method: 'POST', data: { amount } }),
  setPeriodicGift: (giftId) => request('/api/periodic-gift/set', { method: 'POST', data: { giftId } }),
  setDefaultAddress: (id) => request(`/api/address/${id}/default`, { method: 'POST' }),
  deleteAddress: (id) => request(`/api/address/${id}`, { method: 'DELETE' }),
  createOrder: (productId, addressId, quantity) => request('/api/order/create', { method: 'POST', data: { productId, addressId, quantity } }),
  getReferralCode: () => request('/api/referral/code'),
  getReferralStats: () => request('/api/referral/stats')
}

const mockApi = {
  getProducts,
  getProduct,
  getProductDetail,
  login,
  getBalance,
  getMembershipStatus,
  getOrders,
  getAddresses,
  getGifts,
  register,
  recharge,
  setPeriodicGift,
  setDefaultAddress,
  deleteAddress,
  createOrder,
  getReferralCode,
  getReferralStats
}

module.exports = USE_MOCK ? mockApi : realApi
