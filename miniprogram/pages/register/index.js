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
    avatarUrl: '/images/default-avatar.png',
    nickname: '',
    phone: '',
    address: '',
    referralCode: ''
  },

  onLoad(options) {
    if (options.referralCode) {
      this.setData({ referralCode: options.referralCode })
    }
  },

  onChooseAvatar(e) {
    this.setData({ avatarUrl: e.detail.avatarUrl })
  },

  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value })
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value })
  },

  onAddressInput(e) {
    this.setData({ address: e.detail.value })
  },

  async onRegister() {
    if (!this.data.nickname) {
      wx.showToast({ title: '请输入昵称', icon: 'none' })
      return
    }
    if (!this.data.phone) {
      wx.showToast({ title: '请输入手机号', icon: 'none' })
      return
    }

    wx.showLoading({ title: '注册中...' })
    try {
      await api.register({
        nickname: this.data.nickname,
        phone: this.data.phone,
        avatarUrl: this.data.avatarUrl,
        address: this.data.address,
        referralCode: this.data.referralCode
      })
      wx.showToast({ title: '注册成功' })
      wx.switchTab({ url: '/pages/personal/index' })
    } catch (err) {
      wx.showToast({ title: '注册失败', icon: 'none' })
    } finally {
      wx.hideLoading()
    }
  }
})