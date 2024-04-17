<template>
  <el-dialog width="450px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-position="top">
      <el-form-item :label="$t('unieap.school.jiaowuguanli.nianji.gradeName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.school.jiaowuguanli.nianji.gradeName')" maxlength="20"
          show-word-limit />
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

import { infoApi} from '@/api/unieap/school/jiaowuguanli/nianji'
import {bizHandleApi,bizHandleApi1,bizHandleApi2 } from '@/api/unieap/comm'

export default defineComponent({
  /***
   * emits: 列表声明从父组件继承来的事件
   * $emit: 抛出事件， 通知父组件处理
   * 在子组件中，通过$emit()来触发事件
   * 在父组件中，通过v-on来监听子组件事件 //@父组件绑定事件
   */
  emits: ['refresh'],
  setup(_props, { emit }) {
    const {t} = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/jiaowuguanli/nianji/'
    const data = reactive({
      loading: false,
      visible: false,
      form: {
        id: null,
        name: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message: [t('unieap.comm.plsInput')], trigger: 'blur' }]
      }
    })
    const init = async (id) => {
      data.visible = true
      data.loading = true
      data.form.id = id
      if (id) {
        const r = await infoApi({ 'id': id })
        if (r) {
          data.form.name = r.data.name
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
          //const r = data.form.id ? await editApi(data.form) : await bizHandleApi(data.form)
          //const r = await bizHandleApi(data.form)
          const handleName = data.form.id ? 'update' : 'create'
          //const params = Object.assign(urlObj, data.form)  
          const url = urlPrefix + handleName 
          //const url = '/backstage/jiaowuguanli/nianji/create'
          const params = Object.assign({url: urlPrefix + handleName},data.form)
          const r = await bizHandleApi(params)    
          //const r = await bizHandleApi2(data.form)        
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
