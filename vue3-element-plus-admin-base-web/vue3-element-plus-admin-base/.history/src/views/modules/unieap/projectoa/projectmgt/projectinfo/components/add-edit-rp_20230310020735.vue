<template>
  <el-dialog width="800px" :title="$t('unieap.comm.edit')" v-model="visible" :close-on-click-modal="false"
    @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="100px" label-position="right">
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="12">
          <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="projectName">
            <el-input v-model="projectName" style="width:200px" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('unieap.projectoa.comm.jsdw')" prop="jsdw">
            <el-input v-model="jsdw" style="width:200px" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">{{ $t('unieap.projectoa.projectmgt.projectinfo.jsdw') }}</el-divider>
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="10">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="companyName">
            <el-input v-model="form.jsdw.companyName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32"
              style="width:300px" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.leaderName')" prop="leaderName">
            <el-input v-model="form.jsdw.leaderName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32"
              style="width:200px" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="phone">
            <el-input v-model="form.jsdw.phone" :placeholder="$t('unieap.comm.plsInput')" maxlength="32"
              style="width:200px" />
          </el-form-item>
        </el-col>
      </el-row>
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
import { parseDate2Str, parseStr2Date } from '@/utils'
import { useRouter } from 'vue-router'
//自定包引入
import { isNumber } from '@/unieap/utils/validate'
import { bizHandleApi } from '@/api/unieap/comm'
import { projectDicDataApi } from '@/api/unieap/projectoa/dic'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const router = useRouter()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/projectoa/projectmgt/projectinfo/'
    const data = reactive({
      loading: false,
      visible: false,
      addVisible: false,
      optionsProject: [],
      form: {
        projectId: '',
        jsdw: {
          companyType: '',
          companyName: '',
          leaderName: '',
          phone: ''
        },
        licenseNumber: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        projectId: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.projectCode')], trigger: 'blur' }],
        jzmj: [{ required: false, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.projectmgt.projectinfo.jzmj')], validator: isNumber, trigger: 'blur' }],
        planstartDate: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.projectmgt.projectinfo.planstartDate')], trigger: 'blur' }],
        planfinishDate: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.projectmgt.projectinfo.planfinishDate')], trigger: 'blur' }],
        duration: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.projectmgt.projectinfo.duration')], validator: isNumber, trigger: 'blur' }]
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      data.form.projectId = ''
      const dicProject = await projectDicDataApi({})
      if (dicProject) {
        data.optionsProject = dicProject.data
      }
      data.visible = true
      data.loading = true
      data.addVisible = true
      data.form.id = id
      data.form.planstartDate = new Date()
      if (id) {
        data.addVisible = false
        const params = {
          url: urlPrefix + 'info',
          'id': id
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
          if (!fieldValidate()) {
            return
          }
          //时间格式需要转换
          data.form.planstartDate = parseDate2Str(data.form.planstartDate)
          data.form.planfinishDate = parseDate2Str(data.form.planfinishDate)
          const handleName = data.form.id ? 'update' : 'create'
          const urlObj = {
            url: urlPrefix + handleName
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
    const fieldValidate = () => {
      if (data.form.planfinishDate !== '' && data.form.planstartDate !== '') {
        let endTime = data.form.planfinishDate.getTime()
        let startTime = data.form.planstartDate.getTime()
        if (endTime < startTime) {
          ElMessage({
            message: '[计划开工时间]不能晚于[计划开工时间]',
            type: 'error'
          })
          return false
        }
      }
      return true
    }

    const setDuration = () => {
      if (data.form.planfinishDate !== '' && data.form.planstartDate !== '') {
        let endTime = data.form.planfinishDate.getTime()
        let startTime = data.form.planstartDate.getTime()
        if (endTime > startTime) {
          let dates = Math.ceil((endTime - startTime) / (1000 * 60 * 60 * 24))
          data.form.duration = dates
        }
      }
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
      dialogClosedHandle,
      setDuration
    }
  }
})
</script>
