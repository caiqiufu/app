"use strict";
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      searchVal: "",
      tableData: [],
      // 每页数据量
      pageSize: 10,
      // 当前页
      pageCurrent: 1,
      // 数据总量
      total: 0,
      loading: false
    };
  },
  onLoad() {
  },
  onShow() {
    this.selectedIndexs = [];
    this.getData(1);
  },
  methods: {
    editLog(e) {
      if (e.status === "Draft") {
        common_vendor.index.navigateTo({
          //保留当前页面，跳转到应用内的某个页面
          //url:`./home/index?title=${title}`
          url: "./edit?logId=" + e.id
          //url: '../wxlogin'
        });
      } else {
        common_vendor.index.showToast({
          title: "已提交不能修改",
          icon: "error",
          duration: 2e3
        });
      }
    },
    deleteLog(e) {
      common_vendor.index.request({
        url: this.$baseUrl + "/backstage/projectoa/inf/delete",
        data: {
          token: common_vendor.index.getStorageSync("token"),
          logId: e.id
        },
        success: (res) => {
          common_vendor.index.showToast({
            title: "删除成功",
            duration: 2e3
          });
          this.getData(1);
        },
        fail: (e2) => {
          console.log(e2);
        }
      });
    },
    // 多选处理
    selectedItems() {
      return this.selectedIndexs.map((i) => this.tableData[i]);
    },
    // 多选
    selectionChange(e) {
      console.log(e.detail.index);
      this.selectedIndexs = e.detail.index;
    },
    //批量删除
    delTable() {
      console.log(this.selectedItems());
    },
    // 分页触发
    change(e) {
      this.$refs.table.clearSelection();
      this.selectedIndexs.length = 0;
      this.getData(e.current);
    },
    // 搜索
    search() {
      this.getData(1, this.searchVal);
    },
    // 获取数据
    getData(pageCurrent, value = "") {
      this.loading = true;
      let wxcode = common_vendor.index.getStorageSync("wxcode");
      this.pageCurrent = pageCurrent;
      common_vendor.index.request({
        url: this.$baseUrl + "/backstage/projectoa/inf/page",
        data: {
          token: common_vendor.index.getStorageSync("token"),
          wxauId: wxcode,
          sort: "id",
          dir: "asc",
          "pageSize": this.pageSize,
          "currentPage": this.pageCurrent,
          "createBy": ""
        },
        success: (res) => {
          console.log(res);
          this.tableData = res.data.data.list;
          this.total = res.data.data.total;
          this.loading = false;
        },
        fail: (e) => {
          console.log(e);
        }
      });
    },
    // 伪request请求
    request(options) {
      const {
        pageSize,
        pageCurrent,
        success,
        value
      } = options;
      let total = tableData.length;
      let data = tableData.filter((item, index) => {
        const idx = index - (pageCurrent - 1) * pageSize;
        return idx < pageSize && idx >= 0;
      });
      if (value) {
        data = [];
        tableData.forEach((item) => {
          if (item.name.indexOf(value) !== -1) {
            data.push(item);
          }
        });
        total = data.length;
      }
      setTimeout(() => {
        typeof success === "function" && success({
          data,
          total
        });
      }, 500);
    }
  }
};
if (!Array) {
  const _easycom_uni_th2 = common_vendor.resolveComponent("uni-th");
  const _easycom_uni_tr2 = common_vendor.resolveComponent("uni-tr");
  const _easycom_uni_td2 = common_vendor.resolveComponent("uni-td");
  const _easycom_uni_table2 = common_vendor.resolveComponent("uni-table");
  const _easycom_uni_pagination2 = common_vendor.resolveComponent("uni-pagination");
  (_easycom_uni_th2 + _easycom_uni_tr2 + _easycom_uni_td2 + _easycom_uni_table2 + _easycom_uni_pagination2)();
}
const _easycom_uni_th = () => "../../../../uni_modules/uni-table/components/uni-th/uni-th.js";
const _easycom_uni_tr = () => "../../../../uni_modules/uni-table/components/uni-tr/uni-tr.js";
const _easycom_uni_td = () => "../../../../uni_modules/uni-table/components/uni-td/uni-td.js";
const _easycom_uni_table = () => "../../../../uni_modules/uni-table/components/uni-table/uni-table.js";
const _easycom_uni_pagination = () => "../../../../uni_modules/uni-pagination/components/uni-pagination/uni-pagination.js";
if (!Math) {
  (_easycom_uni_th + _easycom_uni_tr + _easycom_uni_td + _easycom_uni_table + _easycom_uni_pagination)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.p({
      width: "100",
      align: "center"
    }),
    b: common_vendor.p({
      width: "100",
      align: "center"
    }),
    c: common_vendor.f($data.tableData, (item, index, i0) => {
      return {
        a: common_vendor.t(item.logTime),
        b: "b369a16a-5-" + i0 + "," + ("b369a16a-4-" + i0),
        c: common_vendor.o(($event) => $options.editLog(item), index),
        d: common_vendor.o(($event) => $options.deleteLog(item), index),
        e: "b369a16a-6-" + i0 + "," + ("b369a16a-4-" + i0),
        f: index,
        g: "b369a16a-4-" + i0 + ",b369a16a-0"
      };
    }),
    d: common_vendor.sr("table", "b369a16a-0"),
    e: common_vendor.p({
      loading: $data.loading,
      border: true,
      stripe: true,
      type: "",
      emptyText: "暂无更多数据"
    }),
    f: common_vendor.o($options.change),
    g: common_vendor.p({
      ["show-icon"]: true,
      ["page-size"]: $data.pageSize,
      current: $data.pageCurrent,
      total: $data.total
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/caiqi/Documents/HBuilderProjects/unieap-slipper-app/pages/unieap/projectoa/jldailylog/loglist.vue"]]);
wx.createPage(MiniProgramPage);
