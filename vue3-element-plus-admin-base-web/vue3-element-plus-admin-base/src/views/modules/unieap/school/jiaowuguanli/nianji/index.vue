<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true">
        <el-form-item>
          <el-button v-permission="'jiaowuguanli.nianji:create'" type="primary" @click="addEditHandle()">
            <el-icon>
              <Plus />
            </el-icon>
            {{
                $t('unieap.school.jiaowuguanli.nianji.create')
            }}

          </el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle" @sort-change="sortChange"  border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.code')" prop="id"  sortable='custom' width="80" />
        <el-table-column align="center" :label="$t('unieap.school.comm.grade')" prop="name" />
        <el-table-column align="center" :label="$t('unieap.school.jiaowuguanli.nianji.classCount')" prop="classCount"
          width="200" />
        <el-table-column align="center" :label="$t('unieap.comm.createDate')" prop="createDate" width="200" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="200" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'jiaowuguanli.nianji:update'" type="primary" link @click="addEditHandle(row.id)">{{
                $t('unieap.comm.update')
            }}</el-button>
            <el-button v-permission="'jiaowuguanli.nianji:delete'" type="danger" link @click="deleteHandle(row.id)">{{
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
import { defineComponent, nextTick, onBeforeMount, reactive, ref, toRefs } from 'vue'

import { ElMessage, ElMessageBox } from 'element-plus'
import AddEdit from './components/add-edit'

import usePage from '@/mixins/page'
import { clearJson, havePermission, parseDate2Str } from '@/utils'

import { pageApi, deleteApi} from '@/api/unieap/school/jiaowuguanli/nianji'

export default defineComponent({
  components: { AddEdit },
  setup() {
    const refForm = ref()
    const refTable = ref()
    const refAddEdit = ref()
    const { page } = usePage()
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sort:'id',
        dir:'asc'
      },
      list: [],
      selection: []
    })

    const getList = () => {
      const params = {
        sort: data.form.sort,
        dir: data.form.dir,
        currentPage: page.current,
        pageSize: page.size
      }
      data.loading = true
      pageApi(params).then(r => {
        if (r) {
          data.list = r.data.list
          page.total = r.data.total
        }
        nextTick(() => { data.loading = false })
      })
    }

    const reacquireHandle = () => {
      page.current = 1
      getList()
    }

    const addEditHandle = (id) => {
      data.visible = true
      nextTick(() => {
        refAddEdit.value.init(id)
      })
    }

    const deleteHandle = (id) => {
      const ids = id ? [id] : data.selection.map(item => item.id)
      ElMessageBox.confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteApi({ keys: ids,url:'aaa' }).then(r => {
          if (r) {
            ElMessage({
              message: '操作成功!',
              type: 'success'
            })
            getList()
          }
        })
      }).catch(() => {
        // to do something on canceled
      })
    }

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
     const sortChange = (column)=>{
      data.form.sort = column.prop
      if (column.prop){
        if (column.order === 'ascending'){
          data.form.dir = 'asc'
        }else{
          data.form.dir = 'desc'
        }
      }      
      getList()
    }

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
      getList,
      pageChangeHandle,
      reacquireHandle,
      addEditHandle,
      deleteHandle,
      selectionHandle,
      clearJson,
      havePermission
    }
  }
})
</script>
  
<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
  