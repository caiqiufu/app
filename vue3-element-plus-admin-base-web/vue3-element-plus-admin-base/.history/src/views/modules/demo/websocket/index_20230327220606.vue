<!-- eslint-disable no-empty-function -->
<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true">
        <el-form-item>
          <el-input
            v-model="form.sendMessage"
            :placeholder="$t('demo.sendMessage')"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.receiveMessage"
            :placeholder="$t('demo.receiveMessage')"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="sendMessageHandle()">发送消息</el-button>
          <el-button v-repeat @click="reacquireHandle()">{{
            $t("demo.search")
          }}</el-button>
          <el-button v-repeat @click="clearJson(form)">{{
            $t("demo.reset")
          }}</el-button>
        </el-form-item>
      </el-form>
    </template>
  </Container>
</template>

<script>
import {
  defineComponent,
  nextTick,
  onBeforeMount,
  reactive,
  ref,
  toRefs,
  watch,
  computed
} from 'vue'
import { Howl } from 'howler'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from 'vuex'
import { clearJson, havePermission, parseDate2Str } from '@/utils'
// 按需导入 io 方法：调用 io('url') 方法，即可创建 websocket 连接的实例
import ws from '@/unieap/utils/websocket'

export default defineComponent({
  components: {},
  setup() {
    const refForm = ref()
    const store = useStore()
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sendMessage: '',
        receiveMessage: ''
      },
      list: [],
      selection: []
    })
    const urls = [
      require('@/assets/mp3/dingdong.mp3'),
      require('@/assets/mp3/keke.mp3'),
      require('@/assets/mp3/pig.mp3'),
      require('@/assets/mp3/xiuxiu.mp3')
    ]
    const response = computed(() => {
      return store.state.websocket.response
    })

    // eslint-disable-next-line no-empty-function
    onBeforeMount(() => {
      // 初始化websocket
      store.dispatch('unieap/websocket/init') // add by Chai
    })

    return {
      refForm,
      ...toRefs(data),
      clearJson
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
