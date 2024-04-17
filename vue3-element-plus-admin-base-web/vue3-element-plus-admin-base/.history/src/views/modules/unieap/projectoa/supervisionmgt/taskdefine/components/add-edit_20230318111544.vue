<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item label="任务名称" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit style="width:300px"/>
      </el-form-item>
      <el-form-item label="任务类型" prop="type">
        <el-select v-model="form.type" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionTaskType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-input v-model="form.priority" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" style="width:300px"/>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.businessLicense')" prop="businessLicense">
        <el-input v-model="form.businessLicense" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit style="width:300px"/>
      </el-form-item>
      <el-form-item label="是否提醒" prop="notifyFlag">
        <el-select v-model="form.notifyFlag" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionNotifyFlag" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.address')" prop="address">
        <el-input type="textarea" :rows="3" v-model="form.address" :placeholder="$t('unieap.comm.plsInput')" style="width:300px"/>
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
import { infoDicDataApi,bizHandleApi } from '@/api/unieap/comm'
//自定包引入


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
      optionsCompanyType: [],
      form: {
        id: null,
        name: '',
        type: '',
        businessCorporation: '',
        businessLicense:'',
        address: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.supervisionmgt.company.companyName')], trigger: 'blur' }],
        type: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.supervisionmgt.company.type')], trigger: 'blur' }],
        businessCorporation: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.supervisionmgt.company.businessCorporation')], trigger: 'blur' }],
        businessLicense: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.supervisionmgt.company.businessLicense')], trigger: 'blur' }],
        address: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.supervisionmgt.company.address')], trigger: 'blur' }]
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      const dicCompanyType = await infoDicDataApi({ 'groupCode': 'COMPANY_TYPE' })
      if (dicCompanyType) {
        data.optionsCompanyType = dicCompanyType.data
      }
      data.visible = true
      data.loading = true
      data.form.id = id
      if (id) {
        const params = {
          repositoryName: 'companyInfoRepository',
          beanName: 'projectoa.supervisionmgt.pojo.CompanyInfo',
          boName: '',
          url: urlPrefix + 'commInfo',
          'id': id
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
          const handleName = 'commDeal'
          const operType = data.form.id ? 'update' : 'create'
          const urlObj = {
            repositoryName: 'companyInfoRepository',
            beanName: 'projectoa.supervisionmgt.pojo.CompanyInfo',
            url: urlPrefix + handleName,
            operType: operType
          }
          const params = Object.assign(urlObj, data.form)
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
