import App from './App'
import store from './store'
//import unieapConfig from '@/common/unieapConfig.js'
//unieap begin
import messages from './locale/index'

let i18nConfig = {
  locale: uni.getLocale(),
  messages
}
//unieap end


// #ifndef VUE3
import Vue from 'vue'
import VueI18n from 'vue-i18n'// v8.x
Vue.use(VueI18n)
const i18n = new VueI18n(i18nConfig)

Vue.config.productionTip = false
Vue.prototype.$store = store
Vue.prototype.$adpid = "1111111111"
Vue.prototype.$backgroundAudioData = {
	playing: false,
	playTime: 0,
	formatedPlayTime: '00:00:00'
}
App.mpType = 'app'
const app = new Vue({
	store,
	i18n,
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
import { createI18n } from 'vue-i18n'// v9.x
const i18n = createI18n(i18nConfig)
export function createApp() {
	const app = createSSRApp(App)
	app.use(store)
	app.use(i18n)
	app.config.globalProperties.$adpid = "222222222"
	//unieap begin
	//app.config.globalProperties.$baseUrl = unieapConfig.VUE_APP_BASE_API
	app.config.globalProperties.$baseUrl = "http://39.108.63.198:8806/slipper"
	//#ifdef H5
	app.config.globalProperties.$baseUrl = "https://www.szlongzhiyi.com/slipper"
    //#endif	
	//#ifdef APP-PLUS
	  app.config.globalProperties.$baseUrl = "http://39.108.63.198:8806/slipper"
	  //app.config.globalProperties.$baseUrl = "http://localhost:8806/slipper"
    //#endif	
	//app.config.globalProperties.$baseUrl = "https://8.217.6.50:8806/slipper"
	//app.config.globalProperties.$baseUrl = "https://localhost:8806/slipper"
	app.config.globalProperties.$baseUrl = "http://localhost:8806/slipper"
	//app.config.globalProperties.$baseUrl = "http://39.108.63.198:8806/slipper"
	app.config.globalProperties.$backgroundAudioData = {
		playing: false,
		playTime: 0,
		formatedPlayTime: '00:00:00'
	}
	return {
		app
	}
}
// #endif
