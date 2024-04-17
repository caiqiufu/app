<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item label="用户" prop="userCode">
          <el-input
            v-model="form.name"
            :placeholder="$t('unieap.comm.plsInput')"
          />
        </el-form-item>
        <el-form-item label="账户" prop="accountCode">
          <el-input
            v-model="form.name"
            :placeholder="$t('unieap.comm.plsInput')"
          />
        </el-form-item>
        <el-form-item label="任务类型" prop="type">
          <el-select
            v-model="form.type"
            :placeholder="$t('unieap.comm.plsSelect')"
          >
            <el-option
              v-for="item in optionTaskType"
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
          <el-button type="primary" @click="addEditHandle()">
            <el-icon>
              <Plus />
            </el-icon>
            {{ $t("unieap.comm.create") }}</el-button
          >
          <el-button
            type="danger"
            :disabled="selection.length <= 0"
            @click="deleteHandle()"
          >
            <el-icon>
              <Delete />
            </el-icon>
            {{ $t("unieap.comm.delete") }}</el-button
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
          label="是否提醒"
          prop="notifyFlagDesc"
          width="200"
        />
        <el-table-column
          align="center"
          label="提醒周期"
          prop="notifyCron"
          width="200"
        />
        <el-table-column
          align="center"
          label="提醒消息模版"
          prop="messageTemplate"
          width="200"
        />
        <el-table-column
          align="center"
          :label="$t('unieap.comm.operation')"
          width="200"
          fixed="right"
        >
          <template v-slot="{ row }">
            <el-button type="primary" link @click="addEditHandle(row.id)">{{
              $t("unieap.comm.update")
            }}</el-button>
            <el-button type="danger" link @click="deleteHandle(row.id)">{{
              $t("unieap.comm.delete")
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <AddEdit ref="refAddEdit" v-if="visible" @refresh="getList" />
    </template>
    <template #footer>
      <Page :page="page" @change="pageChangeHandle" />
    </template>
  </Container>
</template>

<script>
import { useI18n } from "vue-i18n";
import {
  defineComponent,
  nextTick,
  onBeforeMount,
  reactive,
  ref,
  toRefs,
} from "vue";
import usePage from "@/mixins/page";
import { clearJson, havePermission } from "@/utils";
import { ElMessage, ElMessageBox } from "element-plus";
//自定包引入
import AddEdit from "./components/add-edit";
import { infoDicDataApi,bizHandleApi } from "@/api/unieap/comm";

export default defineComponent({
  components: { AddEdit },
  setup() {
    const { t } = useI18n();
    const refForm = ref();
    const refTable = ref();
    const refAddEdit = ref();
    const { page } = usePage();
    //数据定义
    const urlPrefix = "/backstage/common/";
    const data = reactive({
      loading: false,
      visible: false,
      optionTaskType:[],
      form: {
        sort: "id",
        dir: "asc",
        name: "",
        type:''
      },
      list: [],
      selection: [],
    });
    //分页数据,根据需求修改
    const getList = () => {
      const params = {
        sort: data.form.sort,
        dir: data.form.dir,
        currentPage: page.current,
        pageSize: page.size,
        //查询参数
        beanName: "projectoa.supervisionmgt.pojo.TaskDefine",
        name: data.form.name,
        type: data.form.type
      };
      data.loading = true;
      params.url = urlPrefix + "commPage";
      bizHandleApi(params).then((r) => {
        if (r) {
          data.list = r.data.list;
          page.total = r.data.total;
        }
        nextTick(() => {
          data.loading = false;
        });
      });
    };
    //查询
    const reacquireHandle = () => {
      page.current = 1;
      getList();
    };
    //编辑
    const addEditHandle = (id) => {
      data.visible = true;
      nextTick(() => {
        refAddEdit.value.init(id);
      });
    };
    //删除,根据需求修改
    const deleteHandle = (id) => {
      const ids = id ? [id] : data.selection.map((item) => item.id);
      ElMessageBox.confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          const params = {
            beanName: "projectoa.supervisionmgt.pojo.TaskDefine",
            url: urlPrefix + "commDeal",
            operType: "delete",
            keys: ids.join(","),
          };
          bizHandleApi(params).then((r) => {
            if (r) {
              ElMessage({
                message: t("unieap.comm.optSuccess"),
                type: "success",
              });
              getList();
            }
          });
        })
        .catch(() => {
          // to do something on canceled
        });
    };

    const initDicData = async () => {
      infoDicDataApi({ groupCode: "TASK_TYPE" }).then((r) => {
        if (r) {
          data.optionTaskType = r.data;
        }
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
    //分页
    const pageChangeHandle = (argPage) => {
      page.current = argPage.current;
      page.size = argPage.size;
      getList();
    };

    onBeforeMount(() => {
      initDicData();
      getList();
    });

    return {
      refForm,
      refTable,
      refAddEdit,
      page,
      ...toRefs(data),
      sortChange,
      selectionHandle,
      clearJson,
      havePermission,
      pageChangeHandle,
      //自定方法
      getList,
      reacquireHandle,
      addEditHandle,
      deleteHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
