import { createI18n } from 'vue-i18n';
import enLocale from 'element-plus/es/locale/lang/en'
import zhLocale from 'element-plus/es/locale/lang/zh-cn'
// import ElementLocal from 'element-plus/es/locale'
import zh from '@/assets/language/lang/zh.js';
import en from '@/assets/language/lang/en.js';
const messages = {
  zh: 
  { ...zh,
    ...zhLocale
  },
  en: 
  {
     ...en,
     ...enLocale  
  },
};

const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: "zh", // 设置默认语言
  messages
});
export default i18n;