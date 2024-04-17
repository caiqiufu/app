<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.name')" prop="name"
          sortable='custom' />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.company.companyName')"
          prop="companyName" width="200" />
        <el-table-column align="center" :label="$t('unieap.comm.gender')" prop="genderDesc" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.position')"
          prop="positionDesc" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.idNumber')"
          prop="idNumber" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="phone" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.wx')" prop="wx" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.certificateType')"
          prop="certificateTypeDesc" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.certificateLicense')"
          prop="licenseNumber" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="200" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'supervisionmgt.employee:update'" type="primary" link
              @click="addEditHandle(row.id)">{{
                $t('unieap.comm.update')
              }}</el-button>
            <el-button v-permission="'supervisionmgt.employee:delete'" type="danger" link @click="deleteHandle(row.id)">{{
              $t('unieap.comm.delete')
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>
  </el-dialog>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
//自定包引入
import { infoDicDataApi, bizHandleApi } from '@/api/unieap/comm'
import { companyDicDataApi } from '@/api/unieap/projectoa/dic'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      optionsCompanyList: [],
      optionsPositionType: [],
      optionsCertificateType: [],
      form: {
        id: null,
        companyId: '',
        name: '',
        gender: '',
        avatarUrl: '',
        position: '',
        idNumber: '',
        phone:'',
        wx: '',
        email:'',
        certificateType:'',
        certificateLicense: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {}
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      companyDicDataApi({}).then(r => {
        if (r) {
          data.optionsCompanyList = r.data
        }
      })
      infoDicDataApi({ 'groupCode': 'EMPLOYEE_POSITION' }).then(r => {
        if (r) {
          data.optionsPositionType = r.data
        }
      })
      infoDicDataApi({ 'groupCode': 'CERTIFICATE_TYPE' }).then(r => {
        if (r) {
          data.optionsCertificateType = r.data
        }
      })
      data.visible = true
      data.loading = true
      data.form.id = id
      if (id) {
        const params = {
          url: urlPrefix + 'commInfo',
          'id': id,
          repositoryName: 'employeeInfoRepository',
          beanName: 'projectoa.supervisionmgt.pojo.EmployeeInfo',
          boName: '',
        }
        const r = await bizHandleApi(params)
        if (r) {
          data.form = r.data
          data.form.companyId = r.data.companyId.toString()
        }
      }
      nextTick(() => { data.loading = false })
    }

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
      refForm.value.validate(async valid => {
        if (valid) {
          const handleType = data.form.id ? 'update' : 'create'
          const mparams = {
            url: urlPrefix + 'commDeal',
            'id': data.form.id,
            operType: handleType,
            repositoryName: 'employeeInfoRepository',
            beanName: 'projectoa.supervisionmgt.pojo.EmployeeInfo',
            boName: ''
          }
          const params = Object.assign(mparams, data.form)
          const r = await bizHandleApi(params)
          if (r) {
            data.visible = false
            ElMessage({
              message: t('unieap.comm.optSuccess'),
              type: 'success'
            })
            emit('refresh')
          }
        }
      })
    }

    /**
   * @description: 弹窗关闭动画结束时的回调
   * @param {*}
   * @return {*}
   */
    const dialogClosedHandle = () => {
      refForm.value.resetFields()
    }

    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle
    }
  }
})
</script>
