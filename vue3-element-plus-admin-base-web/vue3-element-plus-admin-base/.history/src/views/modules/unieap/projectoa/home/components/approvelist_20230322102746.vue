<template>
  <Container>
    <template #header>
      <div class="panel-title margin_b-10 font-size-18">待审核监理日志</div>
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
          label="项目名称"
          prop="projectName"
          width="200"
        />
        <el-table-column
          align="center"
          label="监理日志日期"
          prop="logTime"
          width="200"
        />
        <el-table-column
          align="center"
          label="日志提交人"
          prop="createBy"
          width="200"
        />
      </el-table>
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
//自定包引入
import { bizHandleApi } from "@/api/unieap/comm";

export default defineComponent({
  components: {},
  setup() {
    const { t } = useI18n();
    const refForm = ref();
    const refTable = ref();
    const { page } = usePage();
    //数据定义
    const urlPrefix = "/backstage/common/";
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sort: "id",
        dir: "asc",
        name: "",
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
        beanName: "projectoa.delivery.pojo.JldailyLog",
        name: data.form.name,
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
    onBeforeMount(() => {
      getList();
    });

    return {
      refForm,
      refTable,
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
    };
  },
});
</script>

<style lang="scss" scoped>
.panel {
  height: fit-content;
  min-width: 150px;
  border-radius: var(--el-border-radius-base);
  background-color: var(--gl-content-panel-background-color);

  &-title {
    font-weight: 700;
  }

  &-content {
    height: 100px;
  }
}
</style>
