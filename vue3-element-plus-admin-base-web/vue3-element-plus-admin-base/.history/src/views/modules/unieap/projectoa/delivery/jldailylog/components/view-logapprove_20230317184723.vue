<template>
  <el-dialog width="600px" :title="form.id ? $t('unieap.comm.update') : $t('unieap.comm.create')" v-model="visible"
    :close-on-click-modal="false" @closed="dialogClosedHandle" append-to-body draggable destroy-on-close>
    <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" label="审核时间" prop="createDate" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.company.companyName')"
          prop="companyName" width="200" />
        <el-table-column align="center" :label="$t('unieap.comm.gender')" prop="genderDesc" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.position')"
          prop="positionDesc" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.idNumber')"
          prop="idNumber" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.phone')" prop="phone" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.wx')" prop="wx" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.certificateType')"
          prop="certificateTypeDesc" />
        <el-table-column align="center" :label="$t('unieap.projectoa.supervisionmgt.employee.certificateLicense')"
          prop="licenseNumber" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="200" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'supervisionmgt.employee:update'" type="primary" link
              @click="addEditHandle(row.id)">{{
                $t('unieap.comm.update')
              }}</el-button>
            <el-button v-permission="'supervisionmgt.employee:delete'" type="danger" link @click="deleteHandle(row.id)">{{
              $t('unieap.comm.delete')
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>
  </el-dialog>
</template>

<script>
import { useI18n } from 'vue-i18n'
import { computed, defineComponent, nextTick, reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
//自定包引入
import { infoDicDataApi, bizHandleApi } from '@/api/unieap/comm'
import { companyDicDataApi } from '@/api/unieap/projectoa/dic'

export default defineComponent({

  emits: ['refresh'],
  setup(_props, { emit }) {
    const { t } = useI18n()
    const refForm = ref()
    //数据定义
    const urlPrefix = '/backstage/common/'
    const data = reactive({
      loading: false,
      visible: false,
      logId:null,
      list: [],
      selection: []
    })

    //分页数据,根据需求修改
    const getList = () => {
      data.loading = true
      const params = {
        sort: 'createDate',
        dir: 'ASC',
        //查询参数
        beanName: 'projectoa.delivery.pojo.ApproveLog',
        boName: '',
        logId:data.logId
      }
      params.url = urlPrefix + 'commPage'
      bizHandleApi(params).then(r => {
        if (r) {
          data.list = r.data.list
        }
        nextTick(() => { data.loading = false })
      })
    }

    /**
     * @description: 加载窗口初始化
     * @param {*} id 
     */
    const init = async (id) => {
      data.visible = true
      data.loading = true
      data.logId = id
      getList()
      nextTick(() => { data.loading = false })
    }

    /**
   * @description: 弹窗关闭动画结束时的回调
   * @param {*}
   * @return {*}
   */
    const dialogClosedHandle = () => {
    }

    return {
      ...toRefs(data),
      init,
      dialogClosedHandle
    }
  }
})
</script>
