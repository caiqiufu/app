<template>
  <Container>
    <template #header>
      <el-form ref="refForm" :inline="true" @keyup.enter="reacquireHandle()">
        <el-form-item :label="$t('unieap.school.comm.grade')">
          <el-select v-model="form.gradeValue" :placeholder="$t('unieap.comm.plsSelect')"
            @change="gradeChange(form.gradeValue)">
            <el-option v-for="item in optionsGrade" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('unieap.school.comm.class')">
          <el-select v-model="form.classValue" :placeholder="$t('unieap.comm.plsSelect')">
            <el-option v-for="item in optionsClass" :key="item.dicCode" :label="item.dicName" :value="item.dicCode">
            </el-option>
          </el-select>
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
          <el-button v-permission="'jiaowuguanli.banji:create'" type="primary" @click="addEditHandle()">
            <el-icon>
              <Plus />
            </el-icon>
            {{ $t('unieap.school.jiaowuguanli.banji.create')
            }}</el-button>
          <el-button v-permission="'jiaowuguanli.banji:delete'" type="danger" :disabled="selection.length <= 0"
            @click="deleteHandle()">
            <el-icon>
              <Delete />
            </el-icon>
            {{ $t('unieap.comm.deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
    </template>
    <template #default>
      <el-table ref="refTable" v-loading="loading" :data="list" @selection-change="selectionHandle" @sort-change="sortChange" border>
        <el-table-column align="center" type="selection" width="50" />
        <el-table-column align="center" :label="$t('unieap.comm.code')" prop="id" sortable='custom' width="60" />
        <el-table-column align="center" :label="$t('unieap.school.comm.grade')" prop="gradeName" width="60" />
        <el-table-column align="center" :label="$t('unieap.school.comm.class')" prop="className" />
        <el-table-column align="center" :label="$t('unieap.school.jiaowuguanli.banji.mainTeacher')"
          prop="mainTeacherName" />
        <el-table-column align="center" :label="$t('unieap.school.jiaowuguanli.banji.nurse')" prop="nurseName" />
        <el-table-column align="center" :label="$t('unieap.school.jiaowuguanli.banji.assistTeacher')"
          prop="assistTeachersName" show-overflow-tooltip />
        <el-table-column align="center" :label="$t('unieap.school.jiaowuguanli.banji.othertTeacher')"
          prop="othertTeachersName" show-overflow-tooltip />
        <el-table-column align="center" :label="$t('unieap.school.jiaowuguanli.banji.kindsCount')"
          prop="kidsCount" />
        <el-table-column align="center" :label="$t('unieap.comm.createDate')" prop="createDate" width="100" />
        <el-table-column align="center" :label="$t('unieap.comm.operation')" width="150" fixed="right">
          <template v-slot="{ row }">
            <el-button v-permission="'jiaowuguanli.banji:update'" type="primary" link @click="addEditHandle(row.id)">{{
                $t('unieap.comm.update')
            }}</el-button>
            <el-button v-permission="'jiaowuguanli.banji:gradeUp'" type="primary" link @click="gradeUpHandle(row.id)">{{
                $t('unieap.school.jiaowuguanli.banji.gradeUp')
            }}</el-button>
            <el-button v-permission="'jiaowuguanli.banji:graduation'" type="primary" link
              @click="graduationHandle(row.id)">{{
                  $t('unieap.school.jiaowuguanli.banji.graduation')
              }}</el-button>
            <el-button type="primary" link @click="addEditHandle(row.id)">{{
                $t('unieap.school.jiaowuguanli.banji.kidsList')
            }}</el-button>
            <el-button icon="el-icon-delete" v-permission="'jiaowuguanli.banji:delete'" type="danger" link
              @click="deleteHandle(row.id)">{{
                  $t('unieap.comm.delete')
              }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <AddEdit ref="refAddEdit" v-if="visible" @refresh="getList" />
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
import AddEdit from './components/add-edit'
import GradeUpW from './components/gradeup'

import usePage from '@/mixins/page'
import { clearJson, havePermission } from '@/utils'

import { infoGradeDicDataApi } from '@/api/unieap/school/jiaowuguanli/nianji'
import { pageApi, deleteApi, infoClassDicDataApi,graduation } from '@/api/unieap/school/jiaowuguanli/banji'

export default defineComponent({
  components: { AddEdit,GradeUpW },
  setup() {
    const refForm = ref()
    const refTable = ref()
    const refAddEdit = ref()
    const refGradeUp = ref()
    const { page } = usePage()
    const data = reactive({
      loading: false,
      visible: false,
      guvisible:false,
      optionsGrade: [],
      optionsClass: [],
      form: {
        gradeValue: '',
        classValue: '',
        sort:'id',
        dir:'asc'
      },
      list: [],
      selection: []
    })
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

    const gradeChange = (gradeValue) => {
      classOptions(gradeValue)
    }

    const getList = () => {
      const params = {
        sort: data.form.sort,
        dir: data.form.dir,
        gradeId: data.form.gradeValue,
        id: data.form.classValue,
        currentPage: page.current,
        pageSize: page.size
      }
      data.loading = true
      pageApi(params).then(r => {
        if (r) {
          data.list = r.data.list
          page.total = r.data.total
        }
        nextTick(() => { data.loading = false })
      })
    }

    const reacquireHandle = () => {
      page.current = 1
      getList()
    }

    const addEditHandle = (id) => {
      data.visible = true
      nextTick(() => {
        refAddEdit.value.init(id)
      })
    }

    const gradeUpHandle = (id) => {
      data.guvisible = true
      nextTick(() => {
        refGradeUp.value.init(id)
      })
    }

    const deleteHandle = (id) => {
      const ids = id ? id : data.selection.map(item => item.id).join(",")
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
     
    const graduationHandle = (id) => {
      ElMessageBox.confirm(`确认将该班级设置为已毕业班级?毕业后，可在【离园宝贝】中查看宝贝信息`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        graduation({'id':id}).then(r => {
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


    const selectionHandle = (val) => {
      data.selection = val
    }
    /**
     * column：当前列
     * prop：当前列需要排序的字段名
     * order：排序的规则（升序、降序和默认，默认就是没排序）
     * @param {*} column 
     * @param {*} prop 
     * @param {*} order 
     */
    const sortChange = (column)=>{
      data.form.sort = column.prop
      if (column.prop){
        if (column.order === 'ascending'){
          data.form.dir = 'asc'
        }else{
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
      refAddEdit,
      refGradeUp,
      page,
      ...toRefs(data),
      gradeOptions,
      classOptions,
      gradeChange,
      getList,
      pageChangeHandle,
      reacquireHandle,
      sortChange,
      addEditHandle,
      deleteHandle,
      gradeUpHandle,
      graduationHandle,
      selectionHandle,
      clearJson,
      havePermission
    }
  }
})
</script>
  
<style lang="scss" scoped>
.el-tag+.el-tag {
  margin-left: 5px;
}
</style>
  