<template>
  <el-dialog width="800px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>

    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.roleName')" prop="roleId">
        <el-select v-model="form.roleId" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="(item, index) in optionRoleList" :key="index" :value="item.dicCode" :label="item.dicName" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.scriptChapter')" prop="chapterNumber">
        <el-select v-model="form.chapterNumber" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="(item, index) in optionChapterList" :key="index" :value="item.dicCode"
            :label="item.dicName">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.brief')" prop="brief">
        <el-input type="textarea" :rows="3" v-model="form.brief" :placeholder="$t('unieap.comm.plsInput')"
          style="width:300px" />
      </el-form-item>
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.rolePicture')" prop="file">
        <UImageUploadMutiple v-model="form.file" @uploadFinishedHandle="uploadFinishedHandle" category="chapter_picture"
          :extKey="chapterId.toString()" size="100px" :fileList="form.fileList" processHandle="testHandler" />
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
import { getDBSeqApi } from '@/api/unieap/comm'
//自定包引入
import { roleDicDataApi, chapterNumberDicDataApi } from '@/api/unieap/bakerstreet/dic'
import UImageUploadMutiple from '@/components/unieap/file-upload-multiple'
import { chapterInfoApi, createChapterApi, editChapterApi } from '@/api/unieap/bakerstreet/scriptmgt'

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
      chapterId: '',
      isEdit: false,
      optionRoleList: [],
      optionChapterList: [],
      fileList: [],
      form: {
        id: null,
        scriptId: '',
        roleId: '',
        chapterNumber: '',
        name: '',
        brief: '',
        fileList: []
      },
      roles: []
    })
    const rules = computed(() => {
      return {
        roleId: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.roleName')], trigger: 'blur' }],
        chapterNumber: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.chapterNumber')], trigger: 'blur' }]
      }
    })
    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (e) => {
      data.visible = true
      data.loading = true
      if (e) {
        data.scriptId = e.scriptId
        data.form.scriptId = e.scriptId
        if (e.id) {
          data.form.id = e.id
          data.chapterId = e.id
          data.isEdit = true
          const r = await chapterInfoApi({ 'id': data.chapterId })
          if (r) {
            data.form.roleId = r.data.roleId.toString()
            data.form.chapterNumber = r.data.chapterNumber
            data.form.name = r.data.name
            data.form.brief = r.data.brief
            data.form.fileList = r.data.fileList
            data.fileList = r.data.fileList
          }
        } else {
          getDBSeqApi(null).then(r => {
            if (r) {
              data.form.id = r.data.toString()
              data.chapterId = r.data.toString()
              data.isEdit = false
            }
          })
        }
      }
      const dicRoleDicData = await roleDicDataApi({ id: data.scriptId })
      if (dicRoleDicData) {
        data.optionRoleList = dicRoleDicData.data
      }
      const dicChapterNumber = await chapterNumberDicDataApi({ id: data.scriptId })
      if (dicChapterNumber) {
        data.optionChapterList = dicChapterNumber.data
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
            roleId: data.form.roleId,
            chapterNumber: data.form.chapterNumber,
            name: data.form.name,
            brief: data.form.brief,
            file: JSON.stringify(data.fileList)
          }
          const r = data.isEdit ? await editChapterApi(formData) : await createChapterApi(formData)
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
      uploadFinishedHandle
    }
  }
})
</script>
<style scoped lang="scss">

</style>