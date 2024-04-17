<template>
  <el-dialog
    width="900px"
    :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')"
    v-model="visible"
    :close-on-click-modal="false"
    @closed="dialogClosedHandle"
    append-to-body
    draggable
    destroy-on-close
  >
    <el-row :gutter="20" type="flex">
      <el-button v-repeat type="primary" @click="editContactHandle(null)">{{
        $t("unieap.school.renyuanguanli.baobei.addContact")
      }}</el-button>
    </el-row>
    <el-row :gutter="20" type="flex">
      <span>
        <br />
      </span>
    </el-row>
    <div class="box">
      <el-scrollbar style="height: 100%">
        <span v-show="form.contactList.length == 0">
          <el-empty :description="$t('unieap.comm.nodata')"></el-empty>
        </span>
        <el-card v-for="contactInfo in form.contactList" :key="contactInfo.id">
          <template #header>
            <el-row :gutter="20" type="flex" justify="let">
              <el-col :span="12" align="left">
                <span>
                  {{ contactInfo.kidsName }} {{ contactInfo.relationshipDesc }}
                  {{
                    contactInfo.priority === "1"
                      ? "(" +
                        $t(
                          "unieap.school.renyuanguanli.baobei.isFirstContact"
                        ) +
                        ")"
                      : ""
                  }}
                </span>
              </el-col>
              <el-col :span="12" align="right">
                <el-button
                  v-repeat
                  v-show="contactInfo.priority !== '1'"
                  type="primary"
                  link
                  @click="deleteContactHandle(contactInfo.id)"
                  >{{ $t("unieap.comm.delete") }}</el-button
                >
                <el-button
                  v-repeat
                  type="primary"
                  link
                  @click="editContactHandle(contactInfo.id)"
                >
                  {{ $t("unieap.comm.update") }}</el-button
                >
                <el-button
                  v-repeat
                  v-show="contactInfo.priority !== '1'"
                  type="primary"
                  link
                  @click="setFirstContactHandle(contactInfo.id)"
                >
                  {{
                    $t("unieap.school.renyuanguanli.baobei.setFirstContact")
                  }}</el-button
                >
                <el-button
                  v-repeat
                  type="primary"
                  link
                  @click="resetPasswordtHandle(contactInfo.id)"
                >
                  {{ $t("unieap.comm.resetPassword") }}</el-button
                >
              </el-col>
            </el-row>
          </template>
          <div class="text item">
            <el-form
              v-loading="loading"
              :model="form"
              :rules="rules"
              ref="refForm"
              label-width="80px"
              label-position="right"
            >
              <el-row :gutter="10" type="flex" justify="start">
                <el-col :span="6">
                  <el-form-item
                    :label="
                      $t('unieap.school.renyuanguanli.baobei.contactName')
                    "
                    prop="contactName"
                  >
                    {{ contactInfo.contactName }}
                  </el-form-item>
                  <el-form-item
                    :label="$t('unieap.school.comm.idType')"
                    prop="idType"
                  >
                    {{ contactInfo.idTypeDesc }}
                  </el-form-item>
                  <el-form-item
                    :label="$t('unieap.school.renyuanguanli.baobei.education')"
                    prop="education"
                  >
                    {{ contactInfo.educationDesc }}
                  </el-form-item>
                  <el-form-item
                    :label="
                      $t('unieap.school.renyuanguanli.baobei.contactSourceType')
                    "
                    prop="contactSourceType"
                  >
                    {{ contactInfo.sourceTypeDesc }}
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item
                    :label="
                      $t('unieap.school.renyuanguanli.baobei.relationship')
                    "
                    prop="nationality"
                  >
                    {{ contactInfo.relationshipDesc }}
                  </el-form-item>
                  <el-form-item
                    :label="$t('unieap.school.comm.idNumber')"
                    prop="idNumber"
                  >
                    {{ contactInfo.idNumber }}
                  </el-form-item>
                  <el-form-item
                    :label="$t('unieap.school.renyuanguanli.baobei.occupation')"
                    prop="occupation"
                  >
                    {{ contactInfo.occupation }}
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item
                    :label="
                      $t('unieap.school.renyuanguanli.baobei.contactNumber')
                    "
                    prop="nationality"
                  >
                    {{ contactInfo.contactNumber }}
                  </el-form-item>
                  <el-form-item
                    :label="$t('unieap.school.comm.nation')"
                    prop="nation"
                  >
                    {{ contactInfo.nationDesc }}
                  </el-form-item>
                  <el-form-item
                    :label="$t('unieap.school.renyuanguanli.baobei.email')"
                    prop="email"
                  >
                    {{ contactInfo.email }}
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item prop="profileUrl">
                    <el-avatar :size="120" :src="contactInfo.profileUrl">
                      <UImageUploadSingle
                        @updateData="updateData"
                        v-model="form.profileUrl"
                        :category="form.category"
                        :extKey="form.extKey"
                      />
                    </el-avatar>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-card>
      </el-scrollbar>
    </div>
  </el-dialog>
  <EditContact
    ref="refEditContact"
    v-if="editContactVisible"
    @refresh="getList"
  />
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

