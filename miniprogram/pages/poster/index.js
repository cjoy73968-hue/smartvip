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
Page({
  data: {
    posterUrl: '',
    referralCode: '',
    loading: false
  },

  onLoad(options) {
    if (options.referralCode) {
      this.setData({ referralCode: options.referralCode })
    }
    this.generatePoster()
  },

  async generatePoster() {
    this.setData({ loading: true })

    try {
      const code = this.data.referralCode || wx.getStorageSync('userInfo')?.referralCode || 'DEMO'

      const posterInfo = {
        width: 600,
        height: 800,
        backgroundColor: '#ffffff',
        views: [
          {
            type: 'image',
            url: '/images/logo.png',
            x: 200,
            y: 50,
            width: 200,
            height: 200
          },
          {
            type: 'text',
            text: '邀请好友加入会员',
            x: 300,
            y: 300,
            fontSize: 36,
            color: '#333333',
            align: 'center'
          },
          {
            type: 'text',
            text: '我的推荐码',
            x: 300,
            y: 400,
            fontSize: 24,
            color: '#666666',
            align: 'center'
          },
          {
            type: 'text',
            text: code,
            x: 300,
            y: 480,
            fontSize: 48,
            color: '#409eff',
            fontWeight: 'bold',
            align: 'center'
          },
          {
            type: 'text',
            text: '扫码注册立享优惠',
            x: 300,
            y: 600,
            fontSize: 28,
            color: '#999999',
            align: 'center'
          },
          {
            type: 'qrcode',
            text: `/pages/register/index?referralCode=${code}`,
            x: 200,
            y: 650,
            width: 200,
            height: 200
          }
        ]
      }

      const res = await wx.showShareMenu({
        withShareTicket: true
      })

      this.setData({ posterUrl: '/images/poster-placeholder.png', loading: false })
      wx.showToast({ title: '海报已生成', icon: 'success' })

    } catch (err) {
      console.error('Failed to generate poster:', err)
      wx.showToast({ title: '生成失败', icon: 'none' })
      this.setData({ loading: false })
    }
  },

  onSavePoster() {
    if (!this.data.posterUrl) {
      wx.showToast({ title: '海报未生成', icon: 'none' })
      return
    }

    wx.saveImageToPhotosAlbum({
      filePath: this.data.posterUrl,
      success: () => {
        wx.showToast({ title: '保存成功', icon: 'success' })
      },
      fail: (err) => {
        if (err.errMsg.includes('auth deny')) {
          wx.showModal({
            title: '提示',
            content: '需要授权保存图片',
            confirmText: '去授权',
            success: (res) => {
              if (res.confirm) {
                wx.openSetting()
              }
            }
          })
        } else {
          wx.showToast({ title: '保存失败', icon: 'none' })
        }
      }
    })
  },

  onShareAppMessage() {
    return {
      title: '邀请好友加入会员',
      path: `/pages/register/index?referralCode=${this.data.referralCode}`
    }
  }
})