<template>
  <el-dialog width="500px" :title="$t('unieap.comm.edit')" v-model="visible" :close-on-click-modal="false"
    @closed="dialogClosedHandle" append-to-body draggable>
    <el-form :model="form" :rules="rules" ref="refForm" label-width="80px" label-position="right">
      <el-form-item v-if="form.uiType === 'select'" :label="form.displayName" prop="attributeValue">
        <el-select v-model="form.attributeValue" :placeholder="$t('unieap.comm.plsSelect')">
          <el-option v-for="item in optionsDatas" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-else-if="form.uiType === 'input'" :label="form.displayName" prop="attributeValue">
        <el-input v-model="form.attributeValue" :placeholder="$t('unieap.comm.plsInput')" style="width:80%" /><span>({{form.unit}})</span>
      </el-form-item>
      <el-form-item v-else-if="form.uiType === 'textarea'" :label="form.displayName" prop="attributeValue">
        <el-input v-model="form.attributeValue" type="textarea" :rows="3" :placeholder="$t('unieap.comm.plsInput')" />
      </el-form-item>
      <el-form-item v-else-if="form.uiType === 'date-picker'" :label="form.displayName" prop="attributeValue">
        <el-date-picker v-model="form.attributeValue" type="date" :placeholder="$t('unieap.comm.plsSelect')">
        </el-date-picker>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button v-repeat type="primary" @click="submit()">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
//自定包引入
import { infoDicDataApi } from '@/api/unieap/comm'
import { isNumber } from '@/unieap/utils/regular'
import { bizHandleApi } from '@/api/unieap/comm'

export default defineComponent({
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/projectoa/projectmgt/projectinfo/'
    const data = reactive({
      visible: false,
      optionsDatas: [],
      form: {
        id: null,
        projectId: '',
        attributeId: '',
        displayName: '',
        attributeName: '',
        attributeValue: '',
        attributeValueDesc: '',
        uiType: '',
        dicgroupCode: '',
        dataType: '',
        unit: ''
      },
      row: null
    })

    const rules = computed(() => {
      return {
        attributeValue: [{ required: true, message: [t('unieap.comm.plsInput')] + [data.form.displayName], trigger: 'blur' }]
      }
    })

    const init = (row) => {
      data.visible = true
      data.row = row // todo: 引用传递 用于编辑之后修改 列表数据
      if (row.uiType === 'select') {
        infoDicDataApi({ 'groupCode': row.dicgroupCode }).then(r => {
          if (r) {
            data.optionsDatas = r.data
          }
        })
      }
      data.form.id = row.id
      data.form.projectId = row.projectId
      data.form.attributeId = row.attributeId
      data.form.displayName = row.displayName
      data.form.attributeName = row.attributeName
      data.form.attributeValue = row.attributeValue
      data.form.attributeValueDesc = row.attributeValueDesc
      data.form.uiType = row.uiType
      data.form.dataType = row.dataType
      data.form.dicgroupCode = row.dicgroupCode
      data.form.unit = row.unit
    }

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     * @author: gumingchen
     */
    const submit = () => {
      refForm.value.validate(valid => {
        if (valid) {
          if (!fieldValidate()) {
            return
          }
          const handleName = 'updateDetail'
          const params = Object.assign({ url: urlPrefix + handleName }, data.form)
          bizHandleApi(params).then(r => {
            if (r) {
              data.visible = false
              ElMessage({
                message: t('unieap.comm.optSuccess'),
                type: 'success'
              })
              emit('refreshAttribute')
            }
          })
        }
      })
    }

    const fieldValidate = () => {
      if (data.form.dataType === 'Number') {
        if (!isNumber(data.form.attributeValue)) {
          ElMessage({
            message: [data.form.displayName]+'是数字,请重新输入',
            type: 'error'
          })
          return false
        }
      }
      return true
    }

    /**
   * @description: 弹窗关闭动画结束时的回调
   * @param {*}
   * @return {*}
   * @author: gumingchen
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
      dialogClosedHandle
    }
  }
})
</script>
