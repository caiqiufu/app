<template>
  <Container>
    <template #header>
      <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px"
        label-position="right">
        <el-form-item style="text-align: center">
          <el-button type="primary" @click="handlePrev">上一步，填写章节信息</el-button>
          <el-button type="primary" @click="addEditHandle({ id: '', scriptId: '' })">
            <el-icon>
              <Plus />
            </el-icon>
            {{
              $t('unieap.bakerstreet.scriptmgt.createClue')
            }}</el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.id')" prop="code" sortable='custom' width="80" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.clueName')" prop="clueName"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.cluePicture')" prop="url" width="100">
          <template v-slot="{ row }">
            <el-image :src="row.url" v-if="row.url" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.brief')" prop="name" width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.createDate')" prop="createDate" width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="200" fixed="right">
          <template v-slot="{ row }">
            <el-button type="primary" link @click="addEditHandle(row)">{{
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
import { computed, defineComponent, onBeforeMount, nextTick, reactive, ref, toRefs } from 'vue'
import usePage from '@/mixins/page'
//自定包引入
import AddEdit from './add-edit-clue'
import { clueDetailPageApi } from '@/api/unieap/bakerstreet/scriptmgt'

export default defineComponent({
  components: { AddEdit },
  props: {
    //value: Object,//组件v-model中绑定的变量传递
    isEdit: {
      type: Boolean,
      default: false
    },
    scriptId: {
      type: String,
      default: () => ''
    }
  },
  emits: ['refresh', 'prevStep', 'nextStep'],
  setup(props, { emit }) {
    const { t } = useI18n()
    const refAddEdit = ref()
    const refForm = ref()
    const refTable = ref()
    const { page } = usePage()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      isEdit: props['props'],
      scriptId: props['scriptId'],
      form: {
        sort: 'id',
        dir: 'asc',
        id: null,
        clueId: '',
        clueName: '',
        url: '',
        brief: '',
        seq: '',
        createDate: ''
      },
      list: [],
      selection: [],
      roles: []
    })
    const rules = computed(() => {
      return {}
    })
    const handlePrev = () => {
      emit('prevStep')
    }
    const handleNext = () => {
      emit('nextStep')
    }
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (e) => {
      data.scriptId = e.scriptId
      data.isEdit = e.isEdit
      getList()
    }
    const getList = () => {
      if (data.scriptId) {
        data.loading = true
        const params = {
          sort: data.form.sort,
          dir: data.form.dir,
          currentPage: page.current,
          pageSize: page.size,
          //查询参数
          id: data.scriptId
        }
        clueDetailPageApi(params).then(r => {
          if (r) {
            data.list = r.data.list
          }
          nextTick(() => { data.loading = false })
        })
      }
    }
    //编辑
    const addEditHandle = (e) => {
      data.visible = true
      e.scriptId = data.scriptId
      //e.scriptId = 1263
      nextTick(() => {
        refAddEdit.value.init(e)
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
      refAddEdit,
      refTable,
      ...toRefs(data),
      rules,
      init,
      selectionHandle,
      pageChangeHandle,
      sortChange,
      getList,
      handlePrev,
      handleNext,
      addEditHandle
    }
  }
})
</script>
