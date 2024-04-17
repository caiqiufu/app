<template>
  <el-dialog width="500px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="80px" label-position="right">
      <el-form-item :label="$t('unieap.school.comm.grade')" prop="gradeId">
        <el-select v-model="form.gradeId" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="item in optionsGrade" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.className')" prop="className">
        <el-input v-model="form.className" :placeholder="$t('unieap.school.jiaowuguanli.banji.className')"
          maxlength="20" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.mainTeacher')">
        <el-select v-model="form.mainTeacherId" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="item in optionsMainTeacher" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.nurse')">
        <el-select v-model="form.nurseId" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="item in optionsNurse" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.assistTeacher')">
        <el-select multiple v-model="form.assistTeachers" :placeholder="$t('unieap.comm.plsSelect')"
          style="width: 80%;">
          <el-option v-for="item in optionsAssistTeachers" :key="item.dicCode" :label="item.dicName"
            :value="item.dicCode">
          </el-option>
        </el-select>
        {{ $t('unieap.comm.mulSelect') }}
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.othertTeacher')">
        <el-select multiple v-model="form.othertTeachers" :placeholder="$t('unieap.comm.plsSelect')"
          style="width: 80%;">
          <el-option v-for="item in optionsOthertTeachers" :key="item.dicCode" :label="item.dicName"
            :value="item.dicCode">
          </el-option>
        </el-select>
        {{ $t('unieap.comm.mulSelect') }}
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">{{ $t('unieap.comm.cancel') }}</el-button>
        <el-button v-repeat type="primary" @click="submit()">{{ $t('unieap.comm.confirm') }}</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'

import { ElMessage } from 'element-plus'

import { infoGradeDicDataApi } from '@/api/unieap/school/jiaowuguanli/nianji'
import { infoApi, addApi, editApi, infoStaffInfoDicDataApi } from '@/api/unieap/school/jiaowuguanli/banji'

export default defineComponent({
  /***
   * emits: 列表声明从父组件继承来的事件
   * $emit: 抛出事件， 通知父组件处理
   * 在子组件中，通过$emit()来触发事件
   * 在父组件中，通过v-on来监听子组件事件 //@父组件绑定事件
   */
  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    const data = reactive({
      loading: false,
      visible: false,
      optionsGrade: [],
      optionsMainTeacher: [],
      optionsNurse: [],
      optionsAssistTeachers: [],
      optionsOthertTeachers: [],
      form: {
        id: null,
        className: '',
        gradeId: '',
        gradeName: '',
        mainTeacherId: '',
        mainTeacherName: '',
        nurseId: '',
        assistTeachers: [],
        othertTeachers: []
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        gradeId: [{ required: true, message: "请选择年级", trigger: 'blur' }],
        className: [{ required: true, message: "请输入名称", trigger: 'blur' }]
      }
    })
    const init = async (id) => {
      const dicGrade = await infoGradeDicDataApi({})
      if (dicGrade) {
        data.optionsGrade = dicGrade.data
      }
      const dicStaffInfo = await infoStaffInfoDicDataApi({})
      if (dicStaffInfo) {
        data.optionsMainTeacher = dicStaffInfo.data
        data.optionsNurse = dicStaffInfo.data
        data.optionsAssistTeachers = dicStaffInfo.data
        data.optionsOthertTeachers = dicStaffInfo.data
      }
      data.visible = true
      data.loading = true
      data.form.id = id
      if (id) {
        const r = await infoApi({ 'id': id })
        if (r) {
          data.form.gradeId = r.data.gradeId.toString()
          data.form.className = r.data.className
          data.form.mainTeacherId = r.data.mainTeacherId.toString()
          data.form.nurseId = r.data.nurseId.toString()
          if (r.data.assistTeachers) {
            data.form.assistTeachers = r.data.assistTeachers.split(',')
          }
          if (r.data.othertTeachers) {
            data.form.othertTeachers = r.data.othertTeachers.split(',')
          }
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
          const r = data.form.id ? await editApi(data.form) : await addApi(data.form)
          if (r) {
            data.visible = false
            ElMessage({
              message: [t('unieap.comm.optSuccess')],
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
