import service from '@/utils/request'

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