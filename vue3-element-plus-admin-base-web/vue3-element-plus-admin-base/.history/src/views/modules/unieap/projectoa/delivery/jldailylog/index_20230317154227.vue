<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-row>
          <el-col :span="5">
            <el-form-item
              :label="$t('unieap.projectoa.comm.projectName')"
              prop="projectName"
            >
              <el-input
                v-model="form.projectName"
                :placeholder="$t('unieap.comm.plsInput')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              :label="$t('unieap.projectoa.delivery.jldailylog.logTime')"
              prop="rlogTimeRange"
            >
              <el-date-picker
                v-model="form.rlogTimeRange"
                type="daterange"
                :range-separator="$t('unieap.comm.fromTo')"
                :start-placeholder="$t('unieap.comm.dateStart')"
                :end-placeholder="$t('unieap.comm.dateEnd')"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item :label="$t('unieap.comm.status')">
              <el-select
                v-model="form.status"
                :placeholder="$t('unieap.comm.plsSelect')"
              >
                <el-option
                  v-for="item in optionJlLogStatus"
                  :key="item.dicCode"
                  :label="item.dicName"
                  :value="item.dicCode"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
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
          </el-col>
        </el-row>
        <el-row :gutter="5" type="flex" justify="center">
          <el-col :span="15">
            <el-form-item>
              <el-button
                v-permission="'delivery.jldailylog:create'"
                type="primary"
                @click="addEditHandle()"
              >
                <el-icon>
                  <Plus />
                </el-icon>
                {{
                  $t("unieap.projectoa.delivery.jldailylog.create")
                }}</el-button
              >
              <el-button
                v-permission="'delivery.jldailylog:delete'"
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
          </el-col>
          <el-col :span="9">
            <el-button
              v-permission="'delivery.jldailylog:view'"
              type="primary"
              :disabled="selection.length != 1"
              @click="viewLogHandle()"
            >
              {{ $t("unieap.comm.view") }}</el-button
            >
            <el-button
              v-permission="'delivery.jldailylog:approve'"
              type="primary"
              :disabled="selection.length != 1"
              @click="approveHandle()"
            >
              {{
                $t("unieap.projectoa.delivery.jldailylog.approve")
              }}</el-button
            >
          </el-col>
        </el-row>
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
          :label="$t('unieap.projectoa.comm.projectName')"
          prop="projectName"
          sortable="custom"
        />
        <el-table-column
          align="center"
          :label="$t('unieap.projectoa.delivery.jldailylog.logTime')"
          prop="logTime"
          width="200"
        />
        <el-table-column
          align="center"
          :label="$t('unieap.comm.status')"
          prop="statusDesc"
          width="100"
        />
        <el-table-column
          align="center"
          :label="$t('unieap.comm.createBy')"
          prop="createBy"
          width="100"
        />
        <el-table-column
          align="center"
          :label="$t('unieap.comm.operation')"
          width="230"
          fixed="right"
        >
          <template v-slot="{ row }">
            <el-button
              v-permission="'delivery.jldailylog:view'"
              type="primary"
              link
              @click="viewLogHandle(row.id)"
              >{{ $t("unieap.comm.view") }}</el-button
            >
            <el-button
              v-permission="'delivery.jldailylog:update'"
              type="primary"
              link
              @click="addEditHandle(row)"
              >{{ $t("unieap.comm.update") }}</el-button
            >
            <el-button
              v-permission="'delivery.jldailylog:delete'"
              type="danger"
              link
              @click="deleteHandle(row.id)"
              >{{ $t("unieap.comm.delete") }}</el-button
            >
            <el-button
              v-permission="'delivery.jldailylog:approve'"
              type="danger"
              link
              @click="approveHandle(row.id)"
              >{{
                $t("unieap.projectoa.delivery.jldailylog.approve")
              }}</el-button
            >
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
import { parseDate2Str } from "@/utils";
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
    const urlPrefix = "/backstage/common/";
    const data = reactive({
      loading: false,
      visible: false,
      visibleViewLog: false,
      optionJlLogStatus: [],
      form: {
        sort: "id",
        dir: "asc",
        projectName: "",
        status: "",
        rlogTimeRange: [],
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
        repositoryName: "",
        beanName: "projectoa.delivery.vo.JldailyLogVO",
        boName: "jldailyLogBO",
        projectName: data.form.projectName,
        status: data.form.status,
        dateStart:
          data.form.rlogTimeRange && data.form.rlogTimeRange.length > 0
            ? parseDate2Str(data.form.rlogTimeRange[0])
            : "",
        dateEnd:
          data.form.rlogTimeRange && data.form.rlogTimeRange.length > 1
            ? parseDate2Str(data.form.rlogTimeRange[1])
            : "",
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

    const jlLogStatusOptions = async () => {
      const dicJlLogStatus = await infoDicDataApi({
        groupCode: "JLLOG_STATUS",
      });
      if (dicJlLogStatus) {
        data.optionJlLogStatus = dicJlLogStatus.data;
      }
    };

    //查询
    const reacquireHandle = () => {
      page.current = 1;
      getList();
    };
    //编辑
    const addEditHandle = (row) => {
      data.visible = true;
      nextTick(() => {
        if (row.id) {
          if (row.status != "Draft" && row.status != "Reject" ) {
            ElMessage({
              message: '已提交审核记录不能修改',
              type: "warning",
            });
            return;
          }
          refAddEdit.value.initEdit(row.id);
        } else {
          refAddEdit.value.init(null);
        }
      });
    };
    //浏览
    const viewLogHandle = (id) => {
      //窗口初始化后，才能调用该页面内的方法和变量
      if (id == null) {
        if (data.selection.length !== 1) {
          ElMessage({
            message: "该监理日志也提交审核,不能修改",
            type: "warning",
          });
          return;
        }
        id = data.selection[0].id;
      }
      data.visible = true;
      nextTick(() => {
        if (id) {
          refAddEdit.value.initView(id);
        }
      });
    };
    //审核
    const approveHandle = (id) => {
      //窗口初始化后，才能调用该页面内的方法和变量
      if (id == null) {
        if (data.selection.length !== 1) {
          ElMessage({
            message: t("unieap.comm.selectOne", { dataType: "日志" }),
            type: "warning",
          });
          return;
        }
        id = data.selection[0].id;
      }
      data.visible = true;
      nextTick(() => {
        if (id) {
          refAddEdit.value.initApprove(id);
        }
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
            repositoryName: "jldailyLogRepository",
            beanName: "projectoa.delivery.pojo.JldailyLog",
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
      jlLogStatusOptions();
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
      viewLogHandle,
      approveHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 5px;
}
</style>
