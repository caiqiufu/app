<template>
  <el-dialog width="700px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="180px" label-position="right">
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.contactName')" prop="contactName">
        <el-input v-model="form.contactName" :placeholder="$t('unieap.comm.plsInput')" style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.relationship')" prop="relationship">
        <el-select v-model="form.relationship" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsRelationship" :key="item.dicCode" :label="item.dicName"
            :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.contactNumber')" prop="contactNumber">
        <el-input v-model="form.contactNumber" :placeholder="$t('unieap.comm.plsInput')" style="width:300px">
        </el-input>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.comm.idType')" prop="idType">
        <el-select v-model="form.idType" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsIdType" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.comm.idNumber')" prop="idNumber">
        <el-input v-model="form.idNumber" :placeholder="$t('unieap.comm.plsInput')" style="width:300px">
        </el-input>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.comm.nation')" prop="nation">
        <el-select v-model="form.nation" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsNation" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.education')" prop="education">
        <el-select v-model="form.education" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsEducation" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.occupation')" prop="occupation">
        <el-input v-model="form.occupation" :placeholder="$t('unieap.comm.plsInput')" style="width:300px">
        </el-input>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.email')" prop="email">
        <el-input v-model="form.email" :placeholder="$t('unieap.comm.plsInput')" suffix="test" style="width:300px">
        </el-input>
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
import { isPhoneNumber, isEmail } from '@/utils/validate'

import { infoDicDataApi } from '@/api/unieap/comm'
import { contactInfoApi, addContactInfoApi, editContactInfoApi } from '@/api/unieap/school/renyuanguanli/baobei'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    const data = reactive({
      loading: false,
      visible: false,
      optionsRelationship: [],
      optionsIdType: [],
      optionsNation: [],
      optionsEducation: [],
      kidsId: '',
      form: {
        kidsId: '',
        id: null,
        contactName: '',
        relationship: '',
        contactNumber: '',
        idType: '',
        idNumber: '',
        nation: '',
        education: '',
        occupation: '',
        email: '',
        profileId: '',
        priority: '-1',
        sourceType: 'SYS'
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        contactName: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.school.renyuanguanli.baobei.contactName')], trigger: 'blur' }],
        relationship: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.school.renyuanguanli.baobei.relationship')], trigger: 'blur' }],
        contactNumber: [{ required: true, validator: isPhoneNumber, message: [t('unieap.comm.plsInput')] + [t('unieap.school.renyuanguanli.baobei.contactNumber')], trigger: 'blur' }],
        email: [{ required: false, validator: isEmail, message: [t('unieap.comm.plsInput')] + [t('unieap.school.renyuanguanli.baobei.email')], trigger: 'blur' }]
      }
    })
    const init = async (param) => {
      data.visible = true
      data.loading = true
      data.kidsId = param.kidsId
      data.form.kidsId = param.kidsId
      data.form.id = param.contactId
      const dicRelationship = await infoDicDataApi({ 'groupCode': 'CONTACT_RELATIONSHIP' })
      if (dicRelationship) {
        data.optionsRelationship = dicRelationship.data
      }
      const dicIdType = await infoDicDataApi({ 'groupCode': 'ID_TYPE' })
      if (dicIdType) {
        data.optionsIdType = dicIdType.data
      }
      const dicNation = await infoDicDataApi({ 'groupCode': 'NATION' })
      if (dicNation) {
        data.optionsNation = dicNation.data
      }
      const dicEducation = await infoDicDataApi({ 'groupCode': 'EDUCATION' })
      if (dicEducation) {
        data.optionsEducation = dicEducation.data
      }
      if (data.form.id) {
        const r = await contactInfoApi({ 'id': data.form.id })
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
     * @author: gumingchen
     */
    const submit = () => {
      refForm.value.validate(async valid => {
        if (valid) {
          const r = data.form.id ? await editContactInfoApi(data.form) : await addContactInfoApi(data.form)
          if (r) {
            ElMessage({
              message: t('unieap.comm.optSuccess'),
              type: 'success'
            })
            data.visible = false
            emit('refresh')
          }
        }
      })
    }

    /**
   * @description: 弹窗关闭动画结束时的回调
   * @param {*}
   * @return {*}
   * @author: gumingchen
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
