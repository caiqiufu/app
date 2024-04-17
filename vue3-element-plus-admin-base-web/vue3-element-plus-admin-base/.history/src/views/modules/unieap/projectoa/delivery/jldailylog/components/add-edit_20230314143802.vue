<template>
  <el-dialog width="800px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="120px" label-position="right">
      <el-row>
        <el-form-item :label="$t('unieap.projectoa.comm.projectName')" prop="projectName">
          <el-select :disabled="disEdit" v-model="form.projectId" :placeholder="$t('unieap.comm.plsSelect')"
            @change="selectedItemHandle(form.projectId)">
            <el-option v-for="item in optionsProjectList" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('unieap.projectoa.comm.sgdw')" prop="sgdw">
          <el-input disabled v-model="form.sgdw" style="width:600px" />
        </el-form-item>
        <el-form-item label="日期" prop="logTime">
          <el-date-picker :disabled="disEdit" v-model="form.logTime" type="date"
            :placeholder="$t('unieap.comm.plsSelect')">
          </el-date-picker>
        </el-form-item>
      </el-row>
      <el-divider content-position="left">天气情况</el-divider>
      <el-row>
        <el-col :span="6">
          <el-form-item label-width="80px" label="最高气温" prop="tempMax">
            <el-input v-model="form.tempMax">
              <template v-slot:append>{{ $t('unieap.projectoa.comm.template') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label-width="80px" label="最低气温" prop="tempMin">
            <el-input v-model="form.tempMin">
              <template v-slot:append>{{ $t('unieap.projectoa.comm.template') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label-width="80px" label="上午天气" prop="weatherMorning">
            <el-select v-model="form.weatherMorning" :placeholder="$t('unieap.comm.plsSelect')">
              <el-option v-for="item in optionsWeatherType" :key="item.dicCode" :label="item.dicName"
                :value="item.dicCode">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label-width="80px" label="下午天气" prop="weatherAfternoon">
            <el-select v-model="form.weatherAfternoon" :placeholder="$t('unieap.comm.plsSelect')">
              <el-option v-for="item in optionsWeatherType" :key="item.dicCode" :label="item.dicName"
                :value="item.dicCode">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">施工情况</el-divider>
      <el-row>
        <el-form-item label-width="80px" label="施工部位" prop="sgbw">
          <el-select filterable allow-create v-model="form.sgbw" :placeholder="$t('unieap.comm.plsSelect')"
            @change="selectedItemHandle(form.projectId)">
            <el-option v-for="item in optionsProjectList" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label-width="80px" label="施工内容" prop="sglr">
          <el-input type="textarea" :rows="1" v-model="form.sglr" style="width:150px" />
        </el-form-item>
        <el-form-item label-width="80px" label="施工情况" prop="sgqk">
          <el-input type="textarea" :rows="1" v-model="form.sgqk" style="width:150px" />
        </el-form-item>
      </el-row>
      <el-divider content-position="left">监理巡检记录</el-divider>
      <el-row>
        <el-form-item label="施工情况" prop="xjjl">
          <el-input type="textarea" :rows="3" v-model="form.xjjl" style="width:600px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="旁站项目及旁站记录情况" prop="pzjl">
          <el-input v-model="form.pzjl" style="width:520px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="见证项目及见证记录情况" prop="jzjl">
          <el-input v-model="form.jzjl" style="width:520px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="隐蔽工程验收情况" prop="ybgcys">
          <el-input v-model="form.ybgcys" style="width:520px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="分项工程检验批验收情况" prop="fxgcjy">
          <el-input v-model="form.fxgcjy" style="width:520px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label-width="180px" label="工程（例会）会议情况" prop="gclh">
          <el-input v-model="form.gclh" style="width:520px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="施工安全问题及处理后落实情况" prop="aqls">
          <el-input type="textarea" :rows="2" v-model="form.aqls" style="width:600px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="监理书面指令、执行及回复情况" prop="jlzlzx">
          <el-input type="textarea" :rows="2" v-model="form.jlzlzx" style="width:600px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="建设单位口头指示及处理意见" prop="ktzscl">
          <el-input type="textarea" :rows="2" v-model="form.ktzscl" style="width:600px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="施工单位汇报事项及处理意见" prop="hbsxcl">
          <el-input type="textarea" :rows="2" v-model="form.hbsxcl" style="width:600px" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="其他事项" prop="qtsx">
          <el-input type="textarea" :rows="3" v-model="form.qtsx" style="width:600px" />
        </el-form-item>
      </el-row>
      <el-row type="flex" justify="center">
        <el-form-item label-width="0px">
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
import { parseDate2Str, clearJson } from '@/utils'
import { infoDicDataApi, bizHandleApi } from '@/api/unieap/comm'
//自定包引入
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
      visibleProject: false,
      optionsWeatherType: [],
      optionsProjectList: [],
      projectId: '',
      disEdit: true,
      form: {
        id: null,
        projectId: '',
        projectName: '',
        logTime: '',
        dateStart: '',
        dateEnd: '',
        tempMax: '',
        tempMin: '',
        weatherMorning: '',
        weatherAfternoon: '',
        sgdw: '',
        sgbw: '',
        sglr: '',
        sgqk: '',
        xjjl: '',
        pzjl: '',
        jzjl: '',
        ybgcys: '',
        fxgcjy: '',
        gclh: '',
        aqls: '',
        jlzlzx: '',
        ktzscl: '',
        hbsxcl: '',
        qtsx: '',
        zjlyj: '',
        zjlName: '',
        zjlDate: '',
        createDate: '',
        createBy: '',
        modifyDate: '',
        modifyBy: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.projectoa.supervisionmgt.company.companyName')], trigger: 'blur' }]
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      clearJson(data.form)
      data.disEdit = false
      data.form.projectId = id
      data.projectId = id
      data.form.logTime = new Date()
      infoDicDataApi({ 'groupCode': 'WEATHER_TYPE' }).then(r => {
        if (r) {
          data.optionsWeatherType = r.data
        }
      })
      projectDicDataApi({}).then(r => {
        if (r) {
          data.optionsProjectList = r.data
        }
      })
      data.visible = true
    }

    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const initEdit = async (id) => {
      clearJson(data.form)
      data.disEdit = true
      data.visible = true
      data.loading = true
      data.form.id = id
      infoDicDataApi({ 'groupCode': 'WEATHER_TYPE' }).then(r => {
        if (r) {
          data.optionsWeatherType = r.data
        }
      })
      projectDicDataApi({}).then(r => {
        if (r) {
          data.optionsProjectList = r.data
        }
      })
      if (id) {
        const params = {
          repositoryName: 'jldailyLogRepository',
          beanName: 'projectoa.delivery.vo.JldailyLogVO',
          url: urlPrefix + 'commInfo',
          'id': id
        }
        bizHandleApi(params).then(r => {
          if (r) {
            data.form = r.data
            data.form.projectId = r.data.projectId.toString()
          }
        })
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
          data.form.logTime = parseDate2Str(data.form.logTime)
          const handleName = 'commDeal'
          const operType = data.form.id ? 'update' : 'create'
          const urlObj = {
            repositoryName: 'jldailyLogRepository',
            beanName: 'projectoa.delivery.pojo.JldailyLog',
            url: urlPrefix + handleName,
            operType: operType
          }
          const params = Object.assign(urlObj, data.form)
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


    const selectedItemHandle = (projectId) => {
      data.form.projectId = projectId
      data.projectId = projectId
      data.optionsProjectList.forEach(function (value) {
        if (value.dicCode === projectId) {
          data.form.projectId = value.dicCode
          data.form.projectName = value.dicName
        }
      })
      const params = {
        repositoryName: 'projectInfoRepository',
        beanName: 'projectoa.projectmgt.pojo.ProjectInfo',
        url: urlPrefix + 'commInfo',
        'id': projectId
      }
      bizHandleApi(params).then(r => {
        if (r) {
          data.form.sgdw = r.data.sgdw
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
      initEdit,
      submit,
      selectedItemHandle,
      dialogClosedHandle
    }
  }
})
</script>
