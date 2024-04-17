<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="100px" label-position="right">
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.clueChapterName')" prop="clueId" style="width:300px">
        <el-select v-model="form.clueId" :placeholder="$t('unieap.comm.plsSelect')" @change="selectChangeClue">
          <el-option v-for="(item, index) in optionClueList" :key="index" :value="item.dicCode" :label="item.dicName">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-show="isEdit" :label="$t('unieap.bakerstreet.scriptmgt.brief')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.cluePicture')" prop="url">
          <UImageUploadMutiple v-model="form.file" @uploadFinishedHandle="uploadFinishedHandle" category="clue_picture"
          :extKey="clueId.toString()" size="100px" :fileList="form.fileList" processHandle="testHandler" />
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
import { clearJson } from '@/utils'
//自定包引入
import { clueDicDataApi } from '@/api/unieap/bakerstreet/dic'
import UImageUploadMutiple from '@/components/unieap/file-upload-multiple'
import { clueDetailInfoApi, createClueDetailByBatchApi, editClueDetailApi } from '@/api/unieap/bakerstreet/scriptmgt'

export default defineComponent({
  components: { UImageUploadMutiple },
  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      scriptId: '',
      clueId: '',
      isEdit: false,
      optionClueList: [],
      fileList: [],
      form: {
        id: null,
        clueId: null,
        clueName: '',
        code:'',
        name: '',
        url: '',
        fileList: []
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        clueId: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.clueChapterName')], trigger: 'blur' }]
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
      data.isEdit = false
      data.scriptId = e.scriptId
      const dicClueDicData = await clueDicDataApi({ id: data.scriptId })
      if (dicClueDicData) {
        data.optionClueList = dicClueDicData.data
      }
      if (e.id) {
        data.form.id = e.id
        data.isEdit = true
        const r = await clueDetailInfoApi({ 'id': data.form.id })
        if (r) {
          data.clueId = r.data.clueId.toString()
          data.form.clueId = r.data.clueId.toString()
          data.form.code = r.data.code
          data.form.name = r.data.name
          data.form.url = r.data.url
          data.form.fileList = r.data.fileList
          data.fileList = r.data.fileList
        }
      }
      nextTick(() => { data.loading = false })
    }
    const selectChangeClue=(value)=>{
      data.clueId = value
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
            clueId: data.form.clueId,
            code: data.form.code,
            name: data.form.name,
            scriptId:data.scriptId,
            file: JSON.stringify(data.fileList)
          }
          const r = data.isEdit ? await editClueDetailApi(formData) : await createClueDetailByBatchApi(formData)
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

    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle,
      uploadFinishedHandle,
      selectChangeClue
    }
  }
})
</script>
<style scoped lang="scss">

</style>