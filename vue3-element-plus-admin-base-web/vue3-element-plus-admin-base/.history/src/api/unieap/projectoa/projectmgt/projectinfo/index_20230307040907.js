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
    params: params
  })
}

/**
 * @description: 修改属性序号
 * @param {*}
 * @return {*}
 */
export function seqEditApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectInfo/seqUpdate',
    method: 'post',
    params: params
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