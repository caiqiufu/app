import service from '@/utils/request'

/**
 * 公司字典值
 * @param {*} params 
 * @returns 
 */
export function companyDicDataApi(params) {
  return service({
    url: '/backstage/projectoa/supervisionmgt/company/getCompanyDicData',
    method: 'post',
    params: params
  })
}

/**
 * 项目字典值
 * @param {*} params 
 * @returns 
 */
export function projectDicDataApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectinfo/projectDicData',
    method: 'post',
    params: params
  })
}