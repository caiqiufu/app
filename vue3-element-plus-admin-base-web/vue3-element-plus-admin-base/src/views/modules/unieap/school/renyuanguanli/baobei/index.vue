<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane :label="$t('unieap.school.renyuanguanli.baobei.studentKids')" name="studentKids">
      <StudentKids ref="refStudentKids" v-if="activeName == 'studentKids'" />
    </el-tab-pane>
    <el-tab-pane :label="$t('unieap.school.renyuanguanli.baobei.leaveKids')" name="leaveKids">
      <LeaveKids ref="refLeaveKids" v-if="activeName == 'leaveKids'" />
    </el-tab-pane>
  </el-tabs>
</template>
  
<script>
import { defineComponent, onBeforeMount, reactive, ref, toRefs } from 'vue'

import StudentKids from './components/studentKids'
import LeaveKids from './components/leaveKids'

export default defineComponent({
  components: { StudentKids,LeaveKids},
  setup() {
    const refLeaveKids = ref()
    const refStudentKids = ref()
    const data = reactive({
      activeName:'studentKids'
    })
    const handleClick = (tab, event)=>{
      data.activeName = tab.name
    }

    onBeforeMount(() => {
    })

    return {
      refLeaveKids,
      refStudentKids,
      ...toRefs(data),
      handleClick
    }
  }
})
</script>
  
<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
  