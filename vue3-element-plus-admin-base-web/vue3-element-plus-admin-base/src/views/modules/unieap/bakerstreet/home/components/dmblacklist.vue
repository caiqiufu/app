<template>
  <Container>
    <template #header>
      <div class="panel-title margin_b-10 font-size-18">{{ $t('unieap.bakerstreet.playmgt.dmblacklist') }}</div>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list"
        @selection-change="selectionHandle" @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.dmName')" prop="name"
          width="200" />
      </el-table>
    </template>
  </Container>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { defineComponent, nextTick, onBeforeMount, reactive, ref, toRefs } from 'vue'
import { clearJson, havePermission } from '@/utils'
//自定包引入
import { blacklistPageApi} from '@/api/unieap/bakerstreet/playmgt'

export default defineComponent({
  components: {},
  setup() {
    const { t } = useI18n()
    const refForm = ref()
    const refTable= ref()
    //数据定义
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        sort: 'id',
        dir: 'asc'
      },
      list: [],
      selection: []
    })
    //分页数据,根据需求修改
    const getList = () => {
      data.loading = true
      const params = {
        playerType:'2'
      }
      blacklistPageApi(params).then(r => {
        if (r) {
          data.list = r.data.list
        }
        nextTick(() => { data.loading = false })
      })
    }
    //查询
    const reacquireHandle = () => {
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

    onBeforeMount(() => {
      getList()
    })

    return {
      refForm,
      refTable,
      ...toRefs(data),
      sortChange,
      selectionHandle,
      clearJson,
      havePermission,
      //自定方法
      getList,
      reacquireHandle
    }
  }
})
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
