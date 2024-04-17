<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item :label="$t('unieap.bakerstreet.playmgt.playerName')" prop="name">
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
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.id')" prop="code" sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.dmplayerName')" prop="name" width="200"
          sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.activeCode')" prop="activeCode" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.activeDate')" prop="activeDate"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.weixin')" prop="weixin" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.createDate')" prop="createDate"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.dmplayScriptCount')"
          prop="dmplayScriptCount" width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="160" fixed="right">
          <template v-slot="{ row }">
            <div>
              {{
                $t('unieap.bakerstreet.playmgt.dmPermission')
              }}
              <el-switch @change="scriptFlagHandle(row)" v-model="row.scriptFlag" active-value="Y" inactive-value="N" />
            </div>
            <div>
              {{
                $t('unieap.bakerstreet.playmgt.change2Player')
              }}
              <el-switch @change="changeFlagHandle(row)" v-model="row.changeFlag" active-value="Y" inactive-value="N" />
            </div>
          </template>
        </el-table-column>
      </el-table>
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
import { bizHandleApi } from '@/api/unieap/comm'
import { updateScriptFlagApi, updateChangeFlagApi } from '@/api/unieap/bakerstreet/playmgt'

export default defineComponent({
  components: {},
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
        repositoryName: 'playerInfoRepository',
        beanName: 'bakerstreet.playmgt.vo.PlayerInfoVO',
        boName: '',
        playerType:'2',
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

    //开本权限
    const scriptFlagHandle = (row) => {
      const params = row
      if (row.id) {
        updateScriptFlagApi(params).then(r => {
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

    //回到玩家
    const changeFlagHandle = (row) => {
      const params = row
      if (row.id) {
        updateChangeFlagApi(params).then(r => {
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

    //查询
    const reacquireHandle = () => {
      page.current = 1
      getList()
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
      page,
      ...toRefs(data),
      sortChange,
      selectionHandle,
      clearJson,
      pageChangeHandle,
      //自定方法
      getList,
      reacquireHandle,
      scriptFlagHandle,
      changeFlagHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
