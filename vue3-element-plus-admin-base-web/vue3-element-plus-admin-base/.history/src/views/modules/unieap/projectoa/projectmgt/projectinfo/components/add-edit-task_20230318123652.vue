<template>
  <el-dialog
    width="600px"
    :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')"
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
        label="任务名称"
        prop="name"
        width="200"
        sortable="custom"
      />
      <el-table-column
        align="center"
        label="任务类型"
        prop="typeDesc"
        width="200"
      />
      <el-table-column
        align="center"
        label="提醒周期"
        prop="notifyCron"
        width="200"
      />
      <el-table-column align="center" label="是否提醒" prop="notifyFlag"
          width="160">
          <template v-slot="{ row }">
            <el-switch @change="notifyFlagHandle(row)" v-model="row.notifyFlag" active-value="Y" inactive-value="N" />
          </template>
        </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import { useI18n } from "vue-i18n";
import {
  computed,
  defineComponent,
  nextTick,
  reactive,
  ref,
  toRefs,
} from "vue";
import { ElMessage } from "element-plus";
import { infoDicDataApi, bizHandleApi } from "@/api/unieap/comm";
//自定包引入
import { updateNotifyFlagApi} from '@/api/unieap/projectoa/projectmgt/projectinfo'

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
      optionTaskType: [],
      optionNotifyFlag: [],
      form: {
        id: null,
        name: "",
        type: "",
        priority: "",
        notifyFlag: "",
        notifyCron: "",
        messageTemplate: "",
      },
      roles: [],
    });
    const rules = computed(() => {
      return {};
    });
    /**
     * @description: 加载窗口初始化
     * @param {*} id
     */
    const init = async (id) => {
      infoDicDataApi({ groupCode: "TASK_TYPE" }).then((r) => {
        if (r) {
          data.optionTaskType = r.data;
        }
      });
      infoDicDataApi({ groupCode: "YESNO_FLAG" }).then((r) => {
        if (r) {
          data.optionNotifyFlag = r.data;
        }
      });
      data.visible = true;
      data.loading = true;
      data.form.id = id;
      if (id) {
        const params = {
          beanName: "projectoa.supervisionmgt.pojo.TaskDefine",
          boName: "",
          url: urlPrefix + "commInfo",
          id: id,
        };
        bizHandleApi(params).then((r) => {
          if (r) {
            data.form = r.data;
          }
        });
      }
      nextTick(() => {
        data.loading = false;
      });
    };

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
      refForm.value.validate(async (valid) => {
        if (valid) {
          const handleName = "commDeal";
          const operType = data.form.id ? "update" : "create";
          const urlObj = {
            beanName: "projectoa.supervisionmgt.pojo.TaskDefine",
            url: urlPrefix + handleName,
            operType: operType,
          };
          const params = Object.assign(urlObj, data.form);
          bizHandleApi(params).then((r) => {
            if (r) {
              data.visible = false;
              ElMessage({
                message: t("unieap.comm.optSuccess"),
                type: "success",
              });
              emit("refresh");
            }
          });
        }
      });
    };

    const notifyFlagHandle = (row) => {
      const params = row
      if (row.id) {
        updatePublishFlagApi(params).then(r => {
          if (r) {
            ElMessage({
              message: '操作成功!',
              type: 'success'
            })
            getList()
          }
        })
      }
    }

    /**
     * @description: 弹窗关闭动画结束时的回调
     * @param {*}
     * @return {*}
     */
    const dialogClosedHandle = () => {
      refForm.value.resetFields();
    };

    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle,
      notifyFlagHandle
    };
  },
});
</script>
