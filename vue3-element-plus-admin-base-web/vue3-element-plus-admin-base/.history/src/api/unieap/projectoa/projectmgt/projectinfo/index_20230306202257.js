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
 * @description: 新增
 * @param {*}
 * @return {*}
 */
export function detailpageApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectinfo/detailpage',
    method: 'post',
    data: params
  })
}