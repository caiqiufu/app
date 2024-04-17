<template>
  <el-dialog
    width="800px"
    :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')"
    v-model="visible"
    :close-on-click-modal="false"
    @closed="dialogClosedHandle"
    append-to-body
    draggable
    destroy-on-close
  >
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item label="任务名称" prop="name">
          <el-input
            v-model="form.name"
            :placeholder="$t('unieap.comm.plsInput')"
          />
        </el-form-item>
        <el-form-item label="单位人员">
          <el-select
            v-model="form.employeeId"
            :placeholder="$t('unieap.comm.plsSelect')"
          >
            <el-option
              v-for="item in optionEmployeeList"
              :key="item.dicCode"
              :label="item.dicName"
              :value="item.dicCode"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="reacquireHandle()">
            <el-icon>
              <Search />
            </el-icon>
            {{ $t("unieap.comm.search") }}</el-button
          >
          <el-button v-repeat @click="clearJson(form), reacquireHandle()">
            <el-icon>
              <Refresh />
            </el-icon>
            {{ $t("unieap.comm.reset") }}</el-button
          >
        </el-form-item>
      </el-form>
    </template>
    <template #default>
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
          width="150"
          sortable="custom"
        />
        <el-table-column
          align="center"
          label="任务类型"
          prop="typeDesc"
          width="100"
        />
        <el-table-column
          align="center"
          label="提醒周期"
          prop="notifyCron"
          width="100"
        />
        <el-table-column
          align="center"
          label="员工名称"
          prop="employeeName"
          width="100"
        >
        <template v-slot="{ row }">
          <el-cascader class="width-full" ref="refCascader" v-model="row.employeeId" :options="employeeIds"
          :props="cascaderProps" :show-all-levels="false" collapse-tags collapse-tags-tooltip clearable />
          </template>
      </el-table-column>
        <el-table-column
          align="center"
          label="是否提醒"
          prop="notifyFlag"
          width="100"
        >
          <template v-slot="{ row }">
            <el-switch
              @change="notifyFlagHandle(row)"
              v-model="row.notifyFlag"
              active-value="Y"
              inactive-value="N"
            />
          </template>
        </el-table-column>
      </el-table>
    </template>
    <template #footer>
      <Page :page="page" @change="pageChangeHandle" />
    </template>
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
import usePage from '@/mixins/page'
import { clearJson } from "@/utils";
import { ElMessage } from "element-plus";
import { infoDicDataApi, bizHandleApi } from "@/api/unieap/comm";
//自定包引入
import { updateNotifyFlagApi } from "@/api/unieap/projectoa/projectmgt/projectinfo";

export default defineComponent({
  emits: ["refresh"],
  setup(_props, { emit }) {
    const { t } = useI18n();
    const refForm = ref();
    const { page } = usePage();
    //数据定义
    const urlPrefix = "/backstage/projectoa/projectmgt/projectInfo/";
    const data = reactive({
      loading: false,
      visible: false,
      optionEmployeeList: [],
      employeeIds:[],
      projectId: "",
      form: {
        id: null,
        projectId: "",
        employeeIds: [],
        employeeName: "",
        taskId: "",
        taskName: "",
        notifyFlag: "",
      },
      roles: [],
      list: [],
      selection: [],
    });
    const rules = computed(() => {
      return {};
    });
    const cascaderProps = computed(() => {
      const reuslt = {
        multiple: true,
        emitPath: false,
        checkStrictly: false,
        value: 'dicCode',
        label: `dicName`,
        children: 'children'
      }
      return reuslt
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id
     */
    const init = async (projectId) => {
      data.visible = true;
      data.loading = true;
      data.projectId = projectId;
      data.form.projectId = projectId;
      bizHandleApi({
        url: urlPrefix + "employeeDicDataList",
      }).then((r) => {
        if (r) {
          data.optionEmployeeList = r.data;
        }
      });
      getList();
      nextTick(() => {
        data.loading = false;
      });
    };
    //分页数据,根据需求修改
    const getList = () => {
      bizHandleApi({
        currentPage: page.current,
        pageSize: page.size,
        url: urlPrefix + "getAllTaskInfoList",
        projectId: data.projectId,
        employeeId: data.form.employeeId,
      }).then((r) => {
        if (r) {
          data.list = r.data.list;
          page.total = r.data.total;
        }
      });
    };

    const notifyFlagHandle = (row) => {
      if (row.id) {
        updateNotifyFlagApi({
          id: row.id,
          projectId: data.projectId,
          employeeId: data.form.employeeId,
          taskId: data.form.taskId,
          notifyFlag: data.form.notifyFlag,
        }).then((r) => {
          if (r) {
            ElMessage({
              message: "操作成功!",
              type: "success",
            });
            getList();
          }
        });
      }
    };
    //查询
    const reacquireHandle = () => {
      page.current = 1;
      getList();
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
    //分页
    const pageChangeHandle = (argPage) => {
      page.current = argPage.current;
      page.size = argPage.size;
      getList();
    };

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
      page,
      ...toRefs(data),
      rules,
      init,
      sortChange,
      selectionHandle,
      clearJson,
      pageChangeHandle,
      //自定方法
      getList,
      reacquireHandle,
      dialogClosedHandle,
      notifyFlagHandle,
    };
  },
});
</script>
