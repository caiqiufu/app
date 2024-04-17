<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-row>
          <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.searchDM')" prop="playerId">
            <el-select v-model="form.playerId" filterable remote reserve-keyword
              :placeholder="$t('unieap.comm.plsInput')" :remote-method="remoteMethod" :loading="loading"
              @change="selectedItemHandle(form.playerId)">
              <el-option v-for="item in optionsDMList" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
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
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.dmName')" prop="playerId">
            <el-input v-model="form.playerId" style="width:200px" disabled />
          </el-form-item>
          <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.dmName')" prop="playerName">
            <el-input v-model="form.playerName" style="width:200px" disabled />
          </el-form-item>
        </el-row>
      </el-form>
    </template>
    <template #default>
      <el-row>
        <el-col :span="12">
          <el-table key="DMPlay" ref="refTableDMPlay" v-loading="loading" :data="listDMPlay"
            @selection-change="selectionHandle" @sort-change="sortChange" border>
            <el-table-column align="center" type="selection" width="50" />
            <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.scriptName')" prop="scriptName"
              width="200" />
            <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.dmPlayTime')" prop="dmPlayTime"
              width="230" />
          </el-table>
        </el-col>
        <el-col :span="12">
          <el-table key="DMBrowse" ref="refTableDMBrowse" v-loading="loading" :data="listDMBrowse"
            @selection-change="selectionHandle" @sort-change="sortChange" border>
            <el-table-column align="center" type="selection" width="50" />
            <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.scriptName')" prop="scriptName"
              width="200" />
            <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.dmBrowseTime')" prop="dmPlayTime"
              width="230" />
          </el-table>
        </el-col>
      </el-row>
    </template>
  </Container>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { defineComponent, nextTick, onBeforeMount, reactive, ref, toRefs } from 'vue'
import { clearJson, havePermission } from '@/utils'
//自定包引入
import { dmPlayPageApi, dmBrowsePageApi, playerDicApi } from '@/api/unieap/bakerstreet/playmgt'

export default defineComponent({
  components: {},
  setup() {
    const { t } = useI18n()
    const refForm = ref()
    const refTableDMPlay = ref()
    const refTableDMBrowse = ref()
    //数据定义
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sort: 'id',
        dir: 'asc',
        playerId: '',
        playerName: '',
      },
      listDMPlay: [],
      listDMBrowse: [],
      optionsDMList: [],
      selection: []
    })
    //分页数据,根据需求修改
    const getList = () => {
      data.loading = true
      const params = {
        code: data.form.playerId
      }
      dmPlayPageApi(params).then(r => {
        if (r) {
          data.listDMPlay = r.data.list
        }
        nextTick(() => { data.loading = false })
      })
      dmBrowsePageApi(params).then(r => {
        if (r) {
          data.listDMBrowse = r.data.list
        }
        nextTick(() => { data.loading = false })
      })
    }
    //查询
    const reacquireHandle = () => {
      getList()
    }

    const remoteMethod = (query) => {
      if (query !== '') {
        data.loading = true
        const params = {
          name: query
        }
        playerDicApi(params).then(r => {
          if (r) {
            data.optionsDMList = r.data
          }
          nextTick(() => { data.loading = false })
        })
      }
    }

    const selectedItemHandle = (playerId) => {
      data.optionsDMList.forEach(function (value) {
        if (value.dicCode === playerId) {
          data.form.playerId = value.dicCode
          data.form.playerName = value.dicName
        }
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

    onBeforeMount(() => {
      getList()
    })

    return {
      refForm,
      refTableDMPlay,
      refTableDMBrowse,
      ...toRefs(data),
      sortChange,
      selectionHandle,
      clearJson,
      havePermission,
      //自定方法
      getList,
      reacquireHandle,
      remoteMethod,
      selectedItemHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
