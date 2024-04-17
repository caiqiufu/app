<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="companyId">
        <el-select v-model="form.companyId.toString()" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsCompanyList" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.comm.gender')" prop="gender">
        <el-radio-group v-model="form.gender">
          <el-radio :label="'M'">{{ $t('unieap.comm.male') }}</el-radio>
          <el-radio :label="'F'">{{ $t('unieap.comm.fmale') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.position')" prop="position">
        <el-select v-model="form.position" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsPositionType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.idNumber')" prop="idNumber">
        <el-input v-model="form.idNumber" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="phone">
        <el-input v-model="form.phone" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.wx')" prop="wx">
        <el-input v-model="form.wx" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.certificateType')" prop="certificateTypeDesc">
        <el-select v-model="form.certificateType" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsCertificateType" :key="item.dicCode" :label="item.dicName"
            :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.certificateLicense')" prop="certificateLicense">
        <el-input v-model="form.certificateLicense" :placeholder="$t('unieap.comm.plsInput')" maxlength="32"
          show-word-limit style="width:300px" />
      </el-form-item>
      <el-row type="flex" justify="center">
        <el-button @click="visible = false">{{ $t('unieap.comm.cancel') }}</el-button>
        <el-button v-repeat type="primary" @click="submit()">{{ $t('unieap.comm.confirm') }}</el-button>
      </el-row>
      <el-row type="flex" justify="center">
        <br />
      </el-row>
    </el-form>
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
