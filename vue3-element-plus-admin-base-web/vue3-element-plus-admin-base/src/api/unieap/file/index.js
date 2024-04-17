import service from '@/utils/request'
import { parseJson2Param, getApiBaseUrl } from '@/utils'

/**
 * @description: 上传
 * @param {*}
 * @return {*}
 * @author: chai
 */
export function uploadApi(params) {
  let result = ''
  const options = {
    url: '/backstage/unieap/file/upload',
    params: params
  }
  result = `${getApiBaseUrl() + options.url}${options.params ? '?'+parseJson2Param(options.params) : ''}`
  return result
}

/**
 * @description: 删除文件
 * @param {{'fileId':''}}
 * @return {*}
 * @author: chai
 */
export function deleteApi(params) {
  return service({
    url: `/backstage/unieap/file/delete`,
    method: 'post',
    params: params
  })
}
