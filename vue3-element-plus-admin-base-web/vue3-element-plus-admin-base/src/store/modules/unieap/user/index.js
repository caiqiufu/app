/*
 * @Description:
 * @Author: gumingchen
 * @Email: 1240235512@qq.com
 * @Date: 2021-04-02 18:59:43
 * @LastEditors: gumingchen
 * @LastEditTime: 2021-08-31 20:03:34
 */
import { clearJson } from '@/utils/index'
import { loginApi,logoutApi } from '@/api/unieap/login'
import { getToken, setToken, clearToken } from '@/utils/storage'

export default {
  state: {
    user: {
      id: '',
      username: '',
      nickname: '',
      avatar: '',
      sex: '',
      mobile: ''
    },
    token: {
      user_id: '',
      token: getToken(),
      expired_at: '',
      updated_at: ''
    }
  },
  getters: {
    tokenVal: state => {
      return state.token.token
    }
  },
  mutations: {
    SET_USER: (state, user) => {
      state.user = user
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    CLEAR_USER: state => {
      clearJson(state.user)
    },
    CLEAR_TOKEN: state => {
      clearJson(state.token)
    }
  },
  actions: {

    /**
     * @description: 登录
     * @param {*} commit
     * @param {*} params
     * @return {*}
     * @author: gumingchen
     */
    async login({ commit }, params) {
      const r = await loginApi(params)
      if (r) {
        setToken(r.data.token)
        commit('SET_TOKEN', r.data)
      }
      return r
    },

    /**
     * @description: 退出登录
     * @param {*} _store
     * @param {*} params
     * @return {*}
     * @author: gumingchen
     */
    async logout(_store) {
      const r = await logoutApi()
      return r
    },

    /**
     * @description: 清除用户信息 凭证
     * @param {*} commit
     * @return {*}
     * @author: gumingchen
     */
    clear({ commit }) {
      commit('CLEAR_USER')
      commit('CLEAR_TOKEN')
      clearToken()
    }
  }
}
