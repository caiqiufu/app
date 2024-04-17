<template>
  <Container>
    <template #header>
      <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px"
        label-position="right">
        <el-form-item style="text-align: center">
          <el-button type="primary" @click="handlePrev">上一步，填写基本信息</el-button>
          <el-button type="primary" :disabled="list.length <= 0" @click="handleNext">下一步，填写章节信息</el-button>
          <el-button type="primary" @click="addEditHandle({ id: '', scriptId: '' })">
            <el-icon>
              <Plus />
            </el-icon>
            {{
              $t('unieap.bakerstreet.scriptmgt.createRole')
            }}</el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.id')" prop="code" sortable='custom' width="80" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.rolePicture')" prop="url" width="100">
          <template v-slot="{ row }">
            <el-avatar :size="50" :src="row.avatarUrl" v-if="row.avatarUrl" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.roleName')" prop="name" width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.gender')" prop="sexDesc" width="100" />
        <el-table-column align="center" :label="$t('unieap.bakerstreet.scriptmgt.brief')" prop="brief" width="100" />
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
  </Container>
</template>
<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, onBeforeMount, nextTick, reactive, ref, toRefs } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
//自定包引入
import AddEdit from './add-edit-role'
import { rolePageApi } from '@/api/unieap/bakerstreet/scriptmgt'
import { bizHandleApi } from '@/api/unieap/comm'

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
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: true,
      isEdit: props['props'],
      scriptId: props['scriptId'],
      form: {
        id: null,
        scriptId: '',
        code: '',
        name: '',
        sex: '',
        brief: '',
        url: ''
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
          //查询参数
          id: data.scriptId
        }
        rolePageApi(params).then(r => {
          if (r) {
            data.list = r.data.list
          }
        })
        nextTick(() => { data.loading = false })
      }
    }
    //编辑
    const addEditHandle = (e) => {
      e.scriptId = data.scriptId
      nextTick(() => {
        refAddEdit.value.init(e)
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
          repositoryName: 'roleInfoRepository',
          beanName: 'bakerstreet.scriptmgt.pojo.RoleInfo',
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
      refAddEdit,
      refTable,
      ...toRefs(data),
      rules,
      init,
      selectionHandle,
      sortChange,
      getList,
      handlePrev,
      handleNext,
      addEditHandle,
      deleteHandle
    }
  }
})
</script>
