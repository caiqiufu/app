<template>
  <Container>
    <template #default>
      <el-row>{{ $t('unieap.bakerstreet.playmgt.dmShow') }}</el-row>
      <el-divider></el-divider>
      <el-row>
        <el-col align="middle" v-for="(item, i) in dmShowList" :key="item.id" :span="4">
          <el-button v-repeat @click="addEditHandle(i, item)">
            {{ $t('unieap.comm.edit') }}</el-button>
          <div class="grid-content bg-purple">
            <el-image :src="item.avatarUrl" width="180" height="200" alt="无法加载">
            </el-image>
          </div>
          {{ item.brief }}
        </el-col>
      </el-row>
      <AddEdit ref="refAddEdit" v-if="visible" @refresh="getList" />
    </template>
  </Container>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { defineComponent, nextTick, onBeforeMount, reactive, ref, toRefs } from 'vue'
//自定包引入
import AddEdit from './components/add-edit'
import { dmShowPageApi } from '@/api/unieap/bakerstreet/playmgt'

export default defineComponent({
  components: { AddEdit },
  setup() {
    const { t } = useI18n()
    //数据定义
    const refAddEdit = ref()
    const data = reactive({
      loading: false,
      visible: false,
      action: '',
      form: {
        sort: 'id',
        dir: 'asc'
      },
      dmShowList: []
    })

    const getList = async () => {
      data.loading = true
      const params = {}
      dmShowPageApi(params).then(r => {
        if (r) {
          data.dmShowList = r.data
        }
      })
      nextTick(() => { data.loading = false })
    }

    //编辑
    const addEditHandle = (index, dmhow) => {
      data.visible = true
      nextTick(() => {
        refAddEdit.value.init(index, dmhow)
      })
    }
    //DOM即将挂载,获取列表数据
    onBeforeMount(() => {
      getList()
    })
    return {
      ...toRefs(data),
      refAddEdit,
      //自定方法
      getList,
      addEditHandle
    }
  }
})
</script>

<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 2px;
}

.bg-purple {
  background: #d3dce6;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
  margin: 2px;
}
</style>
