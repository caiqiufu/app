<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="100px" label-position="right">
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.dmName')" prop="player_ids">
        <el-cascader class="width-full" ref="refCascader" v-model="form.player_ids" :options="players"
          :props="cascaderProps" :show-all-levels="false" collapse-tags collapse-tags-tooltip clearable />
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
//自定包引入
import { playerDicDataApi, scriptDMDicDataApi } from '@/api/unieap/bakerstreet/dic'
import { updateScriptDMApi } from '@/api/unieap/bakerstreet/scriptmgt'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    const refCascader = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      players: [],
      form: {
        scriptId: null,
        playerID: '',
        player_ids: []
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        player_ids: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.dmName')], trigger: 'blur' }]
      }
    })

    const cascaderProps = computed(() => {
      const reuslt = {
        multiple: true,
        emitPath: false,
        checkStrictly: false,
        value: 'dicCode',
        label: `dicName`,
        children: 'children'
      }
      return reuslt
    })

    const getAllDMPlayer = async () => {
      const r = await playerDicDataApi({ playerType: '2' })
      if (r) {
        data.players = r.data
      }
    }

    const getScriptDMPlayer = async () => {
      const r = await scriptDMDicDataApi({ id: data.form.scriptId })
      if (r) {
        let playerIds = []
        r.data.forEach(item => {
          playerIds.push(item.dicCode + '')
        })
        data.form.player_ids = playerIds
      }
    }


    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      data.visible = true
      data.form.scriptId = id
      await getAllDMPlayer()
      await getScriptDMPlayer()
    }

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
      refForm.value.validate(async valid => {
        if (valid) {
          const checkedNodes = refCascader.value.getCheckedNodes(true)
          let playerIds = []
          checkedNodes.forEach(item => {
            playerIds.push.apply(playerIds, item.pathValues)
          })
          const params = {
            id: data.form.scriptId,
            keys: playerIds.join(',')
          }
          const r = await updateScriptDMApi(params)
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
      refCascader,
      ...toRefs(data),
      rules,
      cascaderProps,
      init,
      submit,
      dialogClosedHandle
    }
  }
})
</script>
