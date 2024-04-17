<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="reacquireHandle()">
            <el-icon>
              <Search />
            </el-icon>
            {{ $t('unieap.comm.search') }}</el-button>
          <el-button v-repeat @click="clearJson(form), reacquireHandle()">
            <el-icon>
              <Refresh />
            </el-icon>
            {{ $t('unieap.comm.reset') }}</el-button>
          <el-button v-permission="'supervisionmgt.company:create'" type="primary" @click="addEditHandle()">
            <el-icon>
              <Plus />
            </el-icon>
            {{
              $t('unieap.projectoa.supervisionmgt.company.create')
            }}</el-button>
          <el-button v-permission="'supervisionmgt.company:delete'" type="danger" :disabled="selection.length <= 0"
            @click="deleteHandle()">
            <el-icon>
              <Delete />
            </el-icon>
            {{ $t('unieap.comm.delete') }}</el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="name"
         width="200" sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.company.type')" prop="name"
          width="200" sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.company.businessCorporation')"
          prop="businessCorporation" width="200" sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.company.businessLicense')"
          prop="businessLicense" width="200" sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.company.address')" prop="address"
          width="200" sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="200" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'supervisionmgt.company:update'" type="primary" link
              @click="addEditHandle(row.id)">{{
                $t('unieap.comm.update')
              }}</el-button>
            <el-button v-permission="'supervisionmgt.company:delete'" type="danger" link @click="deleteHandle(row.id)">{{
              $t('unieap.comm.delete')
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
import { useI18n } from 'vue-i18n'
import { defineComponent, nextTick, onBeforeMount, reactive, ref, toRefs } from 'vue'
import usePage from '@/mixins/page'
import { clearJson, havePermission } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'
//自定包引入
import AddEdit from './components/add-edit'
import { bizHandleApi } from '@/api/unieap/comm'

export default defineComponent({
  components: { AddEdit },
  setup() {
    const { t } = useI18n()
    const refForm = ref()
    const refTable = ref()
    const refAddEdit = ref()
    const { page } = usePage()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sort: 'id',
        dir: 'asc',
        name: ''
      },
      list: [],
      selection: []
    })
    //分页数据,根据需求修改
    const getList = () => {
      const params = {
        sort: data.form.sort,
        dir: data.form.dir,
        currentPage: page.current,
        pageSize: page.size,
        //查询参数
        repositoryName: 'companyInfoRepository',
        beanName: 'projectoa.supervisionmgt.pojo.CompanyInfo',
        boName: '',
        name: data.form.name
      }
      data.loading = true
      params.url = urlPrefix + 'commPage'
      bizHandleApi(params).then(r => {
        if (r) {
          data.list = r.data.list
          page.total = r.data.total
        }
        nextTick(() => { data.loading = false })
      })
    }
    //查询
    const reacquireHandle = () => {
      page.current = 1
      getList()
    }
    //编辑
    const addEditHandle = (id) => {
      data.visible = true
      nextTick(() => {
        refAddEdit.value.init(id)
      })
    }
    //删除,根据需求修改
    const deleteHandle = (id) => {
      const ids = id ? [id] : data.selection.map(item => item.id)
      ElMessageBox.confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const params = {
          keys: ids,
          url: urlPrefix + 'delete'
        }
        bizHandleApi(params).then(r => {
          if (r) {
            ElMessage({
              message: t('unieap.comm.optSuccess'),
              type: 'success'
            })
            getList()
          }
        })
      }).catch(() => {
        // to do something on canceled
      })
    }
    //选中
    const selectionHandle = (val) => {
      data.selection = val
    }

    /**
       * column：当前列
       * prop：当前列需要排序的字段名
       * order：排序的规则（升序、降序和默认，默认就是没排序）
       * @param {*} column 
       * @param {*} prop 
       * @param {*} order 
       */
    const sortChange = (column) => {
      data.form.sort = column.prop
      if (column.prop) {
        if (column.order === 'ascending') {
          data.form.dir = 'asc'
        } else {
          data.form.dir = 'desc'
        }
      }
      getList()
    }
    //分页
    const pageChangeHandle = (argPage) => {
      page.current = argPage.current
      page.size = argPage.size
      getList()
    }

    onBeforeMount(() => {
      getList()
    })

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
      deleteHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
