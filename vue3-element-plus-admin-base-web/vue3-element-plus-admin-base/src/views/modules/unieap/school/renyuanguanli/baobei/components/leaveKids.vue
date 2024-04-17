<template>
  <p>test</p>
</template>

<script>
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'

import { ElMessage } from 'element-plus'

import { infoApi, addApi, editApi } from '@/api/unieap/school/jiaowuguanli/nianji'

export default defineComponent({
  /***
   * emits: 列表声明从父组件继承来的事件
   * $emit: 抛出事件， 通知父组件处理
   * 在子组件中，通过$emit()来触发事件
   * 在父组件中，通过v-on来监听子组件事件 //@父组件绑定事件
   */
  emits: ['refresh'],
  setup(_props, { emit }) {
    const refForm = ref()
    const data = reactive({
      loading: true,
      visible: true,
      form: {
        id: null,
        name: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message: "请输入名称", trigger: 'blur' }]
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
          const r = data.form.id ? await editApi(data.form) : await addApi(data.form)
          if (r) {
            data.visible = false
            ElMessage({
              message: '操作成功!',
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
