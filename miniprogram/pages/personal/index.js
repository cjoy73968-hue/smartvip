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
    hasUserInfo: true,
    userInfo: { nickname: '张三', avatarUrl: 'https://example.com/avatar1.png' },
    membershipStatus: '有效',
    balances: { recharge: '1000.00', rebate: '50.00' },
    referralCode: 'REF001',
    expireTime: '2027-01-01'
  },

  onLoad() {
    this.loadUserData()
  },

  onShow() {
    this.loadUserData()
  },

  async loadUserData() {
    try {
      const [balanceRes, membershipRes] = await Promise.all([
        api.getBalance(),
        api.getMembershipStatus()
      ])

      this.setData({
        hasUserInfo: true,
        balances: {
          recharge: balanceRes.data?.rechargeBalance || '0.00',
          rebate: balanceRes.data?.rebateBalance || '0.00'
        },
        membershipStatus: membershipRes.data?.isActive ? '有效' : '已到期',
        expireTime: membershipRes.data?.membershipExpire || ''
      })
    } catch (err) {
      console.error(err)
    }
  },

  onShareAppMessage() {
    const referralCode = this.data.referralCode
    return {
      title: '邀请好友加入会员',
      path: `/pages/register/index?referralCode=${referralCode}`
    }
  },

  goToRecharge() { wx.navigateTo({ url: '/pages/recharge/index' }) },
  goToMembership() { wx.navigateTo({ url: '/pages/membership/index' }) },
  goToProducts() { wx.navigateTo({ url: '/pages/products/index' }) },
  goToOrders() { wx.navigateTo({ url: '/pages/orders/index' }) },
  goToAddresses() { wx.navigateTo({ url: '/pages/addresses/index' }) },
  goToReferral() { wx.navigateTo({ url: '/pages/referral/index' }) }
})
