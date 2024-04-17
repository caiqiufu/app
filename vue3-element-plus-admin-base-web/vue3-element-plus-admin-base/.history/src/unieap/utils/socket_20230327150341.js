import { ElMessage } from 'element-plus'

let URL = 'ws://82.157.123.54:9010/ajaxchattest'
let timer = null
let ws = null
let isConnect = false
let checkHeart = 'check-heart'
let count = 0

// 心跳检测
let heart = {
    timer: null,
    timeout: 20000, // 每20s进行一次心跳检测
    start() {
        this.timer = setTimeout(() => {
            if (isConnect) ws.send(JSON.stringify(checkHeart))
        }, this.timeout)
    },
    reset() {
        clearTimeout(this.timer)
        this.start()
    },
    close() {
        clearTimeout(this.timer)
    }
}

// WebSocket连接
const connectWebsocket = () => {
    if (count > 5) {
        connectCount()
        return
    }
    try { // 正在建立连接
        ws = new WebSocket(URL)
        initWebSocket() // 初始化WebSocket连接
    } catch { // 建立连接出错，重新连接
        connect()
    }
}
// 重新连接WebSocket
function connect() {
    if (isConnect) return
    ElMessage.warning('尝试重新连接WebSocket')
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => { // 每次连接 延迟5s
        count++
        connectWebsocket()
    }, 5000)
}
// 初始化WebSocket连接
function initWebSocket() {
    ws.onopen = function () { // WebSocket连接成功
        isConnect = true
        connectStatus(true)
        ElMessage.success('WebSocket连接成功')
    }
    ws.onerror = function () { // WebSocket连接发生错误
        isConnect = false
        connectStatus(false)
        connect()
        heart.start() // 开始心跳检测
        ElMessage.error('WebSocket连接发生错误,正在尝试重新连接')
    }
    ws.onclose = function (e) { // 已关闭WebSocket连接
        isConnect = false
        connectStatus(false)
        heart.close() // 关闭心跳检测
        ElMessage.warning('已关闭WebSocket连接')
    }
    ws.onmessage = function (e) { // 接收到服务端发送的数据
        heart.reset() // 重置心跳检测
        if (e.data === checkHeart) return // 心跳检测
        receive(e.data)
    }
}
function connectCount() {
    ElMessage.error('WebSocket连接失败')
    clearTimeout(timer)
}

// 发送消息
const webSocketSend = (data) => {
    let code = ws?.readyState
    switch (code) {
        case 1:
            sendSock(data)
            break;
        case 2:
            ElMessage.warning('WebSocket连接正在关闭中,请重新连接后再发送消息')
            break;
        default:
            ElMessage.error('请先建立WebSocket连接')
            break;
    }
}
function sendSock(data) {
    ws.send(JSON.stringify(data))
    ElMessage.success('消息发送成功')
}

// 关闭连接
const closeWebSocket = () => {
    if (isConnection()) ws.close()
}
// 是否已连接
function isConnection() {
    return ws?.readyState === 1
}

// 自定义事件
function receive(data) {
    let event = new CustomEvent('receive', { detail: data })
    window.dispatchEvent(event)
}
function connectStatus(status) {
    let event = new CustomEvent('connectStatus', { detail: status })
    window.dispatchEvent(event)
}

export default {
    connectWebsocket,
    closeWebSocket,
    webSocketSend,
    isConnection
}