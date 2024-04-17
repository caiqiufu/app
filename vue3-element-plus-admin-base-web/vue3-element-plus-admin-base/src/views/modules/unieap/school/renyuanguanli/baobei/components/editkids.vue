<template>
  <el-dialog width="800px" :title="$t('unieap.comm.update')" v-model="visible" :close-on-click-modal="false"
    @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane :label="$t('unieap.school.renyuanguanli.baobei.basicInfo')" name="basicinfo">
        <keep-alive>
          <Basicinfo ref="refBasicinfo" v-show="activeName == 'basicinfo'" name="basicinfo" @refresh="closeDialog" />
        </keep-alive>
      </el-tab-pane>
      <el-tab-pane :label="$t('unieap.school.renyuanguanli.baobei.registerInfo')" name="registerinfo">
        <keep-alive>
          <Registerinfo ref="refRegisterinfo" v-show="activeName == 'registerinfo'" name="registerinfo"
            @refresh="closeDialog" />
        </keep-alive>
      </el-tab-pane>
    </el-tabs>

  </el-dialog>
</template>

<script>
import { defineComponent, nextTick, reactive, ref, toRefs } from 'vue'
import Basicinfo from './basicinfo'
import Registerinfo from './registerinfo'

export default defineComponent({
  components: { Basicinfo, Registerinfo },

  emits: ['refresh'],
  setup(_props, { emit }) {
    const refBasicinfo = ref()
    const refRegisterinfo = ref()
    const data = reactive({
      loading: false,
      visible: false,
      basicInfoLoaded: false,
      registerInfoLoaded: false,
      activeName: 'basicinfo',
      id: null,
      roles: []
    })
    const handleClick = (tab, event) => {
      data.activeName = tab.props.name
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

    const closeDialog = () => {
      data.visible = false
      emit('refresh')

    }
    const init = async (id) => {
      data.visible = true
      data.loading = true
      data.id = id
      nextTick(() => {
        refBasicinfo.value.init(data.id)
        refRegisterinfo.value.init(data.id)
      })
    }

    return {
      refBasicinfo,
      refRegisterinfo,
      ...toRefs(data),
      init,
      dialogClosedHandle,
      handleClick,
      closeDialog
    }
  }
})
</script>
