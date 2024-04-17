import service from '@/utils/request'
/**
 * @description: 分页
 * @param {*}
 * @return {*}
 * @author: gumingchen
 */
export function pageApi(params) {
  return service({
    url: '/backstage/demo/page',
    method: 'get',
    params: params
  })
}

/**
 * @description: 信息
 * @param {*}
 * @return {*}
 * @author: gumingchen
 */
 export function infoApi(params) {
  return service({
    url: `/backstage/demo/info`,
    method: 'get',
    params: params
  })
}
export function infoDicDataApi(params) {
  return service({
    url: `/backstage/common/getDicData`,
    method: 'get',
    params: params
  })
}

/**
 * @description: 新增
 * @param {*}
 * @return {*}
 * @author: gumingchen
 */
 export function addApi(params) {
  return service({
    url: `/backstage/demo/create`,
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑
 * @param {*}
 * @return {*}
 * @author: gumingchen
 */
export function editApi(params) {
  return service({
    url: `/backstage/demo/update`,
    method: 'post',
    data: params
  })
}

/**
 * @description: 删除
 * @param {*}
 * @return {*}
 * @author: gumingchen
 */
export function deleteApi(params) {
  return service({
    url: `/backstage/demo/deleteBatch`,
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

export function sendEmailApi(params) {
  return service({
    url: '/backstage/demo/email',
    method: 'post',
    data: params
  })
}