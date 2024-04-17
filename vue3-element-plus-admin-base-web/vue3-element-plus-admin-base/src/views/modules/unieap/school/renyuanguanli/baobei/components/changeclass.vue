<template>
  <el-dialog width="700px" :title="$t('unieap.school.renyuanguanli.baobei.bindTimecard')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="80px" label-position="right">
      
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

import { bindTimecardApi } from '@/api/unieap/school/renyuanguanli/baobei'

export default defineComponent({
  components: {},
  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    const data = reactive({
      loading: false,
      visible: false,
      kidsId: '',
      form: {
        kidsId: '',
        id: null,
        timecardList: [{ key: Date.now(), timecardNumber: '' }]
      },
      roles: []
    })
    const rules = computed(() => {
      return {}
    })
    const init = async (id) => {
      data.visible = true
      data.loading = true
      data.kidsId = id
      data.form.kidsId = id
      data.form.timecardList = [{ key: Date.now(), timecardNumber: '' }]
      nextTick(() => { data.loading = false })
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

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     * @author: gumingchen
     */
    const submit = () => {
      refForm.value.validate(async valid => {
        if (valid) {
          const params = {
            id: data.form.kidsId,
            bindTimecard: JSON.stringify(data.form.timecardList)
          }
          const r = await bindTimecardApi(params)
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
      //refForm.value.resetFields()
    }

    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle,
      addTimecard,
      removeTimecard
    }
  }
})
</script>
<style lang="scss" scoped>
/* 1,必须去掉scoped，否则overflow-x: hidden失效 */
.box {
  margin: 0 auto;
  height: 500px;
}
</style>