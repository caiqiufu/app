<template>
  <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.comm.idType')" prop="idType">
          <el-select v-model="form.idType" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsIdType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.comm.idNumber')" prop="idNumber">
          <el-input v-model="form.idNumber" :placeholder="$t('unieap.comm.plsInput')">
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.bloodType')" prop="bloodType">
          <el-select v-model="form.bloodType" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsBloodType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.nationality')" prop="nationality">
          <el-input v-model="form.nationality" :placeholder="$t('unieap.comm.plsInput')">
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.birthplace')" prop="birthplace">
          <el-input v-model="form.birthplace" :placeholder="$t('unieap.comm.plsInput')">
          </el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.nativeplace')" prop="nativeplace">
          <el-input v-model="form.nativeplace" :placeholder="$t('unieap.comm.plsInput')">
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.hukouplace')" prop="hukouplace">
          <el-input v-model="form.hukouplace" :placeholder="$t('unieap.comm.plsInput')">
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.stayType')" prop="stayType">
          <el-select v-model="form.stayType" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsStayType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.currentplace')" prop="currentplace">
          <el-input type="textarea" :rows="3" v-model="form.currentplace" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.healthStatus')" prop="healthStatus">
          <el-select v-model="form.healthStatus" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsHealthStatus" :key="item.dicCode" :label="item.dicName"
              :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.orphan')" prop="orphan">
          <el-radio-group v-model="form.orphan">
            <el-radio :label="'Y'">{{ $t('unieap.comm.yes') }}</el-radio>
            <el-radio :label="'N'">{{ $t('unieap.comm.no') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.deformity')" prop="deformity">
          <el-radio-group v-model="form.deformity">
            <el-radio :label="'Y'">{{ $t('unieap.comm.yes') }}</el-radio>
            <el-radio :label="'N'">{{ $t('unieap.comm.no') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">

      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.guardian')" prop="guardian">
          <el-input v-model="form.guardian" :placeholder="$t('unieap.comm.plsInput')">
          </el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.guardianIdtype')" prop="guardianIdtype">
          <el-select v-model="form.guardianIdtype" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsGuardianIdtype" :key="item.dicCode" :label="item.dicName"
              :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.guardianIdnumber')" prop="guardianIdnumber">
          <el-input v-model="form.guardianIdnumber" :placeholder="$t('unieap.comm.plsInput')">
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20" type="flex" justify="center">
      <el-button @click="cancel()">{{ $t('unieap.comm.cancel') }}</el-button>
      <el-button v-repeat type="primary" @click="submit()">{{ $t('unieap.comm.confirm') }}</el-button>
    </el-row>
    <el-row type="flex" justify="center">
      <br />
    </el-row>
  </el-form>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'

import { ElMessage } from 'element-plus'

import { infoDicDataApi } from '@/api/unieap/comm'
import { registerInfoApi, addRegisterInfoApi, editRegisterInfoApi } from '@/api/unieap/school/renyuanguanli/baobei'
import { isIdCardNumber } from '@/utils/regular'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    const data = reactive({
      loading: true,
      visible: true,
      optionsIdType: [],
      optionsBloodType: [],
      optionsStayType: [],
      optionsHealthStatus: [],
      optionsGuardianIdtype: [],
      kidsId: '',
      form: {
        kidsId: '',
        id: null,
        idType: '',
        idNumber: '',
        bloodType: '',
        nationality: '',
        birthplace: '',
        nativeplace: '',
        hukouplace: '',
        currentplace: '',
        healthStatus: '',
        orphan: '',
        deformity: '',
        guardian: '',
        guardianIdtype: '',
        guardianIdnumber: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {

      }
    })
    const init = async (id) => {
      data.visible = true
      data.loading = true
      data.kidsId = id
      const dicIdType = await infoDicDataApi({ 'groupCode': 'ID_TYPE' })
      if (dicIdType) {
        data.optionsIdType = dicIdType.data
        data.optionsGuardianIdtype = dicIdType.data
      }
      const dicBloodType = await infoDicDataApi({ 'groupCode': 'BLOOD_TYPE' })
      if (dicBloodType) {
        data.optionsBloodType = dicBloodType.data
      }
      const dicStayType = await infoDicDataApi({ 'groupCode': 'STAY_TYPE' })
      if (dicStayType) {
        data.optionsStayType = dicStayType.data
      }
      const dicHealthStatus = await infoDicDataApi({ 'groupCode': 'HEALTH_STATUS' })
      if (dicHealthStatus) {
        data.optionsHealthStatus = dicHealthStatus.data
      }
      const r = await registerInfoApi({ 'id': data.kidsId })
      if (r) {
        data.form = r.data
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
          data.form.kidsId = data.kidsId
          const r = data.form.id ? await editRegisterInfoApi(data.form) : await addRegisterInfoApi(data.form)
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


    const cancel = ()=>{
      emit('refresh')
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
      cancel,
      dialogClosedHandle
    }
  }
})
</script>
