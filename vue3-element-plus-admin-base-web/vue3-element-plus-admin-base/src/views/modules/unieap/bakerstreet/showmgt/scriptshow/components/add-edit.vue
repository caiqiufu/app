<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item :label="$t('unieap.comm.seq')" prop="seq">
        <el-input v-model="form.seq" :placeholder="$t('unieap.comm.plsInput')" disabled style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.scriptName')" prop="scriptId">
        <el-select v-model="form.scriptId" :placeholder="$t('unieap.comm.plsSelect')" style="width:300px">
          <el-option v-for="item in optionsScripts" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.brief')" prop="brief">
        <el-input v-model="form.brief" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit
          style="width:300px" />
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
import { scriptDicDataApi } from '@/api/unieap/bakerstreet/dic'
//自定包引入
import { updateScriptShowApi } from '@/api/unieap/bakerstreet/playmgt'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      optionsScripts: [],
      form: {
        id: null,
        scriptId: '',
        seq: '',
        brief: ''
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        scriptId: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.scriptName')], trigger: 'blur' }],
        brief: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.brief')], trigger: 'blur' }]
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (index, scriptshow) => {
      const dicScripts = await scriptDicDataApi({})
      if (dicScripts) {
        data.optionsScripts = dicScripts.data
      }
      data.visible = true
      data.loading = true
      data.form.id = scriptshow.id
      data.form.seq = index
      data.form.brief = scriptshow.brief
      data.form.scriptId = scriptshow.scriptId.toString()
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
          const params = {
            id: data.form.id,
            seq: data.form.seq,
            scriptId: data.form.scriptId,
            brief: data.form.brief
          }
          const r = await updateScriptShowApi(params)
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
