<template>
  <el-dialog width="800px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.roleName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.comm.gender')" prop="sex">
        <el-radio-group v-model="form.sex">
          <el-radio :label="'M'">{{ $t('unieap.comm.male') }}</el-radio>
          <el-radio :label="'F'">{{ $t('unieap.comm.fmale') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.brief')" prop="brief">
        <el-input type="textarea" :rows="3" v-model="form.brief" :placeholder="$t('unieap.comm.plsInput')"
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.rolePicture')" prop="avatarUrl">
        <UImageUploadSingle v-model="form.avatarUrl" @uploadFinishedHandle="uploadFinishedHandle" category="role_picture"
          :extKey="roleId.toString()" size="100px" :fileList="form.fileList" processHandle="testHandler" />
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
import { computed, defineComponent,onBeforeMount, nextTick, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
import { getDBSeqApi } from '@/api/unieap/comm'
import { clearJson } from '@/utils'
//自定包引入
import { roleInfoApi, createRoleApi, editRoleApi } from '@/api/unieap/bakerstreet/scriptmgt'
import { uploadApi} from '@/api/unieap/file'
import UImageUploadSingle from '@/components/unieap/file-upload-single'

export default defineComponent({
  components: { UImageUploadSingle },
  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      action: uploadApi(),
      scriptId: '',
      roleId: '',
      isEdit: false,
      url: '',
      uploadData: {
        extKey: '',
        category: '',
        handlerName: ''
      },
      fileList: [],
      form: {
        id: null,
        scriptId: '',
        code: '',
        name: '',
        sex: '',
        brief: '',
        avatarUrl: '',
        fileList:[]
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.roleName')], trigger: 'blur' }],
        sex: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.comm.gender')], trigger: 'blur' }]
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (e) => {
      clearJson(data.form)
      data.visible = true
      data.loading = true
      if (e) {
        data.scriptId = e.scriptId
        data.form.scriptId = e.scriptId
        if (e.id) {
          data.form.id = e.id
          data.roleId = e.id
          data.isEdit = true
          const r = await roleInfoApi({ 'id': data.roleId })
          if (r) {
            data.form.code = r.data.code
            data.form.name = r.data.name
            data.form.sex = r.data.sex
            data.form.brief = r.data.brief
            data.form.avatarUrl = r.data.url
            data.form.fileList = r.data.fileList
            data.fileList = r.data.fileList
          }
        } else {
          getDBSeqApi(null).then(r => {
            if (r) {
              data.form.id = r.data.toString()
              data.roleId = r.data.toString()
              data.isEdit = false
            }
          })
        }
      }
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
          const formData = {
            id: data.form.id,
            scriptId: data.form.scriptId,
            code: data.form.code,
            name: data.form.name,
            sex: data.form.sex,
            brief: data.form.brief,
            file: JSON.stringify(data.fileList)
          }
          const r = data.isEdit ? await editRoleApi(formData) : await createRoleApi(formData)
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
    //上传成功后刷新数据
    const uploadFinishedHandle = (e) => {
      data.fileList = e.fileList
    }

    /**
   * @description: 弹窗关闭动画结束时的回调
   * @param {*}
   * @return {*}
   */
    const dialogClosedHandle = () => {
      refForm.value.resetFields()
    }
    onBeforeMount(() => {
    })
    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle,
      uploadFinishedHandle
    }
  }
})
</script>
<style scoped lang="scss">

</style>