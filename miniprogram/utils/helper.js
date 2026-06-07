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
const RECHARGE_OPTIONS = [
  { label: '充值100元', value: 100 },
  { label: '充值200元', value: 200 },
  { label: '充值500元', value: 500 },
  { label: '充值1000元', value: 1000 }
]

const DELIVERY_CYCLE_MAP = {
  daily: '每日',
  weekly: '每周',
  monthly: '每月',
  quarterly: '每季度'
}

function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

function formatDateTime(date) {
  if (!date) return ''
  const d = new Date(date)
  return `${formatDate(d)} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

function formatMoney(amount) {
  if (!amount) return '0.00'
  return parseFloat(amount).toFixed(2)
}

function isMembershipActive(expireTime) {
  if (!expireTime) return false
  return new Date(expireTime) > new Date()
}

function getDaysUntilExpire(expireTime) {
  if (!expireTime) return 0
  const now = new Date()
  const expire = new Date(expireTime)
  const diff = expire - now
  return Math.ceil(diff / (1000 * 60 * 60 * 24))
}

module.exports = {
  RECHARGE_OPTIONS,
  DELIVERY_CYCLE_MAP,
  formatDate,
  formatDateTime,
  formatMoney,
  isMembershipActive,
  getDaysUntilExpire
}