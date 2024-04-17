<!-- eslint-disable no-use-before-define -->
<!-- eslint-disable no-empty-function -->
<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true">
        <el-form-item>
          <el-input
            v-model="form.sendMessage"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.receiveMessage"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="sendMessageHandle()">发送消息</el-button>
          <el-button v-repeat @click="sendEmailHandle()">发送Email</el-button>
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
import { clearJson, generateUUID } from '@/utils'
// 按需导入 io 方法：调用 io('url') 方法，即可创建 websocket 连接的实例
import ws from '@/unieap/utils/websocket'

import { setStatusApi } from '@/api/demo'

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

    /**
     * 参数处理
     */
     const paramsHandle = (type = 1, messageType = 1, url = '') => {
      const params = {
        message: {
          ack: generateUUID(), // ack 消息确认
          from: 'chai', // 当前用户ID
          to: 'bobo', // 接收者ID
          type: type, // 消息类型 1-私聊 2-群聊
          messageType: messageType, // 发送消息类型 1-text 2-image 3-file
          content: messageType === 1 ? data.content : '', // 消息内容 messageType = 1
          url: url // 资源路径 messageType = 2 | 3
        }
      }
      return params
    }

    /**
     * 发送
     */
    const sendMessageHandle = () => {
      store.dispatch('unieap/websocket/send', paramsHandle(1, 1))
    }

    /**
     * 发送
     */
     const sendEmailHandle = () => {
      setStatusApi('').then(r => {
        if (r) {
          ElMessage({
            message: '操作成功!',
            type: 'success'
          })
        } 
      })
    }

    /**
     * 图片上传成功事件
     */
    const uploadSuccessHandle = (res, _file) => {
      if (res.code === 'success') {
        store.dispatch('websocket/send', paramsHandle(1, 2, res.data.url))
      } else {
        ElMessage({
          message: res.message,
          type: 'warning'
        })
      }
    }

    onMounted(() => {
      window.addEventListener('receive', getServerInfo)
    })

    const getServerInfo = (res) => {
      data.form.receiveMessage = res
    }

    // eslint-disable-next-line no-empty-function
    onBeforeMount(() => {
      // 初始化websocket
      store.dispatch('unieap/websocket/init') // add by Chai
    })

    return {
      refForm,
      ...toRefs(data),
      clearJson,
      sendMessageHandle,
      sendEmailHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
