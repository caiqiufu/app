import service from '@/utils/request'

/**
 * 年级字典值
 * @param {*} params 
 * @returns 
 */
 export function infoGradeDicDataApi(params) {
  return service({
    url: '/backstage/jiaowuguanli/nianji/getGradeDicData',
    method: 'get',
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
    url: '/backstage/jiaowuguanli/nianji/page',
    method: 'get',
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
    url: '/backstage/jiaowuguanli/nianji/info',
    method: 'get',
    params: params
  })
}

/**
 * @description: 新增
 * @param {*}
 * @return {*}
 */
 export function addApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectbid/create',
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
  return service({
    url: '/backstage/projectoa/projectmgt/projectbid/update',
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
    url: '/backstage/jiaowuguanli/nianji/deleteBatch',
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