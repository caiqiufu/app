<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item>
          <el-input v-model="form.name" :placeholder="$t('demo.name')" clearable />
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="form.date" type="daterange" range-separator="-"
            :start-placeholder="$t('demo.createDateStart')" :end-placeholder="$t('demo.createDateEnd')" clearable />
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="reacquireHandle()">{{ $t('demo.search') }}</el-button>
          <el-button v-repeat @click="clearJson(form), reacquireHandle()">{{ $t('demo.reset') }}</el-button>
          <el-button v-permission="'enterprise:create'" type="primary" @click="addEditHandle()">{{ $t('demo.add')
          }}</el-button>
          <el-button v-permission="'enterprise:delete'" type="danger" :disabled="selection.length <= 0"
            @click="deleteHandle()">{{ $t('demo.batchDelete') }}</el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('demo.id')" prop="id" width="80" />
        <el-table-column align="center" :label="$t('demo.code')" prop="code" width="80">
        </el-table-column>
        <el-table-column align="center" :label="$t('demo.name')" prop="name" />
        <el-table-column align="center" :label="$t('demo.activateFlag')" prop="activateFlagDesc" />
        <el-table-column align="center" :label="$t('demo.createDate')" prop="createDate" width="160" />
        <el-table-column align="center" :label="$t('demo.modifyDate')" prop="modifyDate" width="160" />
        <el-table-column align="center" :label="$t('demo.operation')" width="110" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'enterprise:update'" type="primary" link @click="addEditHandle(row.id)">{{
                $t('demo.edit')
            }}</el-button>
            <el-button v-permission="'enterprise:delete'" type="danger" link @click="deleteHandle(row.id)">{{
                $t('demo.delete')
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

import { pageApi, deleteApi, setStatusApi } from '@/api/demo'

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
        name: '',
        date: []
      },
      list: [],
      selection: []
    })

    const getList = () => {
      const params = {
        name: data.form.name,
        start: data.form.date && data.form.date.length > 0 ? parseDate2Str(data.form.date[0]) : '',
        end: data.form.date && data.form.date.length > 1 ? parseDate2Str(data.form.date[1]) : '',
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
        deleteApi({ keys: ids }).then(r => {
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

    const statusHandle = (row) => {
      const params = {
        key: row.id,
        value: row.status
      }
      setStatusApi(params).then(r => {
        if (r) {
          ElMessage({
            message: '操作成功!',
            type: 'success'
          })
        } else {
          row.status = row.status === 1 ? 0 : 1
        }
      })
    }

    const selectionHandle = (val) => {
      data.selection = val
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
      statusHandle,
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
  