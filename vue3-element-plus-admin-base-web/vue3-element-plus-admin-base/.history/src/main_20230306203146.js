import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementPlus from 'element-plus' //add by chai
import * as ElIcon from '@element-plus/icons-vue'//add by chai
//import i18n from './assets/language/index'//add by chai
import { createI18n } from 'vue-i18n/index'; //add by chai


import '@/assets/sass/index.scss' // 全局样式
import components from '@/components/global/index' // 全局自定义组件
import Directive from '@/directive' // 自定义指令


const app = createApp(App)

// add by chai
Object.keys(ElIcon).forEach((key) => {
  app.component(key, ElIcon[key])
})


app.use(router)
  .use(store)
  .use(createI18n) //add by chai
  .use(ElementPlus)//add by chai
  .use(components)
  .use(Directive)
  .mount('#app')
