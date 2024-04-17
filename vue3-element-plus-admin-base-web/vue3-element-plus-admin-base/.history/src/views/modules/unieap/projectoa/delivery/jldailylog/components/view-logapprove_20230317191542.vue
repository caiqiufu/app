<template>
  <el-dialog
    width="600px"
    title="审核历史"
    v-model="visible"
    :close-on-click-modal="false"
    @closed="dialogClosedHandle"
    append-to-body
    draggable
    destroy-on-close
  >
    <el-table
      ref="refTable"
      v-loading="loading"
      :data="list"
      @selection-change="selectionHandle"
      @sort-change="sortChange"
      border
    >
      <el-table-column align="center" type="selection" width="50" />
      <el-table-column align="center" label="审核时间" prop="createDate" />
      <el-table-column align="center" label="审核人" prop="createBy" />
      <el-table-column
        align="center"
        label="审核结果"
        prop="approveResultDesc"
      />
      <el-table-column align="center" label="审核意见" prop="approveComment" />
    </el-table>
  </el-dialog>
</template>

<script>
import { useI18n } from "vue-i18n";
import {
  defineComponent,
  nextTick,
  reactive,
  ref,
  toRefs,
} from "vue";
import { ElMessage } from "element-plus";
//自定包引入
import { bizHandleApi } from "@/api/unieap/comm";

export default defineComponent({
  emits: ["refresh"],
  setup(_props, { emit }) {
    const { t } = useI18n();
    const refForm = ref();
    //数据定义
    const urlPrefix = "/backstage/common/";
    const data = reactive({
      loading: false,
      visible: false,
      logId: null,
      list: [],
      selection: [],
    });

    //分页数据,根据需求修改
    const getList = () => {
      data.loading = true;
      const params = {
        sort: "createDate",
        dir: "ASC",
        //查询参数
        beanName: "projectoa.delivery.pojo.ApproveLog",
        logId: data.logId,
      };
      params.url = urlPrefix + "commPage";
      bizHandleApi(params).then((r) => {
        if (r) {
          data.list = r.data.list;
        }
        nextTick(() => {
          data.loading = false;
        });
      });
    };

    /**
     * @description: 加载窗口初始化
     * @param {*} id
     */
    const init = async (id) => {
      data.visible = true;
      data.loading = true;
      data.logId = id;
      getList();
      nextTick(() => {
        data.loading = false;
      });
    };

    /**
     * @description: 弹窗关闭动画结束时的回调
     * @param {*}
     * @return {*}
     */
    const dialogClosedHandle = () => {};

    return {
      ...toRefs(data),
      init,
      dialogClosedHandle,
    };
  },
});
</script>
