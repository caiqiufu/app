import service from '@/utils/request'

/**
 * @description: 分配项目属性
 * @param {*}
 * @return {*}
 */
export function assignAttributeApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectInfo/assignAttribute',
    method: 'post',
    data: params
  })
}

/**
 * @description: 修改属性序号
 * @param {*}
 * @return {*}
 */
export function updateSeqApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectInfo/updateSeq',
    method: 'post',
    data: params
  })
}

/**
 * @description: 修改属性值
 * @param {*}
 * @return {*}
 */
export function updateAttributeValueApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectInfo/updateAttributeValue',
    method: 'post',
    data: params
  })
}

/**
 * @description: 新增
 * @param {*}
 * @return {*}
 */
export function detailpageApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectinfo/detailpage',
    method: 'post',
    params: params
  })
}

/**
 * @description: 修改属性值
 * @param {*}
 * @return {*}
 */
export function updateNotifyFlagApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectInfo/updateNotifyFlag',
    method: 'post',
    params: params
  })
}
