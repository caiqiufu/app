<template>
  <el-dialog width="800px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="100px" label-position="right">
      <el-row>
        <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="projectName">
          <el-input v-model="form.projectName" style="width:200px" disabled />
        </el-form-item>
        <el-form-item label="日期" prop="logTime">
          <el-input v-model="form.logTime" style="width:200px" disabled />
        </el-form-item>
      </el-row>
      <el-divider content-position="left">天气情况</el-divider>
      <el-row>
        <el-col :span="6">
          <el-form-item label="最高气温" prop="tempMax">
            <el-input v-model="form.tempMax" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="最低气温" prop="tempMin">
            <el-input v-model="form.tempMin" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="上午天气" prop="weatherMorning">
            <el-input v-model="form.weatherMorning" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="下午天气" prop="weatherAfternoon">
            <el-input v-model="form.weatherAfternoon" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">施工情况</el-divider>
      <el-row>
        <el-form-item :label="$t('unieap.projectoa.comm.sgdw')" prop="sgdw">
          <el-input v-model="form.sgdw" style="width:600px" disabled />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="施工部位" prop="sgbw">
          <el-input type="textarea" :rows="3" v-model="form.sgbw" style="width:600px"/>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="施工内容" prop="sglr">
          <el-input type="textarea" :rows="3" v-model="form.sglr" style="width:600px"/>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="施工情况" prop="sgqk">
          <el-input type="textarea" :rows="3" v-model="form.sgqk" style="width:600px"/>
        </el-form-item>
      </el-row>
      <el-divider content-position="left">监理巡检记录</el-divider>
      <el-row>
        <el-form-item label="施工情况" prop="xjjl">
          <el-input type="textarea" :rows="10" v-model="form.xjjl" style="width:600px"/>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="旁站项目及旁站记录情况" prop="pzjj">
          <el-input v-model="form.pzjj" style="width:520px"  />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="见证项目及见证记录情况" prop="jzjl">
          <el-input v-model="form.jzjl" style="width:520px"  />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="旁站项目及旁站记录情况" prop="pzjj">
          <el-input v-model="form.pzjj" style="width:520px"  />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="旁站项目及旁站记录情况" prop="pzjj">
          <el-input v-model="form.pzjj" style="width:520px"  />
        </el-form-item>
      </el-row>

      

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
import { infoDicDataApi, bizHandleApi } from '@/api/unieap/comm'
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
        businessLicense: '',
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
