<template>
  <el-config-provider :locale="locale" :size="size" :z-index="zIndex">
    <View v-if="!reload" />
  </el-config-provider>
</template>

<script>
import { computed, defineComponent } from 'vue'
import { useStore } from 'vuex'

import View from '@/components/view/index.vue'

import zhCn from 'element-plus/lib/locale/lang/zh-cn'
//import enUs from 'element-plus/lib/locale/lang/en-us'

export default defineComponent({
  components: { View },
  sockets: {
    // 客户端connect事件，服务端可针对connect进行信息传输
    connect: function (data) {
      this.$message.info('成功')
    },
    // 链接状态
    connected: function (data) {
      this.$message.info(data + '成功')
    },
    // 接收服务器推送消息
    push_data_event: function (data) {
      console.log('服务器广播消息', data)
    }
  },
  setup() {
    const store = useStore()

    store.dispatch('theme/getTheme')

    store.dispatch('settings/getLayout')

    const reload = computed({
      get: () => {
        return store.state.theme.reload
      },
      set: (val) => {
        store.dispatch('theme/setReload', val)
      }
    })
    return {
      locale: zhCn,
      size: 'default',
      zIndex: 3000,
      reload
    }
  }
})
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
  width: 100%;
  display: flex;
  color: var(--el-text-color-primary);
}
</style>
