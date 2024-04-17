<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()" label-width="80px">
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.kidsName')" prop="kidsName">
              <el-input v-model="form.kidsName" :placeholder="$t('unieap.comm.plsInput')" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.contactName')" prop="contactName">
              <el-input v-model="form.contactName" :placeholder="$t('unieap.comm.plsInput')" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('unieap.school.comm.grade')">
              <el-select v-model="form.gradeValue" :placeholder="$t('unieap.comm.plsSelect')"
                @change="gradeChange(form.gradeValue)">
                <el-option v-for="item in optionsGrade" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('unieap.school.comm.class')">
              <el-select v-model="form.classValue" :placeholder="$t('unieap.comm.plsSelect')">
                <el-option v-for="item in optionsClass" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.contactNumber')" prop="contactNumber">
          <el-input v-model="form.contactNumber" :placeholder="$t('unieap.comm.plsInput')" />
        </el-form-item>
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.registerDate')" prop="registerDateRange">
          <el-date-picker v-model="form.registerDateRange" type="daterange" :range-separator="$t('unieap.comm.fromTo')"
            :start-placeholder="$t('unieap.comm.dateStart')" :end-placeholder="$t('unieap.comm.dateEnd')">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('unieap.school.renyuanguanli.baobei.timecardNumber')" prop="timecardNumber">
          <el-input v-model="form.timecardNumber" :placeholder="$t('unieap.comm.plsInput')" maxlength="20" />
        </el-form-item>
        <el-form-item>
          <el-button v-repeat @click="reacquireHandle()">
            <el-icon>
              <Search />
            </el-icon>
            {{ $t('unieap.comm.search') }}</el-button>
          <el-button v-repeat @click="clearJson(form), reacquireHandle()">
            <el-icon>
              <Refresh />
            </el-icon>
            {{ $t('unieap.comm.reset') }}</el-button>
        </el-form-item>
        <el-row :gutter="5" type="flex" justify="center">
          <el-col :span="15">
            <el-form-item>
              <el-button v-permission="'renyuanguanli.baobei:create'" type="primary" @click="addKidsHandle()">
                <el-icon>
                  <Plus />
                </el-icon>
                {{ $t('unieap.school.renyuanguanli.baobei.create')
}}</el-button>
              <el-button v-permission="'renyuanguanli.baobei:leaveBatch'" type="primary"
                :disabled="selection.length <= 0" @click="deleteHandle()">
                {{ $t('unieap.school.renyuanguanli.baobei.leaveBatch') }}</el-button>
              <el-button v-permission="'renyuanguanli.baobei:import'" type="primary" @click="deleteHandle()">
                {{ $t('unieap.comm.import') }}</el-button>
              <el-button v-permission="'renyuanguanli.baobei:viewImport'" type="primary" @click="deleteHandle()">
                {{ $t('unieap.comm.viewImport') }}</el-button>
              <el-button v-permission="'renyuanguanli.baobei:studentQRCode'" type="primary"
                :disabled="selection.length <= 0" @click="deleteHandle()">
                {{ $t('unieap.school.renyuanguanli.baobei.studentQRCode') }}</el-button>
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-form-item>
              <el-button v-permission="'renyuanguanli.baobei:kidsBindTimecard'" type="primary" @click="bindTimecardHandle()">
                {{ $t('unieap.school.renyuanguanli.baobei.kidsBindTimecard') }}</el-button>
              <el-button v-permission="'renyuanguanli.baobei:changeClass'" type="primary" @click="changeClassHandle()">
                {{ $t('unieap.school.renyuanguanli.baobei.changeClass') }}</el-button>
              <el-button v-permission="'renyuanguanli.baobei:gradeUp'" type="primary" @click="gradeUpHandle()">
                {{ $t('unieap.school.renyuanguanli.baobei.gradeUp') }}</el-button>
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle"
        @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.code')" prop="id" sortable='custom' width="60" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.kidsName')" prop="kidsName"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.gender')" prop="genderDesc" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.birthday')" prop="birthdayDesc"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.className')" prop="className"
          width="100" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.sourceType')"
          prop="sourceTypeDesc" width="100" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.contactName')"
          prop="contactNameDesc" width="100" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.contactNumber')"
          prop="contactNumber" width="100" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.registerDate')"
          prop="registerDateDesc" width="100" />
        <el-table-column align="center" :label="$t('unieap.school.renyuanguanli.baobei.timecardNumber')"
          prop="timecardNumber" width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="150" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'renyuanguanli.baobei:update'" type="primary" link
              @click="editKidsHandle(row.id)">{{
    $t('unieap.comm.update')
}}</el-button>
            <el-button v-permission="'renyuanguanli.baobei:contactManagement'" type="primary" link
              @click="contactHandle(row.id)">{{
    $t('unieap.school.renyuanguanli.baobei.contactManagement')
}}</el-button>
            <el-button v-permission="'renyuanguanli.baobei:bindTimecard'" type="primary" link
              @click="bindTimecardHandle(row.id)">{{
    $t('unieap.school.renyuanguanli.baobei.bindTimecard')
}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <AddKids ref="refAddKids" v-if="addkidsVisible" @refresh="getList" />
      <EditKids ref="refEditKids" v-if="editkidsVisible" @refresh="getList" />
      <EditContact ref="refEditContact" v-if="contactVisible" @refresh="getList" />
      <BindTimecard ref="refTimecard" v-if="timecardVisible" @refresh="getList" />
      <ChangeClass ref="refChangeClass" v-if="changeclassVisible" @refresh="getList" />
      <GradeUpW ref="refGradeUp" v-if="guvisible" @refresh="getList" />
    </template>
    <template #footer>
      <Page :page="page" @change="pageChangeHandle" />
    </template>
  </Container>
</template>
  
<script>
import { defineComponent, nextTick, onBeforeMount, reactive, ref, toRefs } from 'vue'

import { ElMessage, ElMessageBox } from 'element-plus'
import AddKids from './addkids'
import EditKids from './editkids'
import EditContact from './contact'
import BindTimecard from './bindtimecard'
import ChangeClass from './changeclass'
import GradeUpW from './gradeup'

import usePage from '@/mixins/page'
import { clearJson, havePermission, parseDate2Str } from '@/utils'

import { infoGradeDicDataApi } from '@/api/unieap/school/jiaowuguanli/nianji'
import { deleteApi, infoClassDicDataApi, graduation } from '@/api/unieap/school/jiaowuguanli/banji'
import { studentKidsPageApi } from '@/api/unieap/school/renyuanguanli/baobei'

export default defineComponent({
  components: { AddKids, EditKids, EditContact, BindTimecard, GradeUpW },
  setup() {
    const refForm = ref()
    const refTable = ref()
    const refAddKids = ref()
    const refEditKids = ref()
    const refEditContact = ref()
    const refTimecard = ref()
    const refChangeClass = ref()
    const refGradeUp = ref()
    const { page } = usePage()
    const data = reactive({
      loading: false,
      addkidsVisible: false,
      editkidsVisible: false,
      contactVisible: false,
      timecardVisible: false,
      changeclassVisible: false,
      guvisible: false,
      optionsGrade: [],
      optionsClass: [],
      form: {
        kidsName: '',
        contactName: '',
        gradeValue: '',
        classValue: '',
        contactNumber: '',
        registerDateRange: [],
        timecardNumber: '',
        sort: 'id',
        dir: 'asc'
      },
      list: [],
      selection: []
    })

    const selectionHandle = (val) => {
      data.selection = val
    }
    const gradeOptions = async () => {
      const dic = await infoGradeDicDataApi({})
      if (dic) {
        data.optionsGrade = dic.data
      }
    }
    const classOptions = async (gradeValue) => {
      const dic = await infoClassDicDataApi({ 'gradeId': gradeValue })
      if (dic) {
        data.optionsClass = dic.data
        data.form.classValue = ''
      }
    }

    const changeClassHandle = (id)=>{
      data.changeclassVisible = true
      nextTick(() => {
        refChangeClass.value.init(id)
      })

    }

    const gradeChange = (gradeValue) => {
      classOptions(gradeValue)
    }
    const getList = () => {
      const params = {
        sort: data.form.sort,
        dir: data.form.dir,
        kidsName: data.form.kidsName,
        contactName: data.form.contactName,
        gradeId: data.form.gradeValue,
        classId: data.form.classValue,
        contactNumber: data.form.contactNumber,
        registerDateStart: data.form.registerDateRange && data.form.registerDateRange.length > 0 ? parseDate2Str(data.form.registerDateRange[0]) : '',
        registerDateEnd: data.form.registerDateRange && data.form.registerDateRange.length > 1 ? parseDate2Str(data.form.registerDateRange[1]) : '',
        timecardNumber: data.form.timecardNumber,
        currentPage: page.current,
        pageSize: page.size
      }
      data.loading = true
      studentKidsPageApi(params).then(r => {
        if (r) {
          data.list = r.data.list
          page.total = r.data.total
        }
        nextTick(() => { data.loading = false })
      })
    }
    const addKidsHandle = (id) => {
      data.addkidsVisible = true
      nextTick(() => {
        refAddKids.value.init(id)
      })
    }
    const editKidsHandle = (id) => {
      data.editkidsVisible = true
      nextTick(() => {
        refEditKids.value.init(id)
      })
    }
    const contactHandle = (id) => {
      data.contactVisible = true
      nextTick(() => {
        refEditContact.value.init(id)
      })
    }
    const deleteHandle = (id) => {
      const ids = id ? [id] : data.selection.map(item => item.id)
      ElMessageBox.confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteApi({ keys: ids }).then(r => {
          if (r) {
            ElMessage({
              message: '操作成功!',
              type: 'success'
            })
            getList()
          }
        })
      }).catch(() => {
        // to do something on canceled
      })
    }

    const bindTimecardHandle = (id) => {
      data.timecardVisible = true
      nextTick(() => {
        refTimecard.value.init(id)
      })
    }

    const graduationHandle = (id) => {
      ElMessageBox.confirm(`确认将该班级设置为已毕业班级?毕业后，可在【离园宝贝】中查看宝贝信息`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        graduation({ 'id': id }).then(r => {
          if (r) {
            ElMessage({
              message: '操作成功!',
              type: 'success'
            })
            getList()
          }
        })
      }).catch(() => {
        // to do something on canceled
      })
    }

    /**
     * 查询
     */
    const reacquireHandle = () => {
      page.current = 1
      getList()
    }
    /**
     * column：当前列
     * prop：当前列需要排序的字段名
     * order：排序的规则（升序、降序和默认，默认就是没排序）
     * @param {*} column 
     * @param {*} prop 
     * @param {*} order 
     */
    const sortChange = (column) => {
      data.form.sort = column.prop
      if (column.prop) {
        if (column.order === 'ascending') {
          data.form.dir = 'asc'
        } else {
          data.form.dir = 'desc'
        }
      }
      getList()
    }

    const pageChangeHandle = (argPage) => {
      page.current = argPage.current
      page.size = argPage.size
      getList()
    }

    onBeforeMount(() => {
      gradeOptions()
      getList()
    })

    return {
      refForm,
      refTable,
      refAddKids,
      refEditKids,
      refEditContact,
      refTimecard,
      refChangeClass,
      refGradeUp,
      page,
      ...toRefs(data),
      sortChange,
      selectionHandle,
      clearJson,
      havePermission,
      gradeOptions,
      classOptions,
      getList,
      gradeChange,
      pageChangeHandle,
      reacquireHandle,
      addKidsHandle,
      editKidsHandle,
      contactHandle,
      deleteHandle,
      bindTimecardHandle,
      changeClassHandle,
      graduationHandle
    }
  }
})
</script>
  
<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
  