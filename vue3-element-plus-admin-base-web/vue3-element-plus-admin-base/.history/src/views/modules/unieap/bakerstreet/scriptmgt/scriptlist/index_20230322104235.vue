<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.scriptName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.type')">
          <el-select v-model="form.type" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionScriptType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.category')">
          <el-select v-model="form.category" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionCategory" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
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
          <el-button v-permission="'scriptmgt:createScript'" type="primary" @click="addEditHandle()">
            <el-icon>
              <Plus />
            </el-icon>
            {{
              $t('unieap.bakerstreet.scriptmgt.createScript')
            }}</el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.id')" prop="code" sortable='custom' width="80" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.poster')" prop="logo" width="100">
          <template v-slot="{ row }">
            <el-avatar :size="50" :src="row.posterUrl" v-if="row.posterUrl" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.scriptName')" prop="name"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.type')" prop="typeListDesc"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.category')" prop="categoryDesc"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.duration')" prop="durationDesc"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.playerCount')" prop="playerCountDesc"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.publishFlag')" prop="publishFlag"
          width="160">
          <template v-slot="{ row }">
            <el-switch @change="publishHandle(row)" v-model="row.publishFlag" active-value="1" inactive-value="0" />
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.publishDate')" prop="publishDate"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.createDate')" prop="createDate"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="200" fixed="right">
          <template v-slot="{ row }">
            <el-button type="primary" link @click="addEditDMHandle(row.id)">{{
              $t('unieap.bakerstreet.scriptmgt.editScriptDM')
            }}</el-button>
            <el-button type="primary" link @click="addEditHandle(row.id)">{{
              $t('unieap.comm.edit')
            }}</el-button>
            <el-button type="primary" link @click="deleteHandle(row.id)">{{
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
import { ElMessage, ElMessageBox } from 'element-plus'
import usePage from '@/mixins/page'
import { clearJson, havePermission } from '@/utils'
import { useRouter } from 'vue-router'
//自定包引入
import AddEdit from './components/add-edit'
import { bizHandleApi, infoDicDataApi } from '@/api/unieap/comm'
import { updatePublishFlagApi, scriptPageApi } from '@/api/unieap/bakerstreet/scriptmgt'

export default defineComponent({
  components: { AddEdit },
  setup() {
    const { t } = useI18n()
    const refAddEdit = ref()
    const refForm = ref()
    const refTable = ref()
    const router = useRouter()
    const { page } = usePage()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sort: 'id',
        dir: 'asc',
        name: '',
        type: '',
        category:''
      },
      list: [],
      selection: [],
      optionScriptType: [],
      optionCategory: []
    })
    //分页数据,根据需求修改
    const getList = () => {
      data.loading = true
      const params = {
        sort: data.form.sort,
        dir: data.form.dir,
        currentPage: page.current,
        pageSize: page.size,
        //查询参数
        name: data.form.name,
        typeList: data.form.type,
        category: data.form.category
      }
      scriptPageApi(params).then(r => {
        if (r) {
          data.list = r.data.list
          page.total = r.data.total
        }
        nextTick(() => { data.loading = false })
      })
    }

    const scriptTypeOptions = async () => {
      const dicScriptType = await infoDicDataApi({ 'groupCode': 'SCRIPT_TYPE' })
      if (dicScriptType) {
        data.optionScriptType = dicScriptType.data
      }
    }

    const categoryOptions = async () => {
      const dicCategory = await infoDicDataApi({ 'groupCode': 'SCRIPT_CATEGORY' })
      if (dicCategory) {
        data.optionCategory = dicCategory.data
      }
    }

    //查询
    const reacquireHandle = () => {
      page.current = 1
      getList()
    }
    //编辑
    const addEditDMHandle = (id) => {
      data.visible = true
      nextTick(() => {
        refAddEdit.value.init(id)
      })
    }
    const addEditHandle = (id) => {
      //路由到剧本编辑菜单
      //router.push({
      //  name: 'unieap-bakerstreet-scriptmgt-scriptedit-index',
      //  params:{scriptId:'1'}
      //})
      router.push({
        path: 'unieap-bakerstreet-scriptmgt-scriptedit-index',
        query: { scriptId: id }
      })
    }
    //删除,根据需求修改
    const deleteHandle = (id) => {
      const ids = id ? [id] : data.selection.map(item => item.id)
      const keys = ids.join(",")
      ElMessageBox.confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const params = {
          url: urlPrefix + 'commDeal',
          'id': id,
          keys: keys,
          operType: 'delete',
          repositoryName: 'scriptInfoRepository',
          beanName: 'bakerstreet.scriptmgt.pojo.ScriptInfo',
          boName: '',
        }
        bizHandleApi(params).then(r => {
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
    const publishHandle = (row) => {
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
      scriptTypeOptions()
      categoryOptions()
      getList()
    })

    return {
      refForm,
      refAddEdit,
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
      addEditDMHandle,
      addEditHandle,
      deleteHandle,
      publishHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
