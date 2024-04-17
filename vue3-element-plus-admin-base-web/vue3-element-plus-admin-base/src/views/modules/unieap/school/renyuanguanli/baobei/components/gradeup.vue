<template>
  <el-dialog width="450px" :title="$t('unieap.school.jiaowuguanli.banji.gradeUp')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="120px" label-position="right">
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.currentGradeName')" prop="gradeId">
        <el-select v-model="form.gradeId" :placeholder="$t('unieap.comm.plsSelect')" disabled>
          <el-option v-for="item in optionsGrade" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.currentClassName')" prop="classId">
        <el-select v-model="form.classId" :placeholder="$t('unieap.comm.plsSelect')" disabled>
          <el-option v-for="item in optionsClass" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.upGradeName')" prop="upGradeId">
        <el-select v-model="form.upGradeId" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="item in optionsGrade" :key="item.dicCode" :label="item.dicName" :value="item.dicCode"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.school.jiaowuguanli.banji.className')" prop="className">
        <el-input v-model="form.className" :placeholder="$t('unieap.school.jiaowuguanli.banji.className')"
          maxlength="20" show-word-limit />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button v-repeat type="primary" @click="submit()">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'

import { ElMessage } from 'element-plus'

import { infoGradeDicDataApi } from '@/api/unieap/school/jiaowuguanli/nianji'
import { infoApi, infoClassDicDataApi,gradeUp} from '@/api/unieap/school/jiaowuguanli/banji'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const refForm = ref()
    const data = reactive({
      loading: false,
      visible: false,
      optionsGrade: [],
      optionsClass: [],
      form: {
        id: null,
        gradeId: '',
        classId: '',
        upGradeId: '',
        className: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        upGradeId: [{ required: true, message: "请选择年级", trigger: 'blur' }],
        className: [{ required: true, message: "请输入班级名称", trigger: 'blur' }]
      }
    })
    const init = async (id) => {
      const dicGrade = await infoGradeDicDataApi({})
      if (dicGrade) {
        data.optionsGrade = dicGrade.data
      }
      data.visible = true
      data.loading = true
      data.form.id = id
      if (id) {
        const r = await infoApi({ 'id': id })
        if (r) {
          data.form.gradeId = r.data.gradeId.toString()
          const dicClass = await infoClassDicDataApi({ 'gradeId': data.form.gradeId  })
          if (dicClass) {
            data.optionsClass = dicClass.data
            data.form.classId =  r.data.id.toString()
          }
        }
        nextTick(() => { data.loading = false })
      }
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
          if (data.form.gradeId === data.form.upGradeId) {
            ElMessage({
              message: '班级未改变，请重新选择!',
              type: 'warning'
            })
          } else {
            const r = await gradeUp({ id: data.form.id, gradeId: data.form.upGradeId, className: data.form.className })
            if (r) {
              data.visible = false
              ElMessage({
                message: '操作成功!',
                type: 'success'
              })
              emit('refresh')
            }
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
