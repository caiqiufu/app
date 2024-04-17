<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.parentProject')" prop="parentId">
        <el-select v-model="form.parentId" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsProjectList" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.projectType')" prop="type">
        <el-select v-model="form.type" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsProjectType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.bidAmount')" prop="bidAmount">
        <el-input v-model="form.bidAmount" :placeholder="$t('unieap.comm.plsInput')" maxlength="32"
          @change="bidAmountChanged">
          <template v-slot:append>万元</template>
        </el-input>
        {{ bidAmountUppercase }}
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.bidDate')" prop="bidDate">
        <el-date-picker v-model="form.bidDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.jsdw')" prop="jsdw">
        <el-input v-model="form.jsdw" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.sgdw')" prop="sgdw">
        <el-input v-model="form.sgdw" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.planstartDate')" prop="planstartDate">
        <el-date-picker v-model="form.planstartDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.planfinishDate')" prop="planfinishDate">
        <el-date-picker v-model="form.planfinishDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.duration')" prop="duration">
        <el-input v-model="form.duration" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" style="width:300px"
          @focus="setDuration">
          <template v-slot:append>天</template>
        </el-input>
      </el-form-item>

      <el-row type="flex" justify="center">
        <el-form-item label-width="0px" >
          <el-button @click="visible = false">{{ $t('unieap.comm.cancel') }}</el-button>
          <el-button v-repeat type="primary" @click="submit()">{{ $t('unieap.comm.confirm') }}</el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
import { parseDate2Str } from '@/utils'
import { clearJson } from '@/utils'
//自定包引入
import { isNumber } from '@/unieap/utils/validate'
import { digitUppercase } from '@/unieap/utils/priceutils'
import { infoDicDataApi, bizHandleApi } from '@/api/unieap/comm'
import { projectDicDataApi } from '@/api/unieap/projectoa/dic'

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
      optionsProjectList: [],
      optionsProjectType: [],
      bidAmountUppercase: '',
      form: {
        id: null,
        parentId: null,
        type: '',
        code: '',
        name: '',
        bidAmount: '',
        bidDate: '',
        jsdw: '',
        sgdw: '',
        planstartDate: '',
        planfinishDate: '',
        duration: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.projectName')], trigger: 'blur' }],
        type: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.projectType')], trigger: 'blur' }],
        bidAmount: [{ required: false, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.bidAmount')], validator: isNumber, trigger: 'blur' }],
        planstartDate: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.planstartDate')], trigger: 'blur' }],
        planfinishDate: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.planfinishDate')], trigger: 'blur' }],
        duration: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.duration')], validator: isNumber, trigger: 'blur' }]
      }
    })

    const bidAmountChanged = (value) => {
      data.bidAmountUppercase = digitUppercase(value)
    }

    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      clearJson(data.form)
      data.visible = true
      data.loading = true
      data.form.id = id
      projectDicDataApi({}).then(r => {
        if (r) {
          data.optionsProjectList = r.data
        }
      })
      infoDicDataApi({ 'groupCode': 'PROJECT_TYPE' }).then(r => {
        if (r) {
          data.optionsProjectType = r.data
        }
      })
      data.form.planstartDate = new Date()
      if (id) {
        const params = {
          repositoryName: 'projectInfoRepository',
          beanName: 'projectoa.projectmgt.pojo.ProjectInfo',
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
          if (!fieldValidate()) {
            return
          }
          //时间格式需要转换
          data.form.bidDate = parseDate2Str(data.form.bidDate)
          data.form.planstartDate = parseDate2Str(data.form.planstartDate)
          data.form.planfinishDate = parseDate2Str(data.form.planfinishDate)
          const handleName = 'commDeal'
          const operType = data.form.id ? 'update' : 'create'
          const urlObj = {
            repositoryName: 'projectInfoRepository',
            beanName: 'projectoa.projectmgt.pojo.ProjectInfo',
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
    const fieldValidate = () => {
      if (data.form.planfinishDate !== '' && data.form.planstartDate !== '') {
        
        let endTime = new Date(data.form.planfinishDate).getTime()
        let startTime = new Date(data.form.planstartDate).getTime()
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
        let endTime = new Date(data.form.planfinishDate).getTime()
        let startTime = new Date(data.form.planstartDate).getTime()
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
      bidAmountChanged,
      init,
      submit,
      dialogClosedHandle,
      setDuration
    }
  }
})
</script>
