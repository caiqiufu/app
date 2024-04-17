<template>
  <el-dialog
    width="1000px"
    :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')"
    v-model="visible"
    :close-on-click-modal="false"
    @closed="dialogClosedHandle"
    append-to-body
    draggable
    destroy-on-close
  >
    <el-form
      v-loading="loading"
      :disabled="isView"
      :model="form"
      :rules="rules"
      ref="refForm"
      label-width="120px"
      label-position="right"
    >
      <el-row>
        <el-form-item
          label-width="80px"
          :label="$t('unieap.projectoa.comm.projectName')"
          prop="projectName"
        >
          <el-select
            :disabled="isEdit"
            style="width: 150px"
            v-model="form.projectId"
            :placeholder="$t('unieap.comm.plsSelect')"
            @change="selectedItemHandle(form.projectId)"
          >
            <el-option
              v-for="item in optionsProjectList"
              :key="item.dicCode"
              :label="item.dicName"
              :value="item.dicCode"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label-width="80px"
          :label="$t('unieap.projectoa.comm.sgdw')"
          prop="sgdw"
        >
          <el-input disabled style="width: 150px" v-model="form.sgdw" />
        </el-form-item>
        <el-form-item label-width="80px" label="日期" prop="logTime">
          <el-date-picker
            :disabled="isEdit"
            style="width: 150px"
            v-model="form.logTime"
            type="date"
            :placeholder="$t('unieap.comm.plsSelect')"
          >
          </el-date-picker>
        </el-form-item>
      </el-row>
      <el-divider content-position="left">
        <p style="font-weight: bold">天气情况</p>
      </el-divider>
      <el-row>
        <el-col :span="6">
          <el-form-item label-width="80px" label="最高气温" prop="tempMax">
            <el-input v-model="form.tempMax">
              <template v-slot:append>{{
                $t("unieap.projectoa.comm.template")
              }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label-width="80px" label="最低气温" prop="tempMin">
            <el-input v-model="form.tempMin">
              <template v-slot:append>{{
                $t("unieap.projectoa.comm.template")
              }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item
            label-width="80px"
            label="上午天气"
            prop="weatherMorning"
          >
            <el-select
              v-model="form.weatherMorning"
              :placeholder="$t('unieap.comm.plsSelect')"
            >
              <el-option
                v-for="item in optionsWeatherType"
                :key="item.dicCode"
                :label="item.dicName"
                :value="item.dicCode"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item
            label-width="80px"
            label="下午天气"
            prop="weatherAfternoon"
          >
            <el-select
              v-model="form.weatherAfternoon"
              :placeholder="$t('unieap.comm.plsSelect')"
            >
              <el-option
                v-for="item in optionsWeatherType"
                :key="item.dicCode"
                :label="item.dicName"
                :value="item.dicCode"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">
        <p style="font-weight: bold">施工情况</p>
      </el-divider>
      <el-row>
        <el-col :span="7">
          <el-form-item
            v-for="(buildingPartInfo, index) in form.deliveryBuildingpartList"
            label-width="80px"
            :label="'施工部位' + (index + 1)"
            :key="'sgbw_' + buildingPartInfo.code"
          >
            <el-select
              filterable
              allow-create
              v-model="buildingPartInfo.sgbw"
              style="width: 200px"
              :placeholder="$t('unieap.comm.plsSelect')"
            >
              <el-option
                v-for="item in optionsBuildingPartList"
                :key="item.dicCode"
                :label="item.dicName"
                :value="item.dicCode"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item
            v-for="(buildingPartInfo, index) in form.deliveryBuildingpartList"
            label-width="80px"
            :label="'施工内容' + (index + 1)"
            :key="'sglr_' + buildingPartInfo.code"
          >
            <el-input
              type="textarea"
              :rows="1"
              v-model="buildingPartInfo.sglr"
              style="width: 230px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item
            v-for="(buildingPartInfo, index) in form.deliveryBuildingpartList"
            label-width="80px"
            :label="'施工情况' + (index + 1)"
            :key="'sgqk_' + buildingPartInfo.code"
          >
            <el-input
              type="textarea"
              :rows="1"
              v-model="buildingPartInfo.sgqk"
              style="width: 230px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item
            v-for="(buildingPartInfo, index) in form.deliveryBuildingpartList"
            label-width="0px"
            :key="index"
          >
            <el-button
              @click.prevent="removeBuildingPart(buildingPartInfo)"
              :disabled="form.deliveryBuildingpartList.length <= 1"
              >{{ $t("unieap.comm.delete") }}</el-button
            >
          </el-form-item>
        </el-col>
        <el-form-item>
          <el-button @click="addBuildingPart">
            <el-icon>
              <Plus />
            </el-icon>
            {{ $t("unieap.comm.create") }}
          </el-button>
        </el-form-item>
      </el-row>
      <el-divider content-position="left">
        <p style="font-weight: bold">监理巡检记录</p>
      </el-divider>
      <el-row>
        <el-col :span="12">
          <el-row>
            <el-form-item label="巡检记录" prop="xjjl">
              <el-input
                type="textarea"
                :rows="3"
                v-model="form.xjjl"
                style="width: 280px"
              />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item
              label-width="180px"
              label="旁站项目及旁站记录情况"
              prop="pzjl"
            >
              <el-input v-model="form.pzjl" style="width: 220px" />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item
              label-width="180px"
              label="见证项目及见证记录情况"
              prop="jzjl"
            >
              <el-input v-model="form.jzjl" style="width: 220px" />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item
              label-width="180px"
              label="隐蔽工程验收情况"
              prop="ybgcys"
            >
              <el-input v-model="form.ybgcys" style="width: 220px" />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item
              label-width="180px"
              label="分项工程检验批验收情况"
              prop="fxgcjy"
            >
              <el-input v-model="form.fxgcjy" style="width: 220px" />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item
              label-width="180px"
              label="工程（例会）会议情况"
              prop="gclh"
            >
              <el-input v-model="form.gclh" style="width: 220px" />
            </el-form-item>
          </el-row>
        </el-col>
        <el-col :span="12">
          <el-row>
            <el-form-item label="施工安全问题及处理后落实情况" prop="aqls">
              <el-input
                type="textarea"
                :rows="2"
                v-model="form.aqls"
                style="width: 280px"
              />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="监理书面指令、执行及回复情况" prop="jlzlzx">
              <el-input
                type="textarea"
                :rows="2"
                v-model="form.jlzlzx"
                style="width: 280px"
              />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="建设单位口头指示及处理意见" prop="ktzscl">
              <el-input
                type="textarea"
                :rows="2"
                v-model="form.ktzscl"
                style="width: 280px"
              />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="施工单位汇报事项及处理意见" prop="hbsxcl">
              <el-input
                type="textarea"
                :rows="2"
                v-model="form.hbsxcl"
                style="width: 280px"
              />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="其他事项" prop="qtsx">
              <el-input
                type="textarea"
                :rows="2"
                v-model="form.qtsx"
                style="width: 280px"
              />
            </el-form-item>
          </el-row>
        </el-col>
      </el-row>
      <el-row v-if="!isView" type="flex" justify="center">
        <el-form-item label-width="0px">
          <el-button @click="visible = false">{{
            $t("unieap.comm.cancel")
          }}</el-button>
          <el-button v-repeat type="primary" @click="submit('Draft')"
            >保存草稿</el-button
          >
          <el-button v-repeat type="primary" @click="submit('Pending')"
            >提交审核</el-button
          >
        </el-form-item>
      </el-row>
    </el-form>
    <el-form
      v-loading="loading"
      v-if="isApprove"
      :model="formApprove"
      :rules="rules"
      ref="refFormApprove"
      label-width="120px"
      label-position="right"
    >
      <el-row>
        <el-divider content-position="left">
          <p style="font-weight: bold">审核意见</p>
        </el-divider>
        <el-form-item label="审核结果" prop="approveResult">
          <el-radio-group v-model="formApprove.approveResult" @change="radioChangeHandler">
            <el-radio
              v-for="approveResult in optionLogStatusList"
              :key="approveResult.dicCode"
              :label="approveResult.dicCode"
              >{{ approveResult.dicName }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label-width="180px" label="总监理工程师意见" prop="approveComment">
          <el-input
            type="textarea"
            :rows="2"
            v-model="formApprove.approveComment"
            style="width: 700px"
          />
        </el-form-item>
      </el-row>
      <el-row type="flex" justify="center">
        <el-form-item label-width="0px">
          <el-button @click="visible = false">{{
            $t("unieap.comm.cancel")
          }}</el-button>
          <el-button v-repeat type="primary" @click="submitApprove()">{{
            $t("unieap.comm.confirm")
          }}</el-button>
        </el-form-item>
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
import { parseDate2Str, clearJson } from "@/utils";
import { infoDicDataApi, bizHandleApi } from "@/api/unieap/comm";
//自定包引入
import {
  projectDicDataApi,
  buildingPartDicDataApi,
} from "@/api/unieap/projectoa/dic";

export default defineComponent({
  emits: ["refresh"],
  setup(_props, { emit }) {
    const { t } = useI18n();
    const refForm = ref();
    const refFormApprove = ref();
    //数据定义
    const urlPrefix = "/backstage/common/";
    const data = reactive({
      loading: false,
      visible: false,
      isView: false,
      isEdit: true,
      isApprove: false,
      visibleProject: false,
      optionsWeatherType: [],
      optionsProjectList: [],
      optionsBuildingPartList: [],
      optionLogStatusList: [],
      projectId: "",
      form: {
        id: null,
        projectId: "",
        projectName: "",
        logTime: "",
        status: "",
        dateStart: "",
        dateEnd: "",
        tempMax: "",
        tempMin: "",
        weatherMorning: "",
        weatherAfternoon: "",
        sgdw: "",
        sgbw: "",
        sglr: "",
        sgqk: "",
        xjjl: "",
        pzjl: "",
        jzjl: "",
        ybgcys: "",
        fxgcjy: "",
        gclh: "",
        aqls: "",
        jlzlzx: "",
        ktzscl: "",
        hbsxcl: "",
        qtsx: "",
        zjlyj: "",
        zjlName: "",
        zjlDate: "",
        createDate: "",
        createBy: "",
        modifyDate: "",
        modifyBy: "",
        deliveryBuildingpartList: [],
      },
      formApprove: {
        id: null,
        projectId: null,
        logId: null,
        approveResult: "",
        approveComment: "",
      },
      roles: [],
    });
    const rules = computed(() => {
      return {
        name: [
          {
            required: true,
            message:
              [t("unieap.comm.plsInput")] +
              [t("unieap.projectoa.supervisionmgt.company.companyName")],
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
      data.loading = true;
      initForm("Add", id);
      data.visible = true;
      nextTick(() => {
        data.loading = false;
      });
    };

    /**
     * @description: 加载窗口初始化
     * @param {*} id
     */
    const initEdit = async (id) => {
      data.loading = true;
      initForm("Edit", id);
      data.visible = true;
      nextTick(() => {
        data.loading = false;
      });
    };
    const initView = async (id) => {
      data.loading = true;
      initForm("View", id);
      data.visible = true;
      nextTick(() => {
        data.loading = false;
      });
    };
    const initApprove = async (id) => {
      data.loading = true;
      initForm("Approve", id);
      data.visible = true;
      nextTick(() => {
        data.loading = false;
      });
    };
    //初始化窗口
    const initForm = (optType, id) => {
      clearJson(data.form);
      infoDicDataApi({ groupCode: "WEATHER_TYPE" }).then((r) => {
        if (r) {
          data.optionsWeatherType = r.data;
        }
      });
      data.optionLogStatusList = [
        { dicCode: "Pass", dicName: "审核通过" },
        { dicCode: "Reject", dicName: "驳回" },
      ];
      projectDicDataApi({}).then((r) => {
        if (r) {
          data.optionsProjectList = r.data;
        }
      });
      if (optType === "Add") {
        data.isView = false;
        data.isEdit = false;
        data.isApprove = false;
        data.form.projectId = id;
        data.projectId = id;
        data.form.logTime = new Date();
        data.form.status = "Draft";
      }
      if (optType === "View") {
        data.isView = true;
        data.isEdit = true;
        data.isApprove = false;
      }
      if (optType === "Edit") {
        data.isView = false;
        data.isEdit = true;
        data.isApprove = false;
      }
      if (optType === "Approve") {
        data.isView = true;
        data.isEdit = true;
        data.isApprove = true;
      }
      if (optType === "View" || optType === "Edit" || optType === "Approve") {
        data.form.id = id;
        data.form.status = "Draft";
        if (id) {
          const params = {
            url: "/backstage/projectoa/delivery/jlDailyLog/info",
            id: id,
          };
          bizHandleApi(params).then((r) => {
            if (r) {
              data.projectId = r.data.projectId.toString();
              data.form.projectId = r.data.projectId.toString();
              if (r.data.deliveryBuildingpartList.length === 0) {
                data.form.deliveryBuildingpartList = [
                  {
                    id: Date.now(),
                    projectId: data.form.projectId,
                    logId: data.form.id,
                    sgbw: "",
                    sglr: "",
                    sgqk: "",
                  },
                ];
              } else {
                data.form.deliveryBuildingpartList.forEach(function (
                  element,
                  index,
                  array
                ) {
                  element.id = element.id.toString();
                });
              }
              buildingPartDicDataApi({ projectId: data.projectId }).then(
                (r1) => {
                  if (r1) {
                    data.optionsBuildingPartList = r1.data;
                  }
                }
              );
              //最后才设置form数据
              data.form = r.data;
              data.form.projectId = r.data.projectId.toString();
            }
          });
        }
      }
    };

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = (optType) => {
      refForm.value.validate(async (valid) => {
        if (valid) {
          //时间格式需要转换
          data.form.logTime = parseDate2Str(data.form.logTime);
          const myUrlPrefix = "/backstage/projectoa/delivery/jlDailyLog/";
          const operType = data.form.id ? "update" : "create";
          const urlObj = {
            type: "data",
            url: myUrlPrefix + operType,
          };
          data.form.status = optType;
          const params = Object.assign(urlObj, data.form);
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

    const submitApprove = () => {
      refFormApprove.value.validate(async (valid) => {
        if (valid) {
          //时间格式需要转换
          data.form.logTime = parseDate2Str(data.form.logTime);
          const myUrlPrefix = "/backstage/projectoa/delivery/jlDailyLog/";
          const operType = "approve";
          const urlObj = {
            url: myUrlPrefix + operType,
            projectId: data.form.projectId,
            logId: data.form.id,
            approveResult: data.formApprove.approveResult,
            approveComment: data.formApprove.approveComment,
          };
          const params = Object.assign(urlObj, data.form);
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
       
    const radioChangeHandler = (value) => {
      if(value==='Pass'){
        data.formApprove.approveComment = '审核通过'
      }else{
        data.formApprove.approveComment = '驳回'
      }    
    };

    const selectedItemHandle = (projectId) => {
      data.form.projectId = projectId;
      data.projectId = projectId;
      data.optionsProjectList.forEach(function (value) {
        if (value.dicCode === projectId) {
          data.form.projectId = value.dicCode;
          data.form.projectName = value.dicName;
        }
      });
      const params = {
        repositoryName: "projectInfoRepository",
        beanName: "projectoa.projectmgt.pojo.ProjectInfo",
        url: urlPrefix + "commInfo",
        id: projectId,
      };
      bizHandleApi(params).then((r) => {
        if (r) {
          data.form.sgdw = r.data.sgdw;
        }
      });
      bizHandleApi({
        url: "/backstage/projectoa/delivery/jlDailyLog/info",
        id: null,
        projectId: data.form.projectId,
      }).then((r) => {
        if (r) {
          data.form.sgqk = r.data.sgqk;
          data.form.gclh = r.data.gclh;
          data.form.ktzscl = r.data.ktzscl;
        }
      });
      buildingPartDicDataApi({ projectId: data.form.projectId }).then((r) => {
        if (r) {
          data.optionsBuildingPartList = r.data;
        }
      });
      data.form.deliveryBuildingpartList = [
        {
          id: Date.now(),
          projectId: data.form.projectId,
          logId: data.form.id,
          sgbw: "",
          sglr: "",
          sgqk: "",
        },
      ];
    };

    const removeBuildingPart = (item) => {
      var index = data.form.deliveryBuildingpartList.indexOf(item);
      if (index !== -1) {
        data.form.deliveryBuildingpartList.splice(index, 1);
      }
    };

    const addBuildingPart = () => {
      data.form.deliveryBuildingpartList.push({
        id: Date.now(),
        projectId: data.projectId,
        logId: data.form.id,
        sgbw: "",
        sglr: "",
        sgqk: "",
      });
    };

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
      refFormApprove,
      ...toRefs(data),
      rules,
      init,
      initEdit,
      initView,
      initApprove,
      submit,
      submitApprove,
      selectedItemHandle,
      dialogClosedHandle,
      removeBuildingPart,
      addBuildingPart,
      radioChangeHandler
    };
  },
});
</script>
