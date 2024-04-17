<template>
  <el-dialog width="500px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="80px" label-position="right">
      <el-form-item :label="$t('unieap.projectoa.comm.projectCode')" prop="code">
        <el-input v-model="form.code" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
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
      <el-form-item :label="$t('unieap.projectoa.comm.sgdw')" prop="companyId">
        <el-select v-model="form.companyId" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="item in optionsCompany" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
        <el-button type="primary" @click="addCompanyHandle()">
            <el-icon>
              <Plus />
            </el-icon>
            {{
              $t('unieap.projectoa.supervisionmgt.company.create')
            }}</el-button>
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
import { ElMessage} from 'element-plus'
import { parseDate2Str } from '@/utils'
import { useRouter } from 'vue-router'
//自定包引入
import {isPrice} from '@/unieap/utils/validate'
import {bizHandleApi } from '@/api/unieap/comm'
import {companyDicDataApi} from '@/api/unieap/projectoa/dic'

export default defineComponent({
  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const router = useRouter()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      optionsCompany: [],
      form: {
        id: null,
        code: '',
        name: '',
        bidAmount: '',
        bidDate: '',
        jsdw: '',
        companyId: '',
        sgdw:''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        code: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.projectCode')], trigger: 'blur' }],
        name: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.comm.projectName')], trigger: 'blur' }],
        bidAmount: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.projectmgt.zhongbiaoinfo.bidAmount')], validator: isPrice, trigger: 'blur' }],
        bidDate: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.projectmgt.zhongbiaoinfo.bidDate')], trigger: 'blur' }]
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      const dicCompany = await companyDicDataApi({})
      if (dicCompany) {
        data.optionsCompany = dicCompany.data
      }
      data.visible = true
      data.loading = true
      data.form.id = id
      data.form.bidDate = Date.now()
      if (id) {
        const params = {
          url: urlPrefix + 'info',
          'id': id
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
          //时间格式需要转换
          data.form.bidDate = parseDate2Str(data.form.bidDate)   
          const handleName = data.form.id ? 'update' : 'create'
          const params = Object.assign({url: urlPrefix + handleName},data.form)
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
    /**
     * 增加单位，路由到单位菜单
     */
    const addCompanyHandle = () => {
      //路由到其他菜单
      router.push({
        name: 'unieap-projectoa-supervisionmgt-company-index'
      })
    }
    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle,
      addCompanyHandle
    }
  }
})
</script>
