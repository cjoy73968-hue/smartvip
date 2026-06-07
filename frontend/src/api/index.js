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
import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('merchant_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default {
  login(username, password) {
    return api.post('/merchant/auth/login', null, { params: { username, password } })
  },
  getMembers(params) {
    return api.get('/merchant/members/list', { params })
  },
  getMemberDetail(id) {
    return api.get(`/merchant/members/${id}`)
  },
  updateMember(id, data) {
    return api.put(`/merchant/members/${id}`, data)
  },
  freezeMember(id) {
    return api.put(`/merchant/members/${id}/freeze`)
  },
  unfreezeMember(id) {
    return api.put(`/merchant/members/${id}/unfreeze`)
  },
  getOrders(params) {
    return api.get('/merchant/orders/consumption/list', { params })
  },
  getDeliveryOrders(params) {
    return api.get('/merchant/orders/delivery/list', { params })
  },
  confirmDelivery(id) {
    return api.put(`/merchant/orders/delivery/${id}/deliver`)
  },
  getOrderStatistics() {
    return api.get('/merchant/orders/statistics')
  },
  getGifts(params) {
    return api.get('/gift/list', { params })
  },
  createGift(data) {
    return api.post('/gift', data)
  },
  updateGift(data) {
    return api.put(`/gift/${data.id}`, data)
  },
  deleteGift(id) {
    return api.delete(`/gift/${id}`)
  },
  getConfig() {
    return api.get('/merchant/config')
  },
  updateConfig(data) {
    return api.put('/merchant/config', data)
  },
  updateRebateRate(rate) {
    return api.put('/merchant/config/rebate-rate', null, { params: { rate } })
  },
  updateMembershipPrice(price) {
    return api.put('/merchant/config/membership-price', null, { params: { price } })
  },
  getWithdrawals(params) {
    return api.get('/withdrawal/pending', { params })
  },
  confirmWithdrawal(id) {
    return api.put(`/withdrawal/${id}/confirm`)
  },
  completeWithdrawal(id) {
    return api.put(`/withdrawal/${id}/complete`)
  }
}