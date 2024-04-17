<template>
  <el-dialog
    width="450px"
    :title="form.id ? $t('demo.edit') : $t('demo.add')"
    v-model="visible"
    :close-on-click-modal="false"
    @closed="dialogClosedHandle"
    append-to-body
    draggable
    destroy-on-close>
    <el-form
      v-loading="loading"
      :model="form"
      :rules="rules"
      ref="refForm"
      label-position="top">
      <el-form-item :label="$t('demo.id')" prop="id">
        <el-input
          v-model="form.id"
          :placeholder="$t('demo.id')"
          maxlength="20"
          show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('demo.name')" prop="name">
        <el-input
          v-model="form.name"
          :placeholder="$t('demo.name')"
          maxlength="20"
          show-word-limit />
      </el-form-item>
      <Collapse inactive-text="展开（非必填项）">
        <template #default>
          <el-select v-model="form.activateFlag" :placeholder="$t('demo.plsselect')">
            <el-option
              v-for="item in options"
              :key="item.dicCode"
              :label="item.dicName"
              :value="item.dicCode">
            </el-option>
          </el-select>
          <el-form-item :label="$t('demo.remark')" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              :placeholder="$t('demo.remark')"
              maxlength="200"
              show-word-limit />
          </el-form-item>
        </template>
      </Collapse>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button
          v-repeat
          type="primary"
          @click="submit()">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'

import { ElMessage } from 'element-plus'
import Collapse from '@/components/collapse'

import { infoApi, addApi, editApi,infoDicDataApi } from '@/api/demo'

export default defineComponent({
  /***
   * emits: 列表声明从父组件继承来的事件
   * $emit: 抛出事件， 通知父组件处理
   * 在子组件中，通过$emit()来触发事件
   * 在父组件中，通过v-on来监听子组件事件 //@父组件绑定事件
   */
  emits: ['refresh'],
  components: { Collapse },
  setup(_props, { emit }) {
    const refForm = ref()
    const data = reactive({
      loading: false,
      visible: false,
      options:[],
      form: {
        id: null,
        name: '',
        logo: '',
        describe: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message:"请输入名称", trigger: 'blur' }]
      }
      })
    //const rules = reactive(function() {
    //  return {
    //    name: [{ required: true, message: this.$t('demo.namerequired'), trigger: 'blur' }]
    //  }
    //}())

    const init = async (id) => {
      const dic = await infoDicDataApi({'groupCode':'ACTIVATE_FLAG'})
      if(dic){
        data.options = dic.data
      }
      data.visible = true
      data.loading = true
      data.form.id = id
      if (id) {
        const r = await infoApi({'id': id })
        if (r) {
          data.form.name = r.data.name
          data.form.activateFlag = r.data.activateFlag
          data.form.remark = r.data.remark
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
