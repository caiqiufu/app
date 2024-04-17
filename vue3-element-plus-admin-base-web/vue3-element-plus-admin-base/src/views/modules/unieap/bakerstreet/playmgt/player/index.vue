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
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.playerName')" prop="name" width="200"
          sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.weixin')" prop="weixin" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.createDate')" prop="createDate"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.playScriptCount')" prop="playScriptCount"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.status')" prop="statusDesc" width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.playerType')" prop="playerType"
          width="160">
          <template v-slot="{ row }">
            <el-switch @change="playerTypeHandle(row)" v-model="row.playerType" active-value="2" inactive-value="1" />
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('unieap.bakerstreet.playmgt.blacklistFlag')" prop="blacklistFlag"
          width="160">
          <template v-slot="{ row }">
            <el-switch @change="blacklistHandle(row)" v-model="row.blacklistFlag" active-value="Y" inactive-value="N" />
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
import { playerPageApi, updateBlacklistFlagApi,updatePlayerTypeApi } from '@/api/unieap/bakerstreet/playmgt'

export default defineComponent({
  components: {},
  setup() {
    const { t } = useI18n()
    const refForm = ref()
    const refTable = ref()
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
      data.loading = true
      const params = {
        sort: data.form.sort,
        dir: data.form.dir,
        currentPage: page.current,
        pageSize: page.size,
        //查询参数
        playerType: '1',
        name: data.form.name,
        code: data.form.code
      }
      playerPageApi(params).then(r => {
        if (r) {
          data.list = r.data.list
          page.total = r.data.total
        }
        nextTick(() => { data.loading = false })
      })
    }

    //是否为DM
    const playerTypeHandle = (row) => {
      const params = row
      if (row.id) {
        updatePlayerTypeApi(params).then(r => {
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

    //加入黑名单
    const blacklistHandle = (row) => {
      const params = row
      if (row.id) {
        updateBlacklistFlagApi(params).then(r => {
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
      blacklistHandle,
      playerTypeHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
