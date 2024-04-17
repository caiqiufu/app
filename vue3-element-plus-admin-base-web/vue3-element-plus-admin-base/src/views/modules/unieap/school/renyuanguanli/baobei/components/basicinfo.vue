<template>
  <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.kidsName')" prop="kidsName">
          <el-input v-model="form.kidsName" :placeholder="$t('unieap.comm.plsInput')" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.nickname')" prop="nickname">
          <el-input v-model="form.nickname" :placeholder="$t('unieap.comm.plsInput')" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item :label="$t('unieap.comm.gender')" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="'M'">{{ $t('unieap.comm.male') }}</el-radio>
            <el-radio :label="'F'">{{ $t('unieap.comm.fmale') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.profile')" prop="profileUrl">
          <el-avatar :size="120" :src="form.profileUrl">
            <UImageUploadSingle @updateData="updateData" v-model="form.profileUrl" :category="form.category"
              :extKey="form.extKey" />
          </el-avatar>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.birthday')" prop="birthday">
          <el-date-picker v-model="form.birthday" type="date" :placeholder="$t('unieap.comm.plsSelect')">
          </el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.registerDate')" prop="registerDate">
          <el-date-picker v-model="form.registerDate" type="date" :placeholder="$t('unieap.comm.plsSelect')">
          </el-date-picker>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.comm.grade')" prop="gradeId">
          <el-select v-model="form.gradeId" :placeholder="$t('unieap.comm.plsSelect')" disabled>
            <el-option v-for="item in optionsGrade" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.className')" prop="classId">
          <el-select v-model="form.classId" :placeholder="$t('unieap.comm.plsSelect')" disabled>
            <el-option v-for="item in optionsClass" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.height')" prop="height">
          <el-input v-model="form.height" :placeholder="$t('unieap.comm.plsInput')">
            <template v-slot:append>CM</template>
          </el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.weight')" prop="weight">
          <el-input v-model="form.weight" :placeholder="$t('unieap.comm.plsInput')">
            <template v-slot:append>KG</template>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="10" type="flex" justify="start">
      <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.changeClassHistory')" prop="changeClassHistory">
        <el-link type="primary" :href="form.changeClassHistory">{{
    $t('unieap.school.renyuanguanli.baobei.viewClassHistory')
}}</el-link>
      </el-form-item>
    </el-row>

    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.address')" prop="address">
          <el-select v-model="form.provinceId" :placeholder="$t('unieap.comm.plsSelect')"
            @change="provinceChange(form.provinceId)">
            <el-option v-for="item in optionsProvince" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
          <el-select v-model="form.cityId" :placeholder="$t('unieap.comm.plsSelect')" @change="cityChange(form.cityId)">
            <el-option v-for="item in optionsCity" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
          <el-select v-model="form.streetId" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsStreet" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.addressDetail')" prop="addressDetail">
          <el-input type="textarea" :rows="3" v-model="form.addressDetail" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row type="flex" justify="center">
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
import { parseDate2Str } from '@/utils'

import { infoGradeDicDataApi } from '@/api/unieap/school/jiaowuguanli/nianji'
import { infoClassDicDataApi } from '@/api/unieap/school/jiaowuguanli/banji'
import { editKidsApi, kidsInfoApi, infoDistrictDicDataApi } from '@/api/unieap/school/renyuanguanli/baobei'

import UImageUploadSingle from '@/components/unieap/file-upload-single'


export default defineComponent({
  components: { UImageUploadSingle },
  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    const data = reactive({
      loading: false,
      visible: false,
      optionsGrade: [],
      optionsClass: [],
      optionsProvince: [],
      optionsCity: [],
      optionsStreet: [],
      form: {
        id: null,
        kidsName: '',
        nickname: '',
        gender: 'M',
        profileId: '',
        profileUrl: '',
        birthday: null,
        registerDate: null,
        gradeId: '',
        classId: '',
        height: '',
        weight: '',
        changeClassHistory: '',
        addressId: '',
        provinceId: '',
        cityId: '',
        streetId: '',
        addressDetail: '',
        category: 'demo',
        extKey: '123456789'
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        kidsName: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.school.renyuanguanli.baobei.kidsName')], trigger: 'blur' }],
        gender: [{ required: true, message: [t('unieap.comm.plsSelect')] + [t('unieap.comm.gender')], trigger: 'blur' }],
        birthday: [{ required: true, message: [t('unieap.comm.plsSelect')] + [t('unieap.school.renyuanguanli.baobei.birthday')], trigger: 'blur' }],
        registerDate: [{ required: true, message: [t('unieap.comm.plsSelect')] + [t('unieap.school.renyuanguanli.baobei.registerDate')], trigger: 'blur' }]
      }
    })
    const init = async (id) => {
      data.visible = true
      data.loading = true
      data.form.id = id
      const r = await kidsInfoApi({ 'id': id })
      if (r) {
        const dicGrade = await infoGradeDicDataApi({})
        if (dicGrade) {
          data.optionsGrade = dicGrade.data
        }
        const dicClass = await infoClassDicDataApi({ 'gradeId': r.data.gradeId })
        if (dicClass) {
          data.optionsClass = dicClass.data
          data.form = r.data
          data.form.gradeId = r.data.gradeId.toString()
          data.form.classId = r.data.classId.toString()
        }
        if (r.data.addressId) {
          const dicProvince = await infoDistrictDicDataApi({ 'parentId': 1 })
          if (dicProvince) {
            data.optionsProvince = dicProvince.data
            data.form.provinceId = r.data.provinceId.toString()
          }
          const dicCity = await infoDistrictDicDataApi({ 'parentId': r.data.provinceId })
          if (dicCity) {
            data.optionsCity = dicCity.data
            data.form.cityId = r.data.cityId.toString()
          }
          const dicStreet = await infoDistrictDicDataApi({ 'parentId': r.data.cityId })
          if (dicStreet) {
            data.optionsStreet = dicStreet.data
            data.form.streetId = r.data.streetId.toString()
          }
        } else {
          const dicProvince = await infoDistrictDicDataApi({ 'parentId': 1 })
          if (dicProvince) {
            data.optionsProvince = dicProvince.data
          }
        }
      }
      nextTick(() => { data.loading = false })
    }

    const provinceChange = (provinceId) => {
      cityOptions(provinceId)
    }
    const cityOptions = async (provinceId) => {
      const dic = await infoDistrictDicDataApi({ 'parentId': provinceId })
      if (dic) {
        data.optionsCity = dic.data
        data.form.cityId = ''
        data.form.streetId = ''
      }
    }
    const cityChange = (cityId) => {
      streetOptions(cityId)
    }
    const streetOptions = async (cityId) => {
      const dic = await infoDistrictDicDataApi({ 'parentId': cityId })
      if (dic) {
        data.optionsStreet = dic.data
        data.form.streetId = ''
      }
    }
    /**
     * 子类调用该方法刷新数据
     * @param {*} e 
     */
    const updateData = (e) => {
      data.form.profileId = e.fileId
    }

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
      refForm.value.validate(async valid => {
        if (valid) {
          const params = {
            id: data.form.id,
            kidsCode: '',
            nickname: data.form.nickname,
            kidsName: data.form.kidsName,
            gender: data.form.gender,
            birthday: parseDate2Str(data.form.birthday),
            registerDate: parseDate2Str(data.form.registerDate),
            height: data.form.height,
            weight: data.form.weight,
            profileId: data.form.profileId,
            addressId: data.form.addressId,
            provinceId: data.form.provinceId,
            cityId: data.form.cityId,
            streetId: data.form.streetId,
            addressDetail: data.form.addressDetail
          }
          const r = await editKidsApi(params)
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
    const cancel = () => {
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
      dialogClosedHandle,
      provinceChange,
      cityChange,
      updateData
    }
  }
})
</script>
