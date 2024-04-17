<template>
  <el-dialog width="700px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="80px" label-position="right">
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.kidsName')" prop="kidsName" style="width:300px">
        <el-input v-model="form.kidsName" :placeholder="$t('unieap.comm.plsInput')" maxlength="20" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.comm.gender')" prop="gender">
        <el-radio-group v-model="form.gender">
          <el-radio :label="'M'">{{ $t('unieap.comm.male') }}</el-radio>
          <el-radio :label="'F'">{{ $t('unieap.comm.fmale') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.birthday')" prop="birthday">
        <el-date-picker v-model="form.birthday" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.registerDate')" prop="registerDate">
        <el-date-picker v-model="form.registerDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.className')" prop="className">
        <el-select v-model="form.className" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="item in optionsClass" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-for="(contactInfo, index) in form.contactList"
        :label="($t('unieap.school.renyuanguanli.baobei.contactInfo') + (index + 1))" :key="contactInfo.key">
        <el-col :span="5">
          <el-input v-model="contactInfo.contactName"
            :placeholder="$t('unieap.school.renyuanguanli.baobei.contactName')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input v-model="contactInfo.contactNumber"
            :placeholder="$t('unieap.school.renyuanguanli.baobei.contactNumber')"></el-input>
        </el-col>
        <el-col :span="4">
          <el-select v-model="contactInfo.relationship" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsRelationship" :key="item.dicCode" :label="item.dicName"
              :value="item.dicCode">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button @click.prevent="removeContact(contactInfo)"
            :disabled="(form.contactList.length <= 1) || contactInfo.checked">{{ $t('unieap.comm.delete') }}</el-button>
        </el-col>
        <el-col :span="5">
          <el-checkbox @change="firstContact(checked, contactInfo)" v-model="contactInfo.checked">{{
              $t('unieap.school.renyuanguanli.baobei.firstcontact')
          }}</el-checkbox>
        </el-col>
      </el-form-item>
      <el-form-item>
        <el-button @click="addContact">
          <el-icon>
            <Plus />
          </el-icon>
          {{ $t('unieap.comm.create') }}
        </el-button>
      </el-form-item>

      <el-form-item v-for="(timecardInfo, index) in form.timecardList"
        :label="$t('unieap.school.renyuanguanli.baobei.timecardNumber') + (index + 1)" :key="timecardInfo.key">
        <el-input v-model="timecardInfo.timecardNumber"
          :placeholder="$t('unieap.school.renyuanguanli.baobei.timecardNumber')" style="width:300px"></el-input>
        <el-button @click.prevent="removeTimecard(timecardInfo)" :disabled="(form.timecardList.length <= 1)">{{
            $t('unieap.comm.delete')
        }}</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="addTimecard">
          <el-icon>
            <Plus />
          </el-icon>
          {{ $t('unieap.comm.create') }}
        </el-button>
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
import { parseDate2Str } from '@/utils'

import { infoDicDataApi } from '@/api/unieap/comm'
import { infoClassDicDataApi } from '@/api/unieap/school/jiaowuguanli/banji'
import { addKidsApi } from '@/api/unieap/school/renyuanguanli/baobei'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    const data = reactive({
      loading: false,
      visible: false,
      optionsRelationship: [],
      optionsClass: [],
      form: {
        id: null,
        kidsName: '',
        gender: 'M',
        birthday: null,
        registerDate: null,
        className: '',
        contactList: [{ contactName: '', contactNumber: '', relationship: '', checked: false, key: Date.now() }],
        timecardList: [{ key: Date.now(), timecardNumber: '' }]
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        kidsName: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.school.renyuanguanli.baobei.kidsName')], trigger: 'blur' }],
        gender: [{ required: true, message: [t('unieap.comm.plsSelect')] + [t('unieap.comm.gender')], trigger: 'blur' }],
        birthday: [{ required: true, message: [t('unieap.comm.plsSelect')] + [t('unieap.school.renyuanguanli.baobei.birthday')], trigger: 'blur' }],
        registerDate: [{ required: true, message: [t('unieap.comm.plsSelect')] + [t('unieap.school.renyuanguanli.baobei.registerDate')], trigger: 'blur' }],
        className: [{ required: true, message: [t('unieap.comm.plsSelect')] + [t('unieap.school.renyuanguanli.baobei.className')], trigger: 'blur' }]
      }
    })
    const init = async (id) => {
      const classOptions = await infoClassDicDataApi({})
      if (classOptions) {
        data.optionsClass = classOptions.data
      }
      const dicRelationship = await infoDicDataApi({ 'groupCode': 'CONTACT_RELATIONSHIP' })
      if (dicRelationship) {
        data.optionsRelationship = dicRelationship.data
      }
      data.visible = true
      data.loading = true
      data.form.id = id
      data.form.registerDate = Date.now()
      data.form.contactList = [{ contactName: '', contactNumber: '', relationship: '', checked: false, key: Date.now() }]
      data.form.timecardList = [{ key: Date.now(), timecardNumber: '' }]
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
          if (!contactInfoValidate()) {
            return
          }
          const params = {
            id: data.form.id,
            kidsCode: '',
            kidsName: data.form.kidsName,
            gender: data.form.gender,
            birthday: parseDate2Str(data.form.birthday),
            registerDate: parseDate2Str(data.form.registerDate),
            classId: data.form.className,
            sourceType: 'SYS',
            kidsContact: JSON.stringify(data.form.contactList),
            bindTimecard: JSON.stringify(data.form.timecardList)
          }
          const r = await addKidsApi(params)
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
   * @author: gumingchen
   */
    const dialogClosedHandle = () => {
      refForm.value.resetFields()
    }

    const removeContact = (item) => {
      var index = data.form.contactList.indexOf(item)
      if (index !== -1) {
        data.form.contactList.splice(index, 1)
      }
    }

    const addContact = () => {
      data.form.contactList.push({ contactName: '', contactNumber: '', relationship: '', checked: false, key: Date.now() });
    }

    const removeTimecard = (item) => {
      var index = data.form.timecardList.indexOf(item)
      if (index !== -1) {
        data.form.timecardList.splice(index, 1)
      }
    }

    const addTimecard = () => {
      data.form.timecardList.push({ key: Date.now(), timecardNumber: '' });
    }

    const firstContact = (checked, item) => {
      for (let con of data.form.contactList) {
        var index = data.form.contactList.indexOf(item)
        if (checked && index === -1) {
          con.checked = false
        }
      }
    }
    /**
     * 联系人校验
     */
    const contactInfoValidate = () => {
      const reg = /^1[3456789]\d{9}$/
      if (data.form.contactList.length === 1) {
        let con = data.form.contactList[0]
        if (con.contactName === '' && con.contactNumber === '' && con.relationship === '') {
          return true
        }
      }
      for (let con of data.form.contactList) {
        if (con.contactName === '' || con.contactNumber === '' || con.relationship === '') {
          ElMessage({
            message: t('unieap.school.renyuanguanli.baobei.contactValidate'),
            type: 'error'
          })
          return false
        }
        if (!reg.test(con.contactNumber)) {
          ElMessage({
            message: t('unieap.school.validate.phoneNumber', { phoneNumber: con.contactNumber }),
            type: 'error'
          })
          return false
        }
      }
      return true
    }

    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle,
      removeContact,
      addContact,
      firstContact,
      removeTimecard,
      addTimecard,
      contactInfoValidate
    }
  }
})
</script>
