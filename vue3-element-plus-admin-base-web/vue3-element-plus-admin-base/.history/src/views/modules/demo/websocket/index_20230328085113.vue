<!-- eslint-disable no-use-before-define -->
<!-- eslint-disable no-empty-function -->
<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true">
        <el-form-item>
          <el-input
            v-model="form.sendMessage"
            :placeholder="$t('form.sendMessage')"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.receiveMessage"
            :placeholder="$t('form.receiveMessage')"
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
  onMounted,
  onBeforeMount,
  reactive,
  ref,
  toRefs,
  watch,
  computed, h
} from 'vue'
import { ElNotification, ElAvatar, ElMessage } from 'element-plus'
import { Howl } from 'howler'
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
    const response = computed(() => {
      return store.state.websocket.response
    })
    const urls = [
      require('@/assets/mp3/dingdong.mp3'),
      require('@/assets/mp3/keke.mp3'),
      require('@/assets/mp3/pig.mp3'),
      require('@/assets/mp3/xiuxiu.mp3')
    ]
    watch(() => response, async (newVal, _oldVal) => {
      if (newVal.value) {
        let title = ''
        let tips = ''
        let sound
        switch (newVal.value.type) {
          case 0: // 心跳
            break
          case 1: // 聊天消息
            sound = new Howl({
              src: [urls[0]]
            })
            break
          case 2: // ack 确认消息
            break
          case 3: // 异常消息
            ElMessage({
              message: newVal.value.message,
              type: 'warning'
            })
            break
          case 4: // 好友申请
            sound = new Howl({
              src: [urls[1]]
            })
            break
          case 5: // 好友同意
            sound = new Howl({
              src: [urls[2]]
            })
            break
          case 6: // 好友拒绝
            sound = new Howl({
              src: [urls[2]]
            })
            break
          case 7: // 好友删除
            break
        }
        if ([4, 5, 6].includes(newVal.value.type)) {
          store.dispatch('friend/getApplys')
          const avatar = newVal.value.responseBody.user.avatar || ''
          const nickname = newVal.value.responseBody.user.nickname || ''
          const username = newVal.value.responseBody.user.username || ''
          ElNotification({
            title: title,
            dangerouslyUseHTMLString: true,
            message: h(() => {
              return (
                <div class="width-full">
                  <div style="display: flex">
                    <ElAvatar size={54} src={avatar} />
                    <div class="margin_l-10" style="flex: 1;">
                      <p>{nickname}</p>
                      <p>{username}</p>
                    </div>
                  </div>
                  <p>{tips}</p>
                </div>
              )
            }),
            showClose: false,
            duration: 4000
          })
        }
        if (sound) {
          sound.play()
        }
      }
    }, { deep: true })

    onMounted(() => {
      window.addEventListener('receive', getServerInfo)
    })

    const getServerInfo = (res) => {
      data.form.receiveMessage = res
    }

    const sendMessageHandle = () => {

    };

    // eslint-disable-next-line no-empty-function
    onBeforeMount(() => {
      // 初始化websocket
      store.dispatch('unieap/websocket/init') // add by Chai
    })

    return {
      refForm,
      ...toRefs(data),
      clearJson,
      sendMessageHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
