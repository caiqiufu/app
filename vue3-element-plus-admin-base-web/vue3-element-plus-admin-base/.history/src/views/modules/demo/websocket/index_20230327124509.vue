<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true">
        <el-form-item>
          <el-input
            v-model="form.sendMessage"
            :placeholder="$t('demo.name')"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.receiveMessage"
            :placeholder="$t('demo.name')"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="form.date"
            type="daterange"
            range-separator="-"
            :start-placeholder="$t('demo.createDateStart')"
            :end-placeholder="$t('demo.createDateEnd')"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="connectHandle()">初始化连接</el-button>
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
  onMounted,
  onBeforeMount,
  reactive,
  ref,
  toRefs,
} from "vue";
import { Howl } from "howler";
import { ElMessage, ElMessageBox } from "element-plus";
import { useStore } from "vuex";
import { clearJson, havePermission, parseDate2Str } from "@/utils";

import { io } from "socket.io-client";

export default defineComponent({
  components: {},
  setup() {
    const refForm = ref();
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sendMessage: "",
        receiveMessage: "",
      },
      list: [],
      selection: [],
    });
    let socket = null;

    const connectHandle = () => {
      // 建立连接的事件
      socket.on("connect", () => console.log("connect: websocket 连接成功！"));
    };
    onMounted(() => {
      // 创建客户端 websocket 的实例
      socket = io("ws://localhost:8816/slipper/websocket/11122233");
    });
    onBeforeMount(() => {
      // 关闭连接
      socket.close();
      // 销毁 websocket 实例对象
      socket = null;
    });

    return {
      refForm,
      ...toRefs(data),
      clearJson,
      connectHandle
    };
  },
});
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
