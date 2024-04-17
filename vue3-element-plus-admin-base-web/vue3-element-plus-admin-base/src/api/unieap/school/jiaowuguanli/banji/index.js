import service from '@/utils/request'

/**
 * 年级字典值
 * @param {*} params 
 * @returns 
 */
 export function infoClassDicDataApi(params) {
  return service({
    url: '/backstage/jiaowuguanli/banji/getClassDicData',
    method: 'post',
    params: params
  })
}

/**
 * 员工字典值
 * @param {*} params 
 * @returns 
 */
 export function infoStaffInfoDicDataApi(params) {
  return service({
    url: '/backstage/renyuanguanli/staff/getStaffInfoDicDataList',
    method: 'post',
    params: params
  })
}

/**
 * @description: 分页
 * @param {*}
 * @return {*}
 */
 export function pageApi(params) {
  return service({
    url: '/backstage/jiaowuguanli/banji/page',
    method: 'post',
    params: params
  })
}

/**
 * @description: 信息
 * @param {*}
 * @return {*}
 */
 export function infoApi(params) {
  return service({
    url: '/backstage/jiaowuguanli/banji/info',
    method: 'post',
    params: params
  })
}

/**
 * @description: 新增
 * @param {*}
 * @return {*}
 */
 export function addApi(params) {
  params.assistTeachers = params.assistTeachers.join(",");
  params.othertTeachers = params.othertTeachers.join(",");
  return service({
    url: '/backstage/jiaowuguanli/banji/create',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑
 * @param {*}
 * @return {*}
 */
 export function editApi(params) {
  params.assistTeachers = params.assistTeachers.join(",");
  params.othertTeachers = params.othertTeachers.join(",");
  return service({
    url: '/backstage/jiaowuguanli/banji/update',
    method: 'post',
    data: params
  })
}

/**
 * @description: 删除
 * @param {*}
 * @return {*}
 */
 export function deleteApi(params) {
  return service({
    url: '/backstage/jiaowuguanli/banji/deleteBatch',
    method: 'post',
    data: params
  })
}

/**
 * @description: 升班
 * @param {*}
 * @return {*}
 */
 export function gradeUp(params) {
  return service({
    url: '/backstage/jiaowuguanli/banji/gradeUp',
    method: 'post',
    data: params
  })
}

/**
 * @description: 毕业
 * @param {*}
 * @return {*}
 */
 export function graduation(params) {
  return service({
    url: '/backstage/jiaowuguanli/banji/graduation',
    method: 'post',
    data: params
  })
}

export function setStatusApi(params) {
  return service({
    url: '/backstage/enterprise/page',
    method: 'get',
    params: params
  })
}