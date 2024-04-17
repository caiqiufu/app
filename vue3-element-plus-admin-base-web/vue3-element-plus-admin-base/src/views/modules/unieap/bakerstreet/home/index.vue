<template>
  <ContainerCustom>
    <template #default>
      <el-row :gutter="20">
        <el-col :span="14">
          <el-row :gutter="20">
            <el-image width="200px;" height="200px;" src="https://dynamic-image.yesky.com/600x-/uploadImages/2021/055/41/MUY4765VKP38.jpg"></el-image>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <DMBlacklist/>
            </el-col>
            <el-col :span="12">
              <PlayerBlacklist />
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="10">
          <Roomlist />
        </el-col>
      </el-row>
    </template>
  </ContainerCustom>
</template>

<script >
import { defineComponent, reactive, toRefs } from 'vue'

import ContainerCustom from '@/components/container-custom'
import DMBlacklist from './components/dmblacklist'
import PlayerBlacklist from './components/playerblacklist'
import Roomlist from './components/roomlist'

export default defineComponent({
  components: {DMBlacklist,PlayerBlacklist,Roomlist },
  setup() {
    const data = reactive({
    })

    return {
      ...toRefs(data)
    }
  }
})
</script>

<style lang="scss" scoped>

</style>
