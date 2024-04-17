<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.projectName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" style="width:300px" />
        {{ $t('unieap.projectoa.comm.squareMetres') }}
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.zhongbiaoinfo.bidAmount')" prop="bidAmount">
        <el-input v-model="form.bidAmount" :placeholder="$t('unieap.comm.plsInput')" maxlength="32"/>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.zhongbiaoinfo.bidDate')" prop="bidDate">
        <el-date-picker v-model="form.bidDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.jsdw')" prop="jsdw">
        <el-input v-model="form.jsdw" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.sgdw')" prop="sgdw">
        <el-input v-model="form.sgdw" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.planstartDate')" prop="planstartDate">
        <el-date-picker v-model="form.planstartDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.planfinishDate')" prop="planfinishDate">
        <el-date-picker v-model="form.planfinishDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.duration')" prop="duration">
        <el-input v-model="form.duration" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" style="width:300px"
          @focus="setDuration" />
        {{ $t('unieap.projectoa.comm.duration') }}
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.licenseNumber')" prop="licenseNumber">
        <el-input v-model="form.licenseNumber" :placeholder="$t('unieap.comm.plsInput')" maxlength="32"
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.jsyt')" prop="jsyt">
        <el-input type="textarea" :rows="3" v-model="form.jsyt" :placeholder="$t('unieap.comm.plsInput')"
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.jsdd')" prop="jsdd">
        <el-input type="textarea" :rows="3" v-model="form.jsdd" :placeholder="$t('unieap.comm.plsInput')"
          style="width:300px" />
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
        id: null,
        parentId: null,
        projectId:'',
        projectCode: '',
        projectName: '',
        jsyt: '',
        jsdd: '',
        jzmj: '',
        duration: '',
        planstartDate: '',
        planfinishDate: '',
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
