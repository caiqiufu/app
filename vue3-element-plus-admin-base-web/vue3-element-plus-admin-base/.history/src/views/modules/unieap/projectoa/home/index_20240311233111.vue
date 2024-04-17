<template>
  <ContainerCustom>
    <template #default>
      <el-row :gutter="20">
        <el-col :span="12">
          <JldailyLogApproveList />
        </el-col>
        <el-col :span="12">
          <UnfinishedWorkList />
        </el-col>
      </el-row>
    </template>
  </ContainerCustom>
</template>

<script >
import { defineComponent, reactive, toRefs } from "vue";

import JldailyLogApproveList from './components/approvelist';
import UnfinishedWorkList from './components/todoworklist';


export default defineComponent({
  components: { JldailyLogApproveList,UnfinishedWorkList },
  setup() {
    const data = reactive({});

    return {
      ...toRefs(data),
    };
  },
});
</script>

<style lang="scss" scoped>
</style>
