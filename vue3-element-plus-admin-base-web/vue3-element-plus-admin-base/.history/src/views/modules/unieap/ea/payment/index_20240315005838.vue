<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item label="用户" prop="userCode">
          <el-input v-model="form.userCode" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
        <el-form-item label="账户" prop="accountCode">
          <el-input v-model="form.accountCode" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
        <el-form-item label="账单类型" prop="billType">
          <el-select v-model="form.billType" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionBillType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="reacquireHandle()">
            <el-icon>
              <Search />
            </el-icon>
            {{ $t("unieap.comm.search") }}</el-button>
          <el-button v-repeat @click="clearJson(form), reacquireHandle()">
            <el-icon>
              <Refresh />
            </el-icon>
            {{ $t("unieap.comm.reset") }}</el-button>

        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" label="用户" prop="userCode" width="200" sortable="custom" />
        <el-table-column align="center" label="账户" prop="accountCode" width="200" />
        <el-table-column align="center" label="费用项" prop="feeDesc" width="200" />
        <el-table-column align="center" label="账期" prop="billDate" width="200" />
        <el-table-column align="center" label="账单金额" prop="billAmount" width="200" />
        <el-table-column align="center" label="待缴费金额" prop="outstandingAmount" width="200" />
        <el-table-column align="center" label="已缴费金额" prop="paidAmount" width="200" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="200" fixed="right">
          <template v-slot="{ row }">
            <el-button type="primary" link @click="addEditHandle(row.id)" :disabled="!row.billAmount>0">缴费</el-button>
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
import { infoDicDataApi, bizHandleApi } from "@/api/unieap/comm";

export default defineComponent({
  components: { AddEdit },
  setup() {
    const { t } = useI18n();
    const refForm = ref();
    const refTable = ref();
    const refAddEdit = ref();
    const { page } = usePage();
    //数据定义
    const urlPrefix = "/backstage/ea/mybill/";
    const data = reactive({
      loading: false,
      visible: false,
      optionBillType: [{ "dicCode": "0", "dicName": "全部"}, { "dicCode": "2", "dicName": "欠费" }],
      form: {
        sort: "id",
        dir: "asc",
        userCode: "",
        accountCode: "",
        billType: '0'
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
        beanName: "",
        userCode: data.form.userCode,
        accountCode: data.form.accountCode,
        billType: data.form.billType
      };
      data.loading = true;
      params.url = urlPrefix + "billDetailPage";
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
      //由于有文件上传,需要提前生成额外的主键
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
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
