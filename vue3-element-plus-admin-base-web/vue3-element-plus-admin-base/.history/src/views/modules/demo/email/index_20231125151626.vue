<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true">
        <el-form-item>
          <el-input v-model="form.name" :placeholder="$t('form.name')" clearable />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <UImageUploadSingle v-model="form.avatar" :category="form.category" :extKey="form.extKey" />
        </el-form-item>
        <el-form-item label="头像2" prop="avatar">
          <el-upload action="#" list-type="picture-card" :auto-upload="false">
            <i class="el-icon-plus"></i>
            <div :v-model="fileList" :v-for="(file,index)">
              <img class="el-upload-list__item-thumbnail" :src="file" alt="">
              <span class="el-upload-list__item-actions">
                <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                  <i class="el-icon-zoom-in"></i>
                </span>
                <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDownload(file)">
                  <i class="el-icon-download"></i>
                </span>
                <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
                  <i class="el-icon-delete"></i>
                </span>
              </span>
            </div>
          </el-upload>
          <el-dialog :v-model="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>

        </el-form-item>
      </el-form>
    </template>
  </Container>
</template>
  
<script>
import { defineComponent, onBeforeMount, reactive, ref, toRefs } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import UImageUploadSingle from '@/components/unieap/file-upload-single'

export default defineComponent({
  components: { UImageUploadSingle },
  setup() {
    const refForm = ref()
    const data = reactive({
      //初始值：hideUpload: false,
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      hideUpload: false,
      noneHandleUrl: '',
      limitHandleUrl: '',
      loading: false,
      visible: false,
      handleForm: {},
      imageUrl: '',
      form: {
        category: 'demo',
        extKey: '123456789',
        fileId:'',
        upload: null,
        name: '',
        avatar: '',
        date: []
      },
      list: [],
      selection: []
    })
    const onBeforeMount = () => {
    }

    // 处理-上传图片-------------------------showHandleImg  noneHandleUrl 控制的显示隐藏------------
    const handlePictureCardPreview = (file, fileList) => {
      data.dialogImageUrl.value = file.response.data.url;
      data.dialogVisible = true;
    };
    const imgHandleChange = (file, fileList) => {
      data.noneHandleUrl = fileList.length >= data.limitHandleUrl;
    };
    const handleExceed = (res, file) => {
      ElMessage.error("最多上传三张图片");
    };
    const handleRemove = (file, fileList) => {
      data.noneHandleUrl = fileList.length >= data.limitHandleUrl;
      let arr = fileList.map((item) => {
        return item.response.data.url;
      });
      data.handleForm.handle_pic = arr;
    };
    const beforeUploadImg = (file) => {
      const isImg = /^image\/(jpeg|png|jpg)$/.test(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isImg) {
        ElMessage({
          message: "上传图片格式不对哦",
          type: "warning",
        });
        return false;
      }
      if (!isLt2M) {
        ElMessage({
          message: "上传图片大小不能超过 2MB!",
          type: "warning",
        });
        return false;
      }
      return isImg && isLt2M;
    };
    const handleImageSuccess = (res, fileList) => {
      // console.log(res, "file");
      //上传成功之后放到囊中
      data.handleForm.handle_pic.push(res.data.url);
      ElMessage.success("上传成功");
    };



    return {
      refForm,
      ...toRefs(data),
      onBeforeMount,
      handlePictureCardPreview,
      imgHandleChange,
      handleExceed,
      handleRemove,
      beforeUploadImg,
      handleImageSuccess
    }
  }
})
</script>
  
<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}

.uoloadSty .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}

.disUoloadSty .el-upload--picture-card {
  display: none;
  /* 上传按钮隐藏 */
}
</style>
  