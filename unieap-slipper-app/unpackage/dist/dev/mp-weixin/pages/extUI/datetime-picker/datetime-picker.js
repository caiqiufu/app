"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      single: "",
      datetimesingle: "",
      range: ["2021-02-1", "2021-3-28"],
      datetimerange: [],
      start: Date.now() - 1e9,
      end: Date.now() + 1e9
    };
  },
  watch: {
    datetimesingle(newval) {
      console.log("单选:", this.datetimesingle);
    },
    range(newval) {
      console.log("范围选:", this.range);
    },
    datetimerange(newval) {
      console.log("范围选:", this.datetimerange);
    }
  },
  mounted() {
    setTimeout(() => {
      this.datetimesingle = Date.now() - 2 * 24 * 3600 * 1e3;
      this.single = "2021-2-12";
      this.datetimerange = ["2021-07-08 0:01:10", "2021-08-08 23:59:59"];
    }, 3e3);
  },
  methods: {
    change(e) {
      this.single = e;
      console.log("----change事件:", this.single = e);
    },
    changeLog(e) {
      console.log("----change事件:", e);
    },
    maskClick(e) {
      console.log("----maskClick事件:", e);
    }
  }
};
if (!Array) {
  const _easycom_uni_card2 = common_vendor.resolveComponent("uni-card");
  const _easycom_uni_section2 = common_vendor.resolveComponent("uni-section");
  const _easycom_uni_datetime_picker2 = common_vendor.resolveComponent("uni-datetime-picker");
  (_easycom_uni_card2 + _easycom_uni_section2 + _easycom_uni_datetime_picker2)();
}
const _easycom_uni_card = () => "../../../uni_modules/uni-card/components/uni-card/uni-card.js";
const _easycom_uni_section = () => "../../../uni_modules/uni-section/components/uni-section/uni-section.js";
const _easycom_uni_datetime_picker = () => "../../../uni_modules/uni-datetime-picker/components/uni-datetime-picker/uni-datetime-picker.js";
if (!Math) {
  (_easycom_uni_card + _easycom_uni_section + _easycom_uni_datetime_picker)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.p({
      ["is-full"]: true
    }),
    b: common_vendor.p({
      title: "日期用法：" + $data.single,
      type: "line"
    }),
    c: common_vendor.o($options.maskClick),
    d: common_vendor.o(($event) => $data.single = $event),
    e: common_vendor.p({
      type: "date",
      ["clear-icon"]: false,
      modelValue: $data.single
    }),
    f: common_vendor.p({
      title: "日期时间用法：" + $data.datetimesingle,
      type: "line"
    }),
    g: common_vendor.o($options.changeLog),
    h: common_vendor.o(($event) => $data.datetimesingle = $event),
    i: common_vendor.p({
      type: "datetime",
      modelValue: $data.datetimesingle
    }),
    j: common_vendor.p({
      title: "日期范围用法：[" + $data.range + "]",
      type: "line"
    }),
    k: common_vendor.o($options.maskClick),
    l: common_vendor.o(($event) => $data.range = $event),
    m: common_vendor.p({
      type: "daterange",
      modelValue: $data.range
    }),
    n: common_vendor.p({
      title: "日期时间范围用法：[" + $data.datetimerange + "]",
      type: "line"
    }),
    o: common_vendor.o(($event) => $data.datetimerange = $event),
    p: common_vendor.p({
      type: "datetimerange",
      rangeSeparator: "至",
      modelValue: $data.datetimerange
    }),
    q: common_vendor.p({
      title: "v-model用法：" + $data.single,
      type: "line"
    }),
    r: common_vendor.o(($event) => $data.single = $event),
    s: common_vendor.p({
      modelValue: $data.single
    }),
    t: common_vendor.p({
      title: "时间戳用法：" + $data.single,
      type: "line"
    }),
    v: common_vendor.o(($event) => $options.changeLog($event)),
    w: common_vendor.o(($event) => $data.single = $event),
    x: common_vendor.p({
      returnType: "timestamp",
      modelValue: $data.single
    }),
    y: common_vendor.p({
      title: "date 对象用法：" + $data.datetimesingle,
      type: "line"
    }),
    z: common_vendor.o($options.changeLog),
    A: common_vendor.o(($event) => $data.datetimesingle = $event),
    B: common_vendor.p({
      type: "datetime",
      returnType: "date",
      modelValue: $data.datetimesingle
    }),
    C: common_vendor.p({
      title: "插槽用法：" + $data.single,
      type: "line"
    }),
    D: common_vendor.o(($event) => $data.single = $event),
    E: common_vendor.p({
      modelValue: $data.single
    }),
    F: common_vendor.p({
      title: "无边框用法：" + $data.single,
      type: "line"
    }),
    G: common_vendor.o(($event) => $data.single = $event),
    H: common_vendor.p({
      border: false,
      modelValue: $data.single
    }),
    I: common_vendor.p({
      title: "隐藏清除按钮用法：" + $data.single,
      type: "line"
    }),
    J: common_vendor.o(($event) => $data.single = $event),
    K: common_vendor.p({
      clearIcon: false,
      modelValue: $data.single
    }),
    L: common_vendor.p({
      title: "disabled用法：" + $data.single,
      type: "line"
    }),
    M: common_vendor.o(($event) => $data.single = $event),
    N: common_vendor.p({
      disabled: true,
      modelValue: $data.single
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/caiqi/Documents/HBuilderProjects/unieap-slipper-app/pages/extUI/datetime-picker/datetime-picker.vue"]]);
wx.createPage(MiniProgramPage);
