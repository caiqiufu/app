<template>
  <el-upload class="update-container" list-type="picture-card" :style="{ height: size, width: size }" :action="action"
    :headers="{
      [tokenKey]: token
    }" :show-file-list="showFileList" :limit="limitCount" :file-list="fileList" :data="uploadData"
    :on-exceed="exceedHandle" :on-success="successUploadHandle" :before-upload="beforeUploadHandle"
    :on-progress="uploadProcessHandle" :before-remove="beforeRemoveHandle" :on-preview="previewHandle"
    :on-remove="removeHandle" :disabled="disabled" multiple accept="image/*">
    <Iconfont name="plus" />
  </el-upload>
  <el-dialog v-model="dialogVisible">
    22少时诵诗书所
    <img width="100%" src="http://localhost:8806/slipper/file/poster_picture/febfd245-1282-4bc8-9c49-c95bbed2a1f6.jpg" alt="">
  </el-dialog>
</template>

<script >
import { defineComponent, reactive, toRefs } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import useModel from '@/mixins/model'
import { TOKEN_KEY, SUCCESS_CODE } from '@/utils/constant'
import { uploadApi, deleteApi } from '@/api/unieap/file'

export default defineComponent({
  props: {
    //文件处理类
    processHandle: {
      type: String,
      default: () => ''
    },
    //文件分类
    category: {
      type: String,
      default: () => ''
    },
    //外部主键
    extKey: {
      type: String,
      default: () => ''
    },
    limitCount: {
      type: Number,
      default: () => 99
    },
    modelValue: {
      type: [String, Array],
      required: true,
      default: () => ''
    },
    // 大小 默认 100px
    size: {
      type: String,
      default: () => '100px'
    },
    disabled: {
      type: Boolean,
      default: () => false
    },
    showFileList: {
      type: Boolean,
      default: () => true
    },
    fileList: {
      //{name:'',url:'',fileId:''}
      type: Array,
      default: () => []
    }
  },
  emits: ['uploadFinishedHandle','update:modelValue'],
  setup(props, { emit }) {
    const store = useStore()
    const value = useModel(props)
    const data = reactive({
      action: uploadApi(),
      tokenKey: TOKEN_KEY,
      token: store.getters['administrator/tokenVal'],
      dialogImageUrl: '',
      dialogVisible: false,
      uploadData: null,
      uploadedFileList: []
    })
    /**文件上传成功 */
    const successUploadHandle = (res, file, fileList) => {
      if (SUCCESS_CODE.includes(res.code)) {
        let isExist = false
        data.uploadedFileList.forEach(fileObj => {
          if (fileObj.name === res.data[0].originalName) {
            fileObj.fileId = res.data[0].id
            fileObj.url = res.data[0].url
            isExist = true
          }
        })
        if (!isExist) {
          data.uploadedFileList.push({ name: res.data[0].originalName, fileId: res.data[0].id, url: res.data[0].url })
        }
        //刷新数据
        emit('uploadFinishedHandle', { file: res.data[0], fileList: data.uploadedFileList })
      } else {
        ElMessage({
          message: res.message,
          type: 'warning'
        })
      }
    }
    /**上传文件大小和格式校验 */
    const beforeUploadHandle = (file) => {
      data.uploadData = { extKey: props['extKey'], category: props['category'], handlerName: props['processHandle'] }
    }
    const uploadProcessHandle = () => {

    }
    /**击文件列表中已上传文件进行预览 */
    const previewHandle = (file) => {
      data.dialogImageUrl = file.url;
      data.dialogVisible = true;
    }
    /**删除文件前校验 */
    const beforeRemoveHandle = (file, fileList) => {

    }
    /**删除文件 */
    const removeHandle = (file, fileList) => {
      if(file.fileId){
        deleteApi({fileId:file.fileId}).then(r => {
        if (r) {
          let deleteObjIndex = data.uploadedFileList.findIndex(item => {
            if (item.name === file.name) {
              return true
            }
          })
          data.uploadedFileList.splice(deleteObjIndex, 1)
        }
      })
      }     
    }
    /**文件超出限制个数提示*/
    const exceedHandle = (files, fileList) => {

    }
    return {
      value,
      ...toRefs(data),
      successUploadHandle,
      beforeUploadHandle,
      uploadProcessHandle,
      previewHandle,
      beforeRemoveHandle,
      removeHandle,
      exceedHandle
    }
  }
})
</script>

<style lang="scss" scoped>
::v-deep(.el-upload),
::v-deep(.el-upload-list) {
  height: 100%;
  width: 100%;
}

::v-deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;

  &:hover {
    border-color: var(--el-color-primary);
    color: var(--el-color-primary);
  }
}

.update-container {
  img {
    object-fit: contain;
  }
}
</style>
