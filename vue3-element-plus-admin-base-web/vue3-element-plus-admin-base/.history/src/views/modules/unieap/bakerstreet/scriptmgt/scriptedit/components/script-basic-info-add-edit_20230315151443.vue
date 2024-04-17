<template>
  <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="100px" label-position="right">
    <el-form-item style="text-align: center">
      <el-button type="primary" @click="handleNext" :disabled="disabledValue">下一步，填写角色信息</el-button>
    </el-form-item>
    <el-row :gutter="10" type="flex" justify="start">
      <el-col :span="12">
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.scriptName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('unieap.comm.plsInput')" maxlength="32" show-word-limit />
        </el-form-item>
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.scriptChapter')" prop="scriptChapter">
          <el-select v-model="form.scriptChapter" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="(item, index) in optionScriptChapter" :key="index" :value="item.dicCode"
              :label="item.dicName">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.type')" prop="type_ids">
          <el-checkbox-group v-model="form.type_ids">
            <el-checkbox v-for="scriptType in optionScriptType" :key="scriptType.dicCode" :label="scriptType.dicCode">{{
              scriptType.dicName
            }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.category')" prop="category_ids">
          <el-checkbox-group v-model="form.category_ids">
            <el-checkbox v-for="category in optionCategory" :key="category.dicCode" :label="category.dicCode">{{
              category.dicName
            }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.duration')" prop="duration">
          <el-radio-group v-model="form.duration">
            <el-radio v-for="duration in optionDuration" :key="duration.dicCode" :label="duration.dicCode">{{
              duration.dicName
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item v-for="(clueInfo, index) in form.clueInfoList"
          :label="$t('unieap.bakerstreet.scriptmgt.clueChapterName') + (index + 1)" :key="clueInfo.code">
          <el-input v-model="clueInfo.name" :placeholder="$t('unieap.bakerstreet.scriptmgt.clueChapterName')"
            style="width:300px"></el-input>
          <el-button @click.prevent="removeClue(clueInfo)" :disabled="(form.clueInfoList.length <= 1)">{{
            $t('unieap.comm.delete')
          }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="addClue">
            <el-icon>
              <Plus />
            </el-icon>
            {{ $t('unieap.comm.create') }}
          </el-button>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="10" type="flex" justify="start">
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.playerCount')" prop="playerCount">
        <el-radio-group v-model="form.playerCount">
          <el-radio v-for="playerCount in optionPlayerCount" :key="playerCount.dicCode" :label="playerCount.dicCode">{{
            playerCount.dicName
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-row>
    <el-row :gutter="10" type="flex" justify="start">
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.poster')" prop="posterUrl">
        <PosterUImageUploadSingle v-model="form.posterUrl" @uploadFinishedHandle="uploadFinishedHandlePoster"
          category="poster_picture" :extKey="scriptId" size="100px" :fileList="form.posterList" processHandle="testHandler" />
      </el-form-item>
    </el-row>
    <el-row :gutter="10" type="flex" justify="start">
      <el-form-item :label="$t('unieap.bakerstreet.scriptmgt.dmGuidebook')" prop="dmGuidebook" style="width:500px">
        <DmUImageUploadMutiple v-model="form.dmGuidebook" @uploadFinishedHandle="uploadFinishedHandleDm"
          category="guidebook_picture" :extKey="scriptId" size="100px" :fileList="form.guidebookList" processHandle="testHandler" />
      </el-form-item>
    </el-row>
    <el-row type="flex" justify="center">
      <el-button v-repeat type="primary" @click="submit()">{{ $t('unieap.comm.save') }}</el-button>
    </el-row>
  </el-form>
</template>
<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, onBeforeMount, nextTick, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
//自定包引入
import { infoDicDataApi } from '@/api/unieap/comm'
import PosterUImageUploadSingle from '@/components/unieap/file-upload-single'
import DmUImageUploadMutiple from '@/components/unieap/file-upload-multiple'
import { scriptInfoApi, createScriptApi, editScriptApi } from '@/api/unieap/bakerstreet/scriptmgt'

export default defineComponent({
  components: { PosterUImageUploadSingle, DmUImageUploadMutiple},
  props: {
    //value: Object,//组件v-model中绑定的变量传递
    isEdit: {
      type: Boolean,
      default: false
    },
    scriptId: {
      type: String,
      default: () => ''
    }
  },
  emits: ['refresh', 'prevStep', 'nextStep'],
  setup(props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    //const value = useModel(props)
    const data = reactive({
      loading: false,
      visible: false,
      disabledValue: true,
      isEdit: props['props'],
      scriptId: props['scriptId'],
      posterFileList: [],
      dmGuidebookFileList: [],
      optionScriptChapter: [],
      optionScriptType: [],
      optionCategory: [],
      optionDuration: [],
      optionPlayerCount: [],
      form: {
        id: '',
        code: '',
        name: '',
        posterUrl: '',
        typeList: '',
        type_ids: [],
        category: '',
        category_ids: [],
        duration: '',
        playerCount: '',
        clueInfoList: [],
        scriptChapter: '',
        publishFlag: '',
        publishDate: '',
        guidebookList: [],
        posterList: [],
        brief: '',
        extKey: ''

      },
      roles: []
    })
    const rules = computed(() => {
      return {
        name: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.scriptName')], trigger: 'blur' }],
        scriptChapter: [{ required: true, message: [t('unieap.comm.plsInput')] + [t('unieap.bakerstreet.scriptmgt.scriptChapter')], trigger: 'blur' }]
      }
    })
    const init = async (e) => {
      data.scriptId = e.scriptId
      data.form.id = e.scriptId
      data.isEdit = e.isEdit
      data.disabledValue = !data.isEdit
      infoDicDataApi({ 'groupCode': 'SCRIPT_CHAPTER' }).then(r => {
        if (r) {
          data.optionScriptChapter = r.data
        }
      })
      infoDicDataApi({ 'groupCode': 'SCRIPT_TYPE' }).then(r => {
        if (r) {
          data.optionScriptType = r.data
        }
      })
      infoDicDataApi({ 'groupCode': 'SCRIPT_CATEGORY' }).then(r => {
        if (r) {
          data.optionCategory = r.data
        }
      })
      infoDicDataApi({ 'groupCode': 'SCRIPT_DURATION' }).then(r => {
        if (r) {
          data.optionDuration = r.data
        }
      })
      infoDicDataApi({ 'groupCode': 'SCRIPT_PLAYERCOUNT' }).then(r => {
        if (r) {
          data.optionPlayerCount = r.data
        }
      })
      if (data.isEdit) {
        getList()
      }
    }
    const handlePrev = () => {
      emit('prevStep')
    }
    const handleNext = () => {
      emit('nextStep')
    }

    //分页数据,根据需求修改
    const getList = () => {
      scriptInfoApi({ 'id': data.scriptId }).then(r => {
        if (r) {
          data.form = r.data
          if (r.data.clueInfoList.length === 0) {
            data.form.clueInfoList = [{id:null,scriptId:data.scriptId, code: Date.now(), name: '' }]
          }
          if (r.data.typeList) {
            data.form.type_ids = r.data.typeList.split(',')
          }
          if (r.data.category) {
            data.form.category_ids = r.data.category.split(',')
          }
          data.posterFileList = r.data.posterList
          data.dmGuidebookFileList = r.data.guidebookList
        }
      })
    }
    //上传成功后刷新数据
    const uploadFinishedHandlePoster = (e) => {
      data.posterFileList = e.fileList
    }
    //上传成功后刷新数据
    const uploadFinishedHandleDm = (e) => {
      data.dmGuidebookFileList = e.fileList
    }
    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
      data.loading = true
      refForm.value.validate(async valid => {
        if (valid) {
          if (!clueInfoValidate()) {
            return
          }
          const formData = {
            type: data,
            id: data.form.id,
            code: data.form.code,
            name: data.form.name,
            posterUrl:data.form.posterUrl,
            poster: JSON.stringify(data.posterFileList),
            guidebook: JSON.stringify(data.dmGuidebookFileList),
            typeList: data.form.type_ids.join(','),
            category: data.form.category_ids.join(','),
            duration: data.form.duration,
            playerCount: data.form.playerCount,
            clueList: JSON.stringify(data.form.clueInfoList),
            scriptChapter: data.form.scriptChapter
          }
          const r = data.isEdit ? await editScriptApi(formData) : await createScriptApi(formData)
          if (r) {
            data.disabledValue = false
            ElMessage({
              message: t('unieap.comm.optSuccess'),
              type: 'success'
            })
          }
        }
      })
      nextTick(() => { data.loading = false })
    }

    const clueInfoValidate = () => {
      const reg = /^1[3456789]\d{9}$/
      if (data.form.clueInfoList.length === 0) {
        ElMessage({
          message: t('unieap.bakerstreet.scriptmgt.clueValidate'),
          type: 'error'
        })
        return false
      }
      for (let con of data.form.clueInfoList) {
        if (con.code === '' || con.name === '') {
          ElMessage({
            message: t('unieap.bakerstreet.scriptmgt.clueValidate'),
            type: 'error'
          })
          return false
        }
      }
      return true
    }
    const removeClue = (item) => {
      var index = data.form.clueInfoList.indexOf(item)
      if (index !== -1) {
        data.form.clueList.splice(index, 1)
      }
    }

    const addClue = () => {
      data.form.clueInfoList.push({id:null,scriptId:data.scriptId, code: Date.now(), name: '' });
    }

    onBeforeMount(() => {
      data.form.id = data.scriptId
      data.form.extKey = data.scriptId
    })
    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      getList,
      handlePrev,
      handleNext,
      uploadFinishedHandlePoster,
      uploadFinishedHandleDm,
      submit,
      removeClue,
      addClue
    }
  }
})
</script>
