<template>
  <el-dialog width="700px" :title="$t('unieap.school.renyuanguanli.baobei.bindTimecard')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="80px" label-position="right">
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
      <el-row type="flex">
          <el-col :span="8">
            绑定方式A
          </el-col>
          <el-col :span="16">
            1. 将考勤卡读卡器与电脑连接<br />
            2. 将鼠标光标放置在卡号的输入框内<br />
            3. 将考勤卡刷过读卡器<br />
          </el-col>
        </el-row>
        <el-row type="flex">
          <el-col :span="8">
            绑定方式B
          </el-col>
          <el-col :span="16">
            手动输入考勤卡卡号<br />
          </el-col>
        </el-row>
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