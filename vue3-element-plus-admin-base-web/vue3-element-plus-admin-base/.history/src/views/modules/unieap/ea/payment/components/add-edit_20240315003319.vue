<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-form v-loading="loading" :model="form" :rules="rules" ref="refForm" label-width="150px" label-position="right">
      <el-form-item label="用户" prop="userCode">
        <el-input v-model="form.userCode" disabled maxlength="32" show-word-limit style="width: 300px" />
      </el-form-item>
      <el-form-item label="账户" prop="accountCode">
        <el-input v-model="form.accountCode" disabled maxlength="32" show-word-limit style="width: 300px" />
      </el-form-item>
      <el-form-item label="账单金额" prop="outstandingAmount">
        <el-input v-model="form.outstandingAmount" disabled maxlength="32" show-word-limit style="width: 300px" />
      </el-form-item>
      <el-form-item label="支付金额" prop="paidAmount">
        <el-input v-model="form.paidAmount" maxlength="32" show-word-limit style="width: 300px" />
      </el-form-item>
      <el-form-item label="支付人姓名" prop="userName">
        <el-input v-model="form.userName" maxlength="32" show-word-limit style="width: 300px" />
      </el-form-item>
      <el-form-item label="支付方式" prop="payMethod">
        <el-select v-model="form.payMethod" :placeholder="$t('unieap.comm.plsSelect')" style="width: 300px">
          <el-option v-for="item in optionPayMethod" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="支付账号" prop="accountNo">
        <el-input v-model="form.accountNo" maxlength="32" show-word-limit style="width: 300px" />
      </el-form-item>
      <el-form-item label="支付凭证" prop="screenshotUrl">
        <UImageUploadSingle v-model="form.screenshotUrl" @uploadFinishedHandle="uploadFinishedHandle" category="form.category"
          :extKey="form.id.toString()" size="100px" :fileList="form.fileList" processHandle="testHandler" />
      </el-form-item>
      <el-row type="flex" justify="center">
        <el-button @click="visible = false">{{
    $t("unieap.comm.cancel")
  }}</el-button>
        <el-button v-repeat type="primary" @click="submit()">{{
    $t("unieap.comm.confirm")
          }}</el-button>
      </el-row>
      <el-row type="flex" justify="center">
        <br />
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import { useI18n } from "vue-i18n";
import {
  computed,
  defineComponent,
  nextTick,
  reactive,
  ref,
  toRefs,
} from "vue";
import { ElMessage } from "element-plus";
import { infoDicDataApi, bizHandleApi } from "@/api/unieap/comm";
//自定包引入
import UImageUploadSingle from '@/components/unieap/file-upload-single'
export default defineComponent({
  components: { UImageUploadSingle},
  emits: ["refresh"],
  setup(_props, { emit }) {
    const { t } = useI18n();
    const refForm = ref();
    //数据定义
    const urlPrefix = "/backstage/ea/mybill/";
    const data = reactive({
      loading: false,
      visible: false,
      optionPayMethod: [],
      fileList: [],
      form: {
        id: "",
        userCode: "",
        accountCode: "",
        outstandingAmount: "",
        paidAmount: "",
        userName: "",
        payMethod:"",
        accountNo:"",
        screenshotUrl:"",
        category: 'payment_sreenshot',
        extKey: "",
        file:[]
      },
      roles: [],
    });
    const rules = computed(() => {
      return {
        payAmount: [
          {
            required: true,
            message: "支付金额不能为空,并且必须和账单金额一致",
            trigger: "blur",
          },
        ],
        userName: [
          {
            required: true,
            message: "支付人姓名不能为空,必须和支付凭证上显示的名称一致",
            trigger: "blur",
          },
        ],
        payMethod: [
          {
            required: true,
            message: "支付方式不能为空",
            trigger: "blur",
          },
        ],
        accountNo: [
          {
            required: true,
            message: "支付账号不能为空,必须和支付凭证上的一致",
            trigger: "blur",
          },
        ],
        screenshotUrl: [
          {
            required: true,
            message: "支付凭证不能为空",
            trigger: "blur",
          },
        ],
      };
    });
    /**
     * @description: 加载窗口初始化
     * @param {*} id
     */
    const init = async (id) => {
      infoDicDataApi({ groupCode: "PAY_METHOD" }).then((r) => {
        if (r) {
          data.optionPayMethod = r.data;
        }
      });
      data.visible = true;
      data.loading = true;
      data.id = id;
      data.form.extKey = id;
      if (id) {
        const params = {
          beanName: "",
          boName: "",
          url: urlPrefix + "getBillDetailInfo",
          id: id,
        };
        bizHandleApi(params).then((r) => {
          if (r) {
            data.form = r.data;
          }
        });
      }
      nextTick(() => {
        data.loading = false;
      });
    };

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
      refForm.value.validate(async (valid) => {
        if (valid) {
          const handleName = "billPayment";
          const operType = data.form.id ? "update" : "create";
          const urlObj = {
            beanName: "",
            url: urlPrefix + handleName,
            operType: operType
          };
          const params = Object.assign(urlObj, {
            id: data.form.id,
            userName: data.form.userName,
            payMethod: data.form.payMethod,
            paidAmount: data.form.paidAmount,
            accountNo: data.form.accountNo,
            screenshotUrl: data.form.screenshotUrl
          });
          bizHandleApi(params).then((r) => {
            if (r) {
              data.visible = false;
              ElMessage({
                message: t("unieap.comm.optSuccess"),
                type: "success",
              });
              emit("refresh");
            }
          });
        }
      });
    };
    //上传成功后刷新数据
    const uploadFinishedHandle = (e) => {
      data.fileList = e.fileList
      if(data.fileList!=null && data.fileList.length>0){
        data.form.screenshotUrl = data.fileList[0].url;
      }
    }
    /**
     * @description: 弹窗关闭动画结束时的回调
     * @param {*}
     * @return {*}
     */
    const dialogClosedHandle = () => {
      refForm.value.resetFields();
    };

    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      uploadFinishedHandle,
      dialogClosedHandle,
    };
  },
});
</script>