import { ElMessageBox } from "element-plus";
import { ElMessage } from "element-plus";
import UImageUploadSingle from "@/components/unieap/file-upload-single";

import EditContact from "./addcontact";

import {
  addKidsApi,
  contactInfoListApi,
  deleteContactInfoApi,
  setFirstContactInfoApi,
  resetPasswordContactInfoApi,
} from "@/api/unieap/school/renyuanguanli/baobei";

export default defineComponent({
  components: { UImageUploadSingle, EditContact },
  emits: ["refresh"],
  setup(_props, { emit }) {
    const { t } = useI18n();
    const refForm = ref();
    const refEditContact = ref();
    const data = reactive({
      loading: false,
      visible: false,
      editContactVisible: false,
      kidsId: "",
      form: {
        kidsId: "",
        id: null,
        contactList: [],
      },
      roles: [],
    });
    const rules = computed(() => {
      return {};
    });
    const init = async (id) => {
      data.visible = true;
      data.loading = true;
      data.kidsId = id;
      contactInfoListApi({ id: data.kidsId }).then((r) => {
        if (r) {
          data.form.contactList = r.data;
        }
        nextTick(() => {
          data.loading = false;
        });
      });
    };

    /**
     * 子类调用该方法刷新数据
     * @param {*} e
     */
    const updateData = (e) => {
      //data.form.profileId = e.fileId
    };

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     * @author: gumingchen
     */
    const submit = () => {
      refForm.value.validate(async (valid) => {
        if (valid) {
          const params = {};
          const r = await addKidsApi(params);
          if (r) {
            data.visible = false;
            ElMessage({
              message: t("unieap.comm.optSuccess"),
              type: "success",
            });
            emit("refresh");
          }
        }
      });
    };

    /**
     * @description: 弹窗关闭动画结束时的回调
     * @param {*}
     * @return {*}
     * @author: gumingchen
     */
    const dialogClosedHandle = () => {
      //refForm.value.resetFields()
    };
    /**
     * 编辑联系人
     * @param {*} contactId
     */
    const editContactHandle = (contactId) => {
      data.editContactVisible = true;
      nextTick(() => {
        refEditContact.value.init({
          kidsId: data.kidsId,
          contactId: contactId,
        });
      });
    };
    /**
     * 删除联系人
     * @param {*} contactId
     */
    const deleteContactHandle = (contactId) => {
      ElMessageBox.confirm(
        "<strong>删除须知：</strong></br>1.家长的所有信息都将清空</br>2.家长无法登录掌心宝贝APP</br>3.家长关联的所有付费服务都将无法恢复</br>",
        "提示",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          deleteContactInfoApi({ contactId: contactId }).then((r) => {
            if (r) {
              getList();
              ElMessage.success({
                message: t("unieap.comm.optSuccess"),
              });
            }
          });
        })
        .catch(() => {
          ElMessage.info({
            message: "已取消删除",
          });
        });
    };
    const setFirstContactHandle = (contactId) => {
      setFirstContactInfoApi({
        kidsId: data.kidsId,
        contactId: contactId,
      }).then((r) => {
        if (r) {
          getList();
          ElMessage.success({
            message: t("unieap.comm.optSuccess"),
          });
        }
      });
    };

    const resetPasswordtHandle = (contactId) => {
      ElMessageBox.confirm(
        '确认重置当前账号密码？重置后，密码为APP默认登录密码。可在【园区设置>系统参数设置】中查看"APP默认登录密码"',
        "提示",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          resetPasswordContactInfoApi({ contactId: contactId }).then((r) => {
            if (r) {
              ElMessage.success({
                message: t("unieap.comm.optSuccess"),
              });
            }
          });
        })
        .catch(() => {
          ElMessage.info({
            message: t("unieap.comm.optCancel"),
          });
        });
    };

    const getList = () => {
      data.loading = true;
      contactInfoListApi({ id: data.kidsId, kidsId: data.kidsId }).then((r) => {
        if (r) {
          data.form.contactList = r.data;
        }
        nextTick(() => {
          data.loading = false;
        });
      });
    };

    return {
      refForm,
      ...toRefs(data),
      rules,
      init,
      submit,
      dialogClosedHandle,
      getList,
      refEditContact,
      editContactHandle,
      deleteContactHandle,
      setFirstContactHandle,
      resetPasswordtHandle,
    };
  },
});
</script>
<style lang="scss" scoped>
/* 1,必须去掉scoped，否则overflow-x: hidden失效 */
.box {
  margin: 0 auto;
  height: 500px;
}
</style>