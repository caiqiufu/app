<template>
  <el-dialog width="800px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <template #header>
      <el-form ref="refForm" :inline="true">
        <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="projectName">
          <el-input v-model="projectName" style="width: 200px" disabled />
        </el-form-item>
        <el-form-item :label="$t('unieap.projectoa.comm.jsdw')" prop="jsdw">
          <el-input v-model="jsdw" style="width: 200px" disabled />
        </el-form-item>
        <el-form-item :label="$t('unieap.projectoa.attributemgt.attributeName')" prop="displayName">
          <el-input v-model="form.displayName" :placeholder="$t('unieap.comm.plsInput')" style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="reacquireHandle()">
            <el-icon>
              <Search />
            </el-icon>
            {{ $t("unieap.comm.search") }}</el-button>
          <el-button v-repeat @click="clearJson(form), reacquireHandle()">
            <el-icon>
              <Refresh />
            </el-icon>
            {{ $t("unieap.comm.reset") }}</el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-scrollbar style="height: 100%">
        <el-table ref="refTable" :data="dataList" v-loading="loading" :tree-props="props" row-key="id" border>
          <el-table-column align="left" :label="$t('unieap.projectoa.attributemgt.groupName')" prop="groupName"
            min-width="150" />
          <el-table-column align="left" :label="$t('unieap.projectoa.attributemgt.attributeName')" prop="displayName"
            min-width="260" />
          <el-table-column align="left" :label="$t('unieap.projectoa.attributemgt.attributeValue')" prop="attributeValue"
            min-width="240">
            <template v-slot="{ row }">
              <el-input v-if="row.displayName != null &&
                row.displayName != '' &&
                row.assigned === 'Y' &&
                row.uiType === 'input'
                " @blur="attributeValueEditHandle(row)" v-model="row.attributeValue">
                <template v-if="row.unit != null && row.unit != ''" v-slot:append>
                  {{ row.unit }}
                </template>
              </el-input>
              <el-input v-if="row.displayName != null &&
                row.displayName != '' &&
                row.assigned === 'Y' &&
                row.uiType === 'textarea'
                " type="textarea" :rows="3" @blur="attributeValueEditHandle(row)" v-model="row.attributeValue" />
              <el-select v-if="row.displayName != null &&
                row.displayName != '' &&
                row.assigned === 'Y' &&
                row.uiType === 'select'
                " @change="attributeValueEditHandle(row)" v-model="row.attributeValue">
                <el-option v-for="item in row.dicDataList" :key="item.dicCode" :label="item.dicName"
                  :value="item.dicCode" />
              </el-select>
              <el-date-picker v-if="row.displayName != null &&
                row.displayName != '' &&
                row.assigned === 'Y' &&
                row.uiType === 'textarea'
                " type="date" @blur="attributeValueEditHandle(row)" v-model="row.attributeValue" />
            </template>
          </el-table-column>
          <el-table-column align="left" :label="$t('unieap.projectoa.attributemgt.attributeSeq')" prop="seq" width="80" />
        </el-table>
      </el-scrollbar>
    </template>
  </el-dialog>
</template>

<script>
import { useI18n } from 'vue-i18n'
import {
  computed,
  defineComponent,
  nextTick,
  reactive,
  ref,
  toRefs
} from 'vue'
import { ElMessage } from 'element-plus'
import { clearJson } from '@/utils'
// 自定包引入
import { isNumber } from '@/unieap/utils/regular'
import { bizHandleApi } from '@/api/unieap/comm'
import {
  assignAttributeApi,
  updateSeqApi,
  updateAttributeValueApi
} from '@/api/unieap/projectoa/projectmgt/projectinfo'

export default defineComponent({
  components: {},
  setup() {
    const { t } = useI18n()
    const refForm = ref()
    // 数据定义
    const urlPrefix = '/backstage/projectoa/projectmgt/projectInfo/'
    const props = {
      children: 'children'
    }
    const data = reactive({
      loading: false,
      visible: false,
      editVisible: false,
      projectId: '',
      projectName: '',
      jsdw: '',
      form: {
        id: null,
        displayName: ''
      },
      dataList: []
    })
    const rules = computed(() => {
      return {}
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id
     */
    const init = async (id) => {
      data.visible = true
      data.loading = true
      data.projectId = id
      if (id) {
        getProject()
        getList()
      }
      nextTick(() => {
        data.loading = false
      })
    }
    // 获取项目信息
    const getProject = () => {
      const params = {
        url: '/backstage/common/' + 'commInfo',
        repositoryName: 'projectInfoRepository',
        beanName: 'projectoa.projectmgt.pojo.ProjectInfo',
        boName: '',
        id: data.projectId
      }
      bizHandleApi(params).then((r) => {
        if (r) {
          data.projectName = r.data.name
          data.jsdw = r.data.jsdw
        }
        nextTick(() => {
          data.loading = false
        })
      })
    }
    const getList = () => {
      if (data.projectId) {
        data.loading = true
        const params = {
          url: urlPrefix + 'projectAttributeList',
          // 查询参数
          projectId: data.projectId,
          displayName: data.form.displayName
        }
        bizHandleApi(params).then((r) => {
          if (r) {
            data.dataList = r.data
          }
          nextTick(() => {
            data.loading = false
          })
        })
      }
    }

    const attributeValueEditHandle = (row) => {
      const params = {
        projectAttributeId: row.projectAttributeId,
        attributeValue: row.attributeValue
      }
      if (row.projectAttributeId) {
        if (!fieldValidate(row)) {
          return
        }
        updateAttributeValueApi(params).then((r) => {
          if (r) {
            ElMessage({
              message: t('unieap.comm.optSuccess'),
              type: 'success'
            })
            // getList()
          }
        })
      }
    }

    const fieldValidate = (row) => {
      if (row.dataType === 'Number') {
        if (!isNumber(row.attributeValue)) {
          ElMessage({
            message: [row.displayName] + '是数字,请重新输入',
            type: 'error'
          })
          return false
        }
      }
      return true
    }

    const seqEditHandle = (row) => {
      const params = Object.assign({ projectId: data.projectId }, row)
      if (row.attributeId) {
        updateSeqApi(params).then((r) => {
          if (r) {
            ElMessage({
              message: t('unieap.comm.optSuccess'),
              type: 'success'
            })
            getList()
          }
        })
      }
    }
    const assignAttributeHandle = (row) => {
      const params = Object.assign({ projectId: data.projectId }, row)
      if (row.attributeId) {
        assignAttributeApi(params).then((r) => {
          if (r) {
            ElMessage({
              message: t('unieap.comm.optSuccess'),
              type: 'success'
            })
            getList()
          }
        })
      }
    }

    // 查询
    const reacquireHandle = () => {
      getList()
    }

    /**
     * @description: 弹窗关闭动画结束时的回调
     * @param {*}
     * @return {*}
     */
    const dialogClosedHandle = () => { }

    return {
      refForm,
      rules,
      props,
      ...toRefs(data),
      clearJson,
      init,
      getList,
      reacquireHandle,
      dialogClosedHandle,
      seqEditHandle,
      assignAttributeHandle,
      attributeValueEditHandle
    }
  }
})
</script>
