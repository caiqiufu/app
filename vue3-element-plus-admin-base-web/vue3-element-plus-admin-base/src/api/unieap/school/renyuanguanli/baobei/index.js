import service from '@/utils/request'

/**
 * @description: 在园分页
 * @param {*}
 * @return {*}
 */
export function studentKidsPageApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/page',
    method: 'post',
    params: params
  })
}

/**
 * @description: 新增宝贝信息
 * @param {*}
 * @return {*}
 */
export function addKidsApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/create',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑宝贝基本信息
 * @param {*}
 * @return {*}
 */
export function editKidsApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/update',
    method: 'post',
    data: params
  })
}

/**
 * @description: 新增宝贝学籍信息
 * @param {*}
 * @return {*}
 */
export function addRegisterInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/createRegister',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑宝贝学籍信息
 * @param {*}
 * @return {*}
 */
export function editRegisterInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/updateRegister',
    method: 'post',
    data: params
  })
}

/**
 * @description: 宝贝信息
 * @param {*}
 * @return {*}
 */
export function kidsInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/kidsInfo',
    method: 'post',
    params: params
  })
}

/**
 * @description: 宝贝学籍信息
 * @param {*}
 * @return {*}
 */
export function registerInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/registerInfo',
    method: 'post',
    params: params
  })
}

/**
 * @description: 联系人列表信息
 * @param {*}
 * @return {*}
 */
export function contactInfoListApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/contactInfoList',
    method: 'post',
    params: params
  })
}

/**
 * @description: 联系人信息
 * @param {*}
 * @return {*}
 */
export function contactInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/contactInfo',
    method: 'post',
    params: params
  })
}

/**
 * @description: 新增联系人信息
 * @param {*}
 * @return {*}
 */
export function addContactInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/createContact',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑联系人信息
 * @param {*}
 * @return {*}
 */
export function editContactInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/updateContact',
    method: 'post',
    data: params
  })
}

/**
 * @description: 删除联系人信息
 * @param {*}
 * @return {*}
 */
export function deleteContactInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/deleteContact',
    method: 'post',
    data: params
  })
}

/**
 * @description: 设置第一联系人
 * @param {*}
 * @return {*}
 */
export function setFirstContactInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/setFirstContact',
    method: 'post',
    data: params
  })
}

/**
 * @description: 重置联系人密码
 * @param {*}
 * @return {*}
 */
export function resetPasswordContactInfoApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/resetPasswordContact',
    method: 'post',
    data: params
  })
}

/**
 * @description: 绑定考勤卡
 * @param {*}
 * @return {*}
 */
export function bindTimecardApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/bindTimecard',
    method: 'post',
    data: params
  })
}

/**
 * 行政区域字典值, get 方式后端直接接收，post 方式后端采用@RequestBody对象接收，form-data时后端直接参数名称接收
 * @param {*} params 
 * @returns 
 */
export function infoDistrictDicDataApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/getDistrictDicData',
    method: 'post',
    params: params
  })
}

/**
 * @description: 离园分页
 * @param {*}
 * @return {*}
 */
export function leaveKidsPageApi(params) {
  return service({
    url: '/backstage/renyuanguanli/baobei/page',
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
    url: '/backstage/renyuanguanli/baobei/info',
    method: 'post',
    params: params
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