<template>
  <Container>
    <template #header>
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="基本基本信息"></el-step>
        <el-step title="剧本角色列表"></el-step>
        <el-step title="剧本章节列表"></el-step>
        <el-step title="剧本线索列表"></el-step>
      </el-steps>
    </template>
    <template #default>
      <ScriptBasicInfoEdit ref="refScriptBasicInfoEdit" v-show="showStatus[0]" :isEdit="isEdit" :scriptId="scriptId"
        @nextStep="nextStep" />
      <ScriptRoleInfoEdit ref="refScriptRoleInfoEdit" v-show="showStatus[1]" :isEdit="isEdit" :scriptId="scriptId"
        @nextStep="nextStep" @prevStep="prevStep" />
      <ScriptChapterInfoEdit ref="refScriptChapterInfoEdit" v-show="showStatus[2]" :isEdit="isEdit" :scriptId="scriptId"
        @nextStep="nextStep" @prevStep="prevStep" />
      <ScriptClueInfoEdit ref="refScriptClueInfoEdit" v-show="showStatus[3]" :isEdit="isEdit" :scriptId="scriptId"
        @prevStep="prevStep" @finishCommit="finishCommit" />
    </template>
  </Container>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { defineComponent, onBeforeMount, reactive, ref, toRefs } from 'vue'
import { useRoute } from 'vue-router'
//自定包引入
import { getDBSeqApi } from '@/api/unieap/comm'
import ScriptBasicInfoEdit from './components/script-basic-info-add-edit'
import ScriptRoleInfoEdit from './components/script-role-info-add-edit'
import ScriptChapterInfoEdit from './components/script-chapter-info-add-edit'
import ScriptClueInfoEdit from './components/script-clue-info-add-edit'



export default defineComponent({
  components: { ScriptBasicInfoEdit, ScriptRoleInfoEdit, ScriptChapterInfoEdit, ScriptClueInfoEdit },
  setup() {
    const { t } = useI18n()
    const route = useRoute()
    //数据定义
    const refScriptBasicInfoEdit = ref()
    const refScriptRoleInfoEdit = ref()
    const refScriptChapterInfoEdit = ref()
    const refScriptClueInfoEdit = ref()
    const data = reactive({
      loading: false,
      visible: false,
      active: 0,
      isEdit: false,
      scriptId: '',
      showStatus: [true, false, false, false],
      form: {
        id: ''
      }
    })

    const hideAll = () => {
      for (let i = 0; i < data.showStatus.length; i++) {
        data.showStatus[i] = false;
      }
    }
    const prevStep = () => {
      if (data.active > 0 && data.active < data.showStatus.length) {
        data.active--;
        hideAll();
        data.showStatus[data.active] = true
      }
      initParams()
    }
    const nextStep = () => {
      if (data.active < data.showStatus.length - 1) {
        data.active++;
        hideAll();
        data.showStatus[data.active] = true
      }
      initParams()
    }
    const finishCommit = () => {

    }
    const initParams = () => {
      switch (data.active) {
        case 0:
          setTimeout(() => {
            refScriptBasicInfoEdit.value.init({ scriptId: data.scriptId, isEdit: data.isEdit })
          }, 200)
          break;
        case 1:
          setTimeout(() => {
            refScriptRoleInfoEdit.value.init({ scriptId: data.scriptId, isEdit: data.isEdit })
          }, 200)
          break
        case 2:
          setTimeout(() => {
            refScriptChapterInfoEdit.value.init({ scriptId: data.scriptId, isEdit: data.isEdit })
          }, 200)
          break
        case 3:
          setTimeout(() => {
            refScriptClueInfoEdit.value.init({ scriptId: data.scriptId, isEdit: data.isEdit })
          }, 200)
          break
        default:
          break;
      }
    }

    onBeforeMount(() => {
      //route.query.scriptId = '1653'
      if (route.query.scriptId) {
        data.scriptId = route.query.scriptId
        data.isEdit = true
        setTimeout(() => {
          refScriptBasicInfoEdit.value.init({ scriptId: data.scriptId, isEdit: data.isEdit })
        }, 200)
      } else {
        getDBSeqApi(null).then(r => {
          if (r) {
            data.scriptId = r.data.toString()
            data.isEdit = false
            setTimeout(() => {
              refScriptBasicInfoEdit.value.init({ scriptId: data.scriptId, isEdit: data.isEdit })
            }, 200)
          }
        })
      }
    })

    return {
      ...toRefs(data),
      refScriptBasicInfoEdit,
      refScriptRoleInfoEdit,
      refScriptChapterInfoEdit,
      refScriptClueInfoEdit,
      //自定方法
      hideAll,
      prevStep,
      nextStep,
      finishCommit
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
