<template>
  <div>
    <span>WebSocket状态：</span>
    <el-switch :disabled="true" v-model="status" class="ml-2" />
  </div>
  <el-button type="success" @click="connect">连接</el-button>
  <el-button type="danger" @click="closeConnect">断开连接</el-button>
  <el-input class="msg" v-model="content" placeholder="请输入消息" />
  <el-button type="primary"
             :disabled="!content"
             @click="send">发送消息</el-button>
  <div v-for="(item, index) in list" :key="index">
    <div class="time">{{ item.role }} {{ item.time }}</div>
    <div class="info" v-html="item.con" />
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import dayjs from 'dayjs'
import socket from '@/unieap/utils/socket'

const content = ref('')
const status = ref(false)
const list = reactive([])

onMounted(() => {
  window.addEventListener('receive', getServerInfo)
  window.addEventListener('connectStatus', getConnectStatus)
})

const connect = () => {
  socket.connectWebsocket()
}
const closeConnect = () => {
  if (socket.isConnection()) socket.closeWebSocket()
}
const send = () => {
  socket.webSocketSend(content.value)
  if (socket.isConnection()) {
    list.push({
      role: '你发送的信息',
      time: dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss'),
      con: content.value
    })
    content.value = ''
  }
}
const getServerInfo = (res) => {
  list.push({
    role: '服务器回应',
    time: dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss'),
    con: res.detail
  })
}
const getConnectStatus = (res) => {
  status.value = res.detail
}
onBeforeUnmount(() => {
  closeConnect()
  if (socket.isConnection()) {
    window.removeEventListener('receive')
    window.removeEventListener('connectStatus')
  }
})
</script>
<style lang="scss" scoped>
.msg {
  display: block;
  margin: 20px 0;
}
.time {
  font-size: 12px;
  color: var(--el-color-success);
}
.info {
  font-size: 12px;
  color: var(--el-color-primary);
}
</style>
