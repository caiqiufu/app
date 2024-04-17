"use strict";
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      logId: "",
      approveResult: "Pass",
      approveComment: "通过",
      //项目信息
      optionsParentProjectList: [],
      //标段信息
      optionsProjectList: [],
      optionsBuildingPartList: [],
      parentId: "",
      projectId: "",
      // 基础表单数据
      baseFormData: {
        id: null,
        parentId: "",
        projectId: "",
        projectName: "",
        logTime: Date.now(),
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
        wxauId: ""
      },
      // 校验规则
      rules: {
        parentId: {
          rules: [{
            required: true,
            errorMessage: "项目不能为空"
          }]
        },
        projectId: {
          required: true,
          errorMessage: "标段不能为空"
        }
      }
    };
  },
  computed: {},
  onLoad(option) {
    this.logId = option.logId;
  },
  onShow() {
    this.projectDicData(null);
    this.getData();
  },
  onReady() {
  },
  methods: {
    //获取项目信息
    projectDicData(e) {
      let para = encodeURIComponent(JSON.stringify({
        "parentId": ""
      }));
      common_vendor.index.request({
        url: this.$baseUrl + "/backstage/projectoa/inf/getDicData?params=" + para,
        data: {
          token: common_vendor.index.getStorageSync("token"),
          groupCode: "PROJECT_LIST"
        },
        success: (res) => {
          this.optionsParentProjectList = [];
          res.data.data.forEach((item) => {
            this.optionsParentProjectList.push({
              value: item.dicCode,
              text: item.dicName
            });
          });
        },
        fail: (e2) => {
          console.log(e2);
        }
      });
    },
    changeParent(parentId) {
      if (parentId != "") {
        this.getProjectList(parentId);
      }
    },
    getProjectList(parentId) {
      let para = encodeURIComponent(JSON.stringify({
        "parentId": parentId
      }));
      common_vendor.index.request({
        url: this.$baseUrl + "/backstage/projectoa/inf/getDicData?params=" + para,
        data: {
          token: common_vendor.index.getStorageSync("token"),
          groupCode: "PROJECT_LIST"
        },
        success: (res) => {
          this.optionsProjectList = [];
          res.data.data.forEach((item) => {
            this.optionsProjectList.push({
              value: item.dicCode,
              text: item.dicName
            });
          });
        },
        fail: (e) => {
          console.log(e);
        }
      });
    },
    changeProject(projectId) {
      if (projectId != "") {
        this.optionsProjectList.forEach((item) => {
          if (item.value === projectId) {
            this.baseFormData.projectName = item.text;
          }
        });
        this.getBuildingpartList(projectId);
      }
    },
    getBuildingpartList(projectId) {
      this.baseFormData.deliveryBuildingpartList = [];
      this.baseFormData.deliveryBuildingpartList.push({
        id: Date.now(),
        projectId: this.baseFormData.projectId,
        logId: this.baseFormData.id,
        sgbw: "",
        sglr: "",
        sgqk: ""
      });
      let para = encodeURIComponent(JSON.stringify({
        projectId
      }));
      common_vendor.index.request({
        url: this.$baseUrl + "/backstage/projectoa/inf/getDicData?params=" + para,
        data: {
          token: common_vendor.index.getStorageSync("token"),
          groupCode: "BUILDING_PART"
        },
        success: (res) => {
          this.optionsBuildingPartList = [];
          res.data.data.forEach((item) => {
            this.optionsBuildingPartList.push({
              value: item.dicCode,
              text: item.dicName
            });
          });
        },
        fail: (e) => {
          console.log(e);
        }
      });
    },
    getData() {
      common_vendor.index.request({
        url: this.$baseUrl + "/backstage/projectoa/inf/info",
        data: {
          token: common_vendor.index.getStorageSync("token"),
          id: this.logId
        },
        success: (res) => {
          this.parentId = res.data.data.parentId.toString();
          this.projectId = res.data.data.projectId.toString();
          this.getProjectList(this.parentId);
          this.getBuildingpartList(this.projectId);
          if (this.baseFormData.deliveryBuildingpartList.length === 0) {
            this.baseFormData.deliveryBuildingpartList = [{
              id: Date.now(),
              projectId: this.projectId,
              logId: "",
              sgbw: "",
              sglr: "",
              sgqk: ""
            }];
          }
          this.baseFormData = res.data.data;
          this.baseFormData.parentId = res.data.data.parentId.toString();
          this.baseFormData.projectId = res.data.data.projectId.toString();
        },
        fail: (e) => {
          console.log(e);
        }
      });
    },
    add() {
      this.baseFormData.deliveryBuildingpartList.push({
        id: Date.now(),
        projectId: this.baseFormData.projectId,
        logId: this.baseFormData.id,
        sgbw: "",
        sglr: "",
        sgqk: ""
      });
    },
    del(id) {
      let index = this.baseFormData.deliveryBuildingpartList.findIndex((v) => v.id === id);
      this.baseFormData.deliveryBuildingpartList.splice(index, 1);
    },
    // change事件
    approveResultChange(e) {
      this.approveResult = e.detail.value;
      if (this.approveResult === "Pass") {
        this.approveComment = "通过";
      } else {
        this.approveComment = "驳回";
      }
    },
    submit() {
      common_vendor.index.request({
        url: this.$baseUrl + "/backstage/projectoa/inf/approve?token=" + common_vendor.index.getStorageSync("token"),
        method: "POST",
        data: {
          logId: this.logId,
          projectId: this.baseFormData.projectId,
          approveResult: this.approveResult,
          approveComment: this.approveComment
        },
        success: (res) => {
          common_vendor.index.showToast({
            title: "提交成功",
            duration: 2e3
          });
          common_vendor.index.navigateBack({
            delta: 1
            //返回层数，2则上上页
          });
        },
        fail: (e) => {
          console.log(e);
        }
      });
    }
  }
};
if (!Array) {
  const _easycom_uni_data_select2 = common_vendor.resolveComponent("uni-data-select");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  (_easycom_uni_data_select2 + _easycom_uni_forms_item2 + _easycom_uni_easyinput2 + _easycom_uni_list2 + _easycom_uni_forms2)();
}
const _easycom_uni_data_select = () => "../../../../uni_modules/uni-data-select/components/uni-data-select/uni-data-select.js";
const _easycom_uni_forms_item = () => "../../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_easyinput = () => "../../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_list = () => "../../../../uni_modules/uni-list/components/uni-list/uni-list.js";
const _easycom_uni_forms = () => "../../../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
if (!Math) {
  (_easycom_uni_data_select + _easycom_uni_forms_item + _easycom_uni_easyinput + _easycom_uni_list + _easycom_uni_forms)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o($options.changeParent),
    b: common_vendor.o(($event) => $data.baseFormData.parentId = $event),
    c: common_vendor.p({
      localdata: $data.optionsParentProjectList,
      placeholder: "选择项目",
      modelValue: $data.baseFormData.parentId
    }),
    d: common_vendor.p({
      label: "项目",
      name: "parentId",
      required: true
    }),
    e: common_vendor.o($options.changeProject),
    f: common_vendor.o(($event) => $data.baseFormData.projectId = $event),
    g: common_vendor.p({
      localdata: $data.optionsProjectList,
      placeholder: "选择标段",
      modelValue: $data.baseFormData.projectId
    }),
    h: common_vendor.p({
      label: "标段",
      name: "projectId",
      required: true
    }),
    i: common_vendor.f($data.baseFormData.deliveryBuildingpartList, (buildingPartInfo, index, i0) => {
      return {
        a: "1c207336-7-" + i0 + "," + ("1c207336-6-" + i0),
        b: common_vendor.o(($event) => buildingPartInfo.sgbw = $event, index),
        c: common_vendor.p({
          placeholder: "请输入施工部位",
          modelValue: buildingPartInfo.sgbw
        }),
        d: "sgbw_" + buildingPartInfo.id,
        e: "1c207336-6-" + i0 + "," + ("1c207336-5-" + i0),
        f: common_vendor.p({
          label: "施工部位" + (index + 1)
        }),
        g: "1c207336-9-" + i0 + "," + ("1c207336-8-" + i0),
        h: common_vendor.o(($event) => buildingPartInfo.sglr = $event, index),
        i: common_vendor.p({
          placeholder: "请输入施工内容",
          modelValue: buildingPartInfo.sglr
        }),
        j: "sglr_" + buildingPartInfo.id,
        k: "1c207336-8-" + i0 + "," + ("1c207336-5-" + i0),
        l: common_vendor.p({
          label: "施工内容" + (index + 1)
        }),
        m: "1c207336-11-" + i0 + "," + ("1c207336-10-" + i0),
        n: common_vendor.o(($event) => buildingPartInfo.sgqk = $event, index),
        o: common_vendor.p({
          placeholder: "请输入施工情况",
          modelValue: buildingPartInfo.sgqk
        }),
        p: "sgqk_" + buildingPartInfo.id,
        q: "1c207336-10-" + i0 + "," + ("1c207336-5-" + i0),
        r: common_vendor.p({
          label: "施工情况" + (index + 1)
        }),
        s: common_vendor.o(($event) => $options.del(buildingPartInfo.id), index),
        t: index,
        v: "1c207336-5-" + i0 + ",1c207336-0"
      };
    }),
    j: $data.baseFormData.deliveryBuildingpartList.length <= 1,
    k: common_vendor.p({
      border: true
    }),
    l: common_vendor.o((...args) => $options.add && $options.add(...args)),
    m: common_vendor.o(($event) => $data.baseFormData.xjjl = $event),
    n: common_vendor.p({
      type: "textarea",
      placeholder: "请输入巡检记录",
      modelValue: $data.baseFormData.xjjl
    }),
    o: common_vendor.p({
      label: "巡检记录"
    }),
    p: common_vendor.o(($event) => $data.baseFormData.pzjl = $event),
    q: common_vendor.p({
      placeholder: "请输入旁站项目及旁站记录情况",
      modelValue: $data.baseFormData.pzjl
    }),
    r: common_vendor.p({
      label: "旁站记录"
    }),
    s: common_vendor.o(($event) => $data.baseFormData.jzjl = $event),
    t: common_vendor.p({
      placeholder: "请输入见证项目及见证记录情况",
      modelValue: $data.baseFormData.jzjl
    }),
    v: common_vendor.p({
      label: "见证记录"
    }),
    w: common_vendor.o(($event) => $data.baseFormData.ybgcys = $event),
    x: common_vendor.p({
      placeholder: "请输入隐蔽工程验收情况",
      modelValue: $data.baseFormData.ybgcys
    }),
    y: common_vendor.p({
      label: "隐蔽工程"
    }),
    z: common_vendor.o(($event) => $data.baseFormData.fxgcjy = $event),
    A: common_vendor.p({
      placeholder: "请输入分项工程检验批验收情况",
      modelValue: $data.baseFormData.fxgcjy
    }),
    B: common_vendor.p({
      label: "分项工程"
    }),
    C: common_vendor.o(($event) => $data.baseFormData.gclh = $event),
    D: common_vendor.p({
      placeholder: "请输入工程(例会)会议情况",
      modelValue: $data.baseFormData.gclh
    }),
    E: common_vendor.p({
      label: "工程例会"
    }),
    F: common_vendor.o(($event) => $data.baseFormData.aqls = $event),
    G: common_vendor.p({
      placeholder: "请输入施工安全问题及处理后落实情况",
      modelValue: $data.baseFormData.aqls
    }),
    H: common_vendor.p({
      label: "安全问题"
    }),
    I: common_vendor.o(($event) => $data.baseFormData.jlzlzx = $event),
    J: common_vendor.p({
      placeholder: "请输入监理书面指令,执行及回复情况",
      modelValue: $data.baseFormData.jlzlzx
    }),
    K: common_vendor.p({
      label: "监理指令"
    }),
    L: common_vendor.o(($event) => $data.baseFormData.ktzscl = $event),
    M: common_vendor.p({
      placeholder: "请输入建设单位口头指示及处理意见",
      modelValue: $data.baseFormData.ktzscl
    }),
    N: common_vendor.p({
      label: "口头指示"
    }),
    O: common_vendor.o(($event) => $data.baseFormData.hbsxcl = $event),
    P: common_vendor.p({
      placeholder: "请输入施工单位汇报事项及处理意见",
      modelValue: $data.baseFormData.hbsxcl
    }),
    Q: common_vendor.p({
      label: "汇报事项"
    }),
    R: common_vendor.o(($event) => $data.baseFormData.qtsx = $event),
    S: common_vendor.p({
      placeholder: "请输入其他事项",
      modelValue: $data.baseFormData.qtsx
    }),
    T: common_vendor.p({
      label: "其他事项"
    }),
    U: $data.approveResult == "Pass",
    V: $data.approveResult == "Reject",
    W: common_vendor.o((...args) => $options.approveResultChange && $options.approveResultChange(...args)),
    X: common_vendor.p({
      label: "审核结果"
    }),
    Y: common_vendor.o(($event) => $data.approveComment = $event),
    Z: common_vendor.p({
      placeholder: "请输入审核意见",
      modelValue: $data.approveComment
    }),
    aa: common_vendor.p({
      label: "审核意见"
    }),
    ab: common_vendor.o(($event) => $options.submit()),
    ac: common_vendor.sr("baseForm", "1c207336-0"),
    ad: common_vendor.p({
      model: $data.baseFormData,
      rules: $data.rules,
      labelWidth: "80px"
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/caiqi/Documents/HBuilderProjects/unieap-slipper-app/pages/unieap/projectoa/jldailylog/approve.vue"]]);
wx.createPage(MiniProgramPage);
