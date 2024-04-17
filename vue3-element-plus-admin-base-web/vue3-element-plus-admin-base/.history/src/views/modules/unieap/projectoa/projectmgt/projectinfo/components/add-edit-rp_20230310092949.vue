<template>
  <el-dialog width="800px" :title="$t('unieap.comm.edit')" v-model="visible" :close-on-click-modal="false"
    @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="80px" label-position="right">
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="12">
          <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="projectName">
            <el-input v-model="projectName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('unieap.projectoa.comm.jsdw')" prop="jsdwName">
            <el-input v-model="jsdwName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">{{ $t('unieap.projectoa.projectmgt.projectinfo.jsdw') }}</el-divider>
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="10">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="jsdw.companyName">
            <el-input v-model="form.jsdw.companyName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.leaderName')" prop="jsdw.leaderName">
            <el-input v-model="form.jsdw.leaderName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="jsdwPhone">
            <el-input v-model="form.jsdw.phone" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">{{ $t('unieap.projectoa.projectmgt.projectinfo.sgdw') }}</el-divider>
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="10">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="sgdwCompanyName">
            <el-input v-model="form.sgdw.companyName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.leaderName')" prop="sgdwLeaderName">
            <el-input v-model="form.sgdw.leaderName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="sgdwPhone">
            <el-input v-model="form.sgdw.phone" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">{{ $t('unieap.projectoa.projectmgt.projectinfo.sjdw') }}</el-divider>
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="10">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="sjdwCompanyName">
            <el-input v-model="form.sjdw.companyName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.leaderName')" prop="sjdwLeaderName">
            <el-input v-model="form.sjdw.leaderName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="sjdwPhone">
            <el-input v-model="form.sjdw.phone" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">{{ $t('unieap.projectoa.projectmgt.projectinfo.kcdw') }}</el-divider>
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="10">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="kcdwCompanyName">
            <el-input v-model="form.kcdw.companyName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.leaderName')" prop="kcdwLeaderName">
            <el-input v-model="form.kcdw.leaderName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="kcdwPhone">
            <el-input v-model="form.kcdw.phone" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">{{ $t('unieap.projectoa.projectmgt.projectinfo.jldw') }}</el-divider>
      <el-row :gutter="10" type="flex" justify="start">
        <el-col :span="10">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.company.companyName')" prop="jldwCompanyName">
            <el-input v-model="form.jldw.companyName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.projectmgt.projectinfo.leaderName')" prop="jldwLeaderName">
            <el-input v-model="form.jldw.leaderName" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="jldwPhone">
            <el-input v-model="form.jldw.phone" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" />
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
    const urlPrefix = '/backstage/projectoa/projectmgt/projectInfo/'
    const data = reactive({
      loading: false,
      visible: false,
      optionsProject: [],
      projectId: '',
      projectName: '',
      jsdwName: '',
      form: {
        jsdw: {
          companyType: '',
          companyName: '',
          leaderName: '',
          phone: ''
        },
        sgdw: {
          companyType: '',
          companyName: '',
          leaderName: '',
          phone: ''
        },
        sjdw: {
          companyType: '',
          companyName: '',
          leaderName: '',
          phone: ''
        },
        kcdw: {
          companyType: '',
          companyName: '',
          leaderName: '',
          phone: ''
        },
        jldw: {
          companyType: '',
          companyName: '',
          leaderName: '',
          phone: ''
        }
      },
      roles: []
    })
    const rules = computed(() => {
      return {
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      data.projectId = id
      const dicProject = await projectDicDataApi({})
      if (dicProject) {
        data.optionsProject = dicProject.data
      }
      data.visible = true
      data.loading = true
      if (id) {
        getProject()
        getList()
      }
      nextTick(() => { data.loading = false })
    }
    //获取项目信息
    const getProject = () => {
      const params = {
        url: '/backstage/common/' + 'commInfo',
        repositoryName: 'projectInfoRepository',
        beanName: 'projectoa.projectmgt.pojo.ProjectInfo',
        boName: '',
        'id': data.projectId
      }
      bizHandleApi(params).then(r => {
        if (r) {
          data.projectName = r.data.name
          data.jsdwName = r.data.jsdw
        }
        nextTick(() => { data.loading = false })
      })
    }
    const getList = () => {
      if (data.projectId) {
        data.loading = true
        const params = {
          url: urlPrefix + 'getProjectLeaderList',
          //查询参数
          'projectId': data.projectId
        }
        bizHandleApi(params).then(r => {
          if (r) {
            if(r.data.jsdw){
              data.form.jsdw = r.data.jsdw
            }
            if(r.data.kcdw){
              data.form.kcdw = r.data.kcdw
            }
            if(r.data.sjdw){
              data.form.sjdw = r.data.sjdw
            }
            if(r.data.jldw){
              data.form.jldw = r.data.jldw
            }
            if(r.data.sgdw){
              data.form.sgdw = r.data.sgdw
            }
          }
          nextTick(() => { data.loading = false })
        })
      }
    }
    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
      refForm.value.validate(async valid => {
        if (valid) {
          const urlObj = {
            type: 'data',
            url: urlPrefix + 'updateProjectLeader'
          }
          const params = Object.assign(urlObj, data.form, { 'projectId': data.projectId })
          bizHandleApi(params).then(r => {
            if (r) {
              data.visible = false
              ElMessage({
                message: t('unieap.comm.optSuccess'),
                type: 'success'
              })
              emit('refresh')
            }
          })
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
