/**
 * state是用来存放全局共享的值的
 * 想要更改state中的这些值就要用到mutations
 * 相同点：mutations和action都是用来改变Vuex store的状态的
 * 不同点：mutations提供的回调函数是同步的,而actions提供的方法是异步的，此外，actions的方法最终还是通过调用mutations的方法来实现修改vuex的状态的
 */

/*
 * @Description:
 * @Author: Chai
 * @Email: caiqiufu@sohu.com
 * @Date: 2023-02-26 08:44:56
 * @LastEditors: Chai
 * @LastEditTime: 2023-02-26 08:44:56
 */
import WebsocketClass from '@/unieap/utils/websocket'
import { getToken } from '@/utils/storage'

export default {
  state: {
    response: null, // add by Chai 服务端发送的消息
    socket: null
  },
  getters: {
  },
  mutations: {
    INIT: (state, token) => {
      const url = process.env.VUE_APP_WS_URL + token
      state.socket = new WebsocketClass(url, data => {
        state.response = data
        console.log('🚲~~:', data)
      })
      state.socket.connect()
      console.log('--------------------------------------')
      console.log('websocket init completed')
    },
    SEND: (state, data) => {
      const params = {
        type: 1,
        requestBody: data
      }
      console.log('✈️:~~', params)
      state.socket.send(params)
    },
    CLOSE: (state) => {
      if (state.socket) {
        state.socket.close()
      }
      state.response = null
      state.body = null
      state.socket = null
    }
  },
  actions: {

    /**
     * @description: 初始化websocket
     * @param {*} commit
     * @return {*}
     * @author: gumingchen
     */
    init({ commit }) {
      commit('INIT', getToken)
    },

    /**
     * @description: 发送信息
     * @param {*} commit
     * @param {*} data
     * @return {*}
     * @author: gumingchen
     */
    send({ commit, dispatch }, data) {
      commit('SEND', data)
      const now = new Date()
      dispatch('message/pushPrivateMessage', {
        ack: data.message.ack,
        content: data.message.content,
        created_at: now.getTime(),
        from: data.message.from,
        id: '',
        status: 0,
        to: data.message.to,
        type: data.message.messageType,
        url: data.message.url,
        loading: true
      }, { root: true })
    },

    /**
     * @description: 手动断开websocket
     * @param {*} commit
     * @return {*}
     * @author: gumingchen
     */
    close({ commit }) {
      commit('CLOSE')
    }
  }
}
