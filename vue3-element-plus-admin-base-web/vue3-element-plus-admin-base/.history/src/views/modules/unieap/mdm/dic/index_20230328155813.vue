<template>
  <ContainerSidebar ref="refContainerSidebar" :scroll="false">
    <template #sidebar>
      <Sidebar v-model="active" @change="changeHandle" />
    </template>
    <template #default>
      <div class="name-box margin_b-20 font-size-20">{{form.name}}&nbsp;</div>
      <el-row v-show="active">
        <el-col
          :xs="24"
          :sm="24"
          :md="16"
          :lg="14"
          :xl="10">
          <el-form
            v-loading="loading"
            :model="form"
            :rules="rules"
            ref="refForm"
            @keyup.enter="submit()"
            label-position="top">
            <el-form-item label="中文名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="中文名称"
                maxlength="20"
                show-word-limit
                :readonly="!havePermission('\menu:create|menu:update', '|')" />
            </el-form-item>
            <el-form-item label="英文名称" prop="code">
              <el-input
                v-model="form.code"
                placeholder="英文名称"
                maxlength="30"
                show-word-limit
                :readonly="!havePermission('menu:create|menu:update', '|')" />
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input-number
                v-model="form.sort"
                :min="1"
                controls-position="right"
                :disabled="!havePermission('menu:create|menu:update', '|')" />
            </el-form-item>
            <el-form-item>
              <el-button v-permission="'menu:update'" type="primary" @click="submit">保存</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </template>
  </ContainerSidebar>
</template>

<script>
import { defineComponent, nextTick, onBeforeMount, reactive, ref, toRefs } from 'vue'

import { ElMessage } from 'element-plus'
import ContainerSidebar from '@/components/container-sidebar'
import Sidebar from './components/sidebar'

import { havePermission } from '@/utils'
import { MenuTypes } from '@/utils/dictionary'
import { VIRTUAL_ID_KEY } from './index.js'

import { infoApi, addApi, editApi } from '@/api/unieap/mdm/dic'

export default defineComponent({
  components: { ContainerSidebar, Sidebar },
  setup() {
    const refContainerSidebar = ref()
    const refForm = ref()
    const data = reactive({
      active: '',
      loading: false,
      MenuTypes,
      form: {
        id: null,
        code: '',
        name: '',
        type: '',
        sort: 1,
        parentId: ''
      },
      row: null, // todo: 引用传递 用于编辑之后修改 列表数据
      parentType: 0 // 父级的类型
    })

    const rules = reactive(function() {
      return {
        name: [{ required: true, message: '请输入中文名称', trigger: 'blur' }],
        code: [{ required: false, message: '请输入英文名称', trigger: 'blur' }]
      }
    }())

    const getInfo = async () => {
      data.loading = true
      const r = await infoApi({id: data.form.id, type: data.form.type})
      if (r) {
        data.form.id = r.data.id
        data.form.name = r.data.text
        data.form.code = r.data.attr1 // code
        data.form.type = r.data.attr2 // type
        data.form.sort = r.data.attr3 // seq
        data.form.parentId = r.data.parentId
      }
      nextTick(() => { data.loading = false })
    }

    const clearFrom = () => {
      data.form.id = null,
      data.form.name = ''
      data.form.type = ''
      data.form.code = ''
      data.form.sort = ''
      data.form.parentId = ''
    }

    const clearRouterParams = () => {
      
    }

    const changeHandle = ({ row, parentType }) => {
      refContainerSidebar.value.setScrollTop()
      data.row = row
      data.parentType = parentType
      if ((row.id + '').includes(VIRTUAL_ID_KEY)) {
        clearFrom()
        data.form.name = row.name
        data.form.code = row.code
        data.form.parentId = row.parentId
        data.form.type = row.type
        data.form.sort = 1
      } else {
        data.form.id = row.id
        data.form.type = row.type
        getInfo()
      }
    }

    const buttonHandle = (val) => {
      let result = false
      switch (data.parentType) {
        case 0:
          if (val === 2) {
            result = true
          }
          break
        case 1:
          if (val === 0 || val === 3 || val === 4) {
            result = true
          }
          break
      }
      return result
    }

    const submit = () => {
      refForm.value.validate(async valid => {
        if (valid) {
          const r = data.form.id ? await editApi(data.form) : await addApi(data.form)
          if (r) {
            ElMessage({
              message: '操作成功!',
              type: 'success'
            })
            if (r.data) {
              data.row.id = r.data
              data.form.id = r.data
            }
            data.row.name = data.form.text
            data.row.code = data.form.code
            data.row.type = data.form.type
          }
          //
        }
      })
    }

    return {
      refContainerSidebar,
      refForm,
      ...toRefs(data),
      rules,
      clearRouterParams,
      changeHandle,
      buttonHandle,
      submit,
      havePermission
    }
  }
})
</script>

<style lang="scss" scoped>
.name-box {
  font-weight: bold;
}
</style>
