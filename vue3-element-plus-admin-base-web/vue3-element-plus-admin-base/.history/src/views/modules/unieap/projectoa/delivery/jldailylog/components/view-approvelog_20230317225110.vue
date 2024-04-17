<template>
  <el-dialog
    width="800px"
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
      <el-table-column
        align="center"
        label="审核时间"
        prop="createDate"
        width="180"
      />
      <el-table-column align="center" label="审核人" prop="createBy" />
      <el-table-column
        align="center"
        label="审核结果"
        prop="activateFlagDesc"
        width="150"
      />
      <el-table-column align="center" label="审核意见" prop="approveComment" />
    </el-table>
  </el-dialog>
</template>

<script>
import { defineComponent, nextTick, reactive, toRefs } from "vue";
//自定包引入
import { bizHandleApi } from "@/api/unieap/comm";

export default defineComponent({
  emits: ["refresh"],
  setup(_props, { emit }) {
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
    //选中
    const selectionHandle = (val) => {
      data.selection = val;
    };
    /**
     * column：当前列
     * prop：当前列需要排序的字段名
     * order：排序的规则（升序、降序和默认，默认就是没排序）
     * @param {*} column
     * @param {*} prop
     * @param {*} order
     */
     const sortChange = (column) => {
      data.form.sort = column.prop;
      if (column.prop) {
        if (column.order === "ascending") {
          data.form.dir = "asc";
        } else {
          data.form.dir = "desc";
        }
      }
      getList();
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
      selectionHandle,
      sortChange
    };
  },
});
</script>
