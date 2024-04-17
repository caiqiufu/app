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

      <el-row v-if="!isView" type="flex" justify="center">
        <el-form-item label-width="0px">
          <el-button @click="visible = false">{{
            $t("unieap.comm.cancel")
          }}</el-button>
          <el-button v-repeat type="primary" @click="submit()">{{
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
    //数据定义
    const urlPrefix = "/backstage/common/";
    const data = reactive({
      loading: false,
      visible: false,
      isView: false,
      isEdit: true,
      visibleProject: false,
      optionsWeatherType: [],
      optionsProjectList: [],
      optionsBuildingPartList: [],
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
      initForm('Add',id)
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
      initForm('Edit',id)
      data.visible = true;
      nextTick(() => {
        data.loading = false;
      })
    }
    const initView = async (id) => {
      data.loading = true;
      initForm('View',id)
      data.visible = true;
      nextTick(() => {
        data.loading = false;
      })
    }
    //初始化窗口
    const initForm = (optType, id) => {
      clearJson(data.form);
      infoDicDataApi({ groupCode: "WEATHER_TYPE" }).then((r) => {
        if (r) {
          data.optionsWeatherType = r.data;
        }
      });
      projectDicDataApi({}).then((r) => {
        if (r) {
          data.optionsProjectList = r.data;
        }
      });
      if (optType === "View") {
        data.isView = true;
        data.isEdit = true;
      }
      if (optType === "Edit") {
        data.isView = false;
        data.isEdit = true;
      }
      if (optType === "View" || optType === "Edit") {
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
      if (optType === "Add") {
        data.isEdit = false;
        data.form.projectId = id;
        data.projectId = id;
        data.form.logTime = new Date();
        data.form.status = "Draft";
      }
    }

    /**
     * @description: 表单验证提交
     * @param {*}
     * @return {*}
     */
    const submit = () => {
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
      ...toRefs(data),
      rules,
      init,
      initEdit,
      initView,
      submit,
      selectedItemHandle,
      dialogClosedHandle,
      removeBuildingPart,
      addBuildingPart,
    };
  },
});
</script>
