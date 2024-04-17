<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()" label-width="80px">
        <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="projectName">
          <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
        <el-form-item :label="$t('unieap.projectoa.comm.sgdw')" prop="sgdw">
          <el-input v-model="form.sgdw" :placeholder="$t('unieap.comm.plsInput')" />
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
        </el-form-item>
        <el-row :gutter="5" type="flex" justify="center">
          <el-col :span="15">
            <el-form-item>
              <el-button v-permission="'projectmgt.projectinfo:create'" type="primary" @click="addEditHandle()">
                <el-icon>
                  <Plus />
                </el-icon>
                {{
                  $t('unieap.projectoa.projectmgt.projectinfo.create')
                }}</el-button>
              <el-button v-permission="'projectmgt.projectinfo:delete'" type="danger" :disabled="selection.length <= 0"
                @click="deleteHandle()">
                <el-icon>
                  <Delete />
                </el-icon>
                {{ $t('unieap.comm.delete') }}</el-button>
              <el-button v-permission="'projectmgt.projectinfo:viewAttributeDetail'" type="primary"
                :disabled="selection.length != 1" @click="viewAttributeDefineHandle()">
                {{ $t('unieap.projectoa.projectmgt.projectinfo.viewAttributeDetail') }}</el-button>
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-button v-permission="'projectmgt.projectinfo:editAttributeDefine'" type="primary" @click="addEditAttributeDefineHandle()">
              {{
                $t('unieap.projectoa.projectmgt.projectinfo.editAttributeDefine')
              }}</el-button>
            <el-button v-permission="'projectmgt.projectinfo:editAttributeDetail'" type="primary" @click="addEditAttributeDetailHandle()">
              {{
                $t('unieap.projectoa.projectmgt.projectinfo.editAttributeDetail')
              }}</el-button>
            <el-button v-permission="'projectmgt.projectinfo:editRP'" type="primary" :disabled="selection.length <= 0"
              @click="deleteHandle()">
              {{ $t('unieap.projectoa.projectmgt.projectinfo.editRP') }}</el-button>
          </el-col>

        </el-row>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.seq')" prop="code" width="100" sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.projectName')" prop="name" width="200"
          sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.bidAmount')"
          prop="bidAmount" width="200" :formatter="moneyFormatter" />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.bidDate')" prop="bidDate"
          width="150" :formatter="dateFormatter" />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.planstartDate')"
          prop="planstartDate" width="150" :formatter="dateFormatter" />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.planfinishDate')"
          prop="planfinishDate" width="150" :formatter="dateFormatter" />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.duration')" prop="duration" />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.sgdw')" prop="sgdw" />
        <el-table-column align="center" :label="$t('unieap.projectoa.comm.jsdw')" prop="jsdw" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="150" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'projectmgt.projectinfo:update'" type="primary" link
              @click="addEditHandle(row.id)">{{
                $t('unieap.comm.update')
              }}</el-button>
            <el-button v-permission="'projectmgt.projectinfo:delete'" type="danger" link @click="deleteHandle(row.id)">{{
              $t('unieap.comm.delete')
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <AddEdit ref="refAddEdit" v-if="visible" @refresh="getList" />
      <AddEditAttributeDefine ref="refAddEditAttributeDefine" v-if="visibleAddEditAttributeDefine" @refresh="getList" />
      <AddEditAttributeValue ref="refAddEditAttributeValue" v-if="visibleAddEditAttributeValue" @refresh="getList" />
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
import { moneyFormatter, dateFormatter } from '@/unieap/utils/formatter'
import AddEdit from './components/add-edit-basicinfo'
import AddEditAttributeDefine from './components/add-edit-attribute-define'
import AddEditAttributeValue from './components/add-edit-attribute-value'
import { bizHandleApi } from '@/api/unieap/comm'

export default defineComponent({
  components: { AddEdit, AddEditAttributeDefine,AddEditAttributeValue },
  setup() {
    const { t } = useI18n()
    const refForm = ref()
    const refTable = ref()
    const refAddEdit = ref()
    const refAddEditAttributeDefine = ref()
    const refAddEditAttributeValue = ref()
    const { page } = usePage()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      visibleAddEditAttributeDefine: false,
      visibleAddEditAttributeValue: false,
      form: {
        sort: 'name',
        dir: 'asc'
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
        repositoryName: 'projectInfoRepository',
        beanName: 'projectoa.projectmgt.pojo.ProjectInfo',
        boName: '',
        projectName: data.form.projectName
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
          type: 'data',
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
    //项目属性分配
    const viewAttributeDefineHandle = () => {
      data.visibleAddEditAttributeDefine = true
      if (data.selection.length !== 1) {
        ElMessage({
          message: t('unieap.comm.selectOne', { dataType: '项目' }),
          type: 'warning'
        })
        return
      }
      const projectId = data.selection[0].id
      nextTick(() => {
        refAddEditAttributeDefine.value.init(projectId)
      })
    }
    //编辑
    const addEditAttributeDefineHandle = () => {
      data.visibleAddEditAttributeDefine = true
      if (data.selection.length !== 1) {
        ElMessage({
          message: t('unieap.comm.selectOne', { dataType: '项目' }),
          type: 'warning'
        })
        return
      }
      const projectId = data.selection[0].id
      nextTick(() => {
        refAddEditAttributeDefine.value.init(projectId)
      })
    }
    //编辑
    const addEditAttributeDetailHandle = () => {
      data.visibleAddEditAttributeValue = true
      if (data.selection.length !== 1) {
        ElMessage({
          message: t('unieap.comm.selectOne', { dataType: '项目' }),
          type: 'warning'
        })
        return
      }
      const projectId = data.selection[0].id
      nextTick(() => {
        refAddEditAttributeValue.value.init(projectId)
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
      refAddEditAttributeDefine,
      refAddEditAttributeValue,
      page,
      ...toRefs(data),
      sortChange,
      selectionHandle,
      clearJson,
      havePermission,
      pageChangeHandle,
      //自定方法
      moneyFormatter,
      dateFormatter,
      getList,
      reacquireHandle,
      addEditHandle,
      deleteHandle,
      viewAttributeDefineHandle,
      addEditAttributeDefineHandle,
      addEditAttributeDetailHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
