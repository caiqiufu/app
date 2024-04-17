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
    url: '/backstage/projectoa/projectmgt/projectInfo/projectDicData',
    method: 'post',
    params: params
  })
}

/**
 * 项目施工部位字典值
 * @param {*} params 
 * @returns 
 */
export function buildingPartDicDataApi(params) {
  return service({
    url: '/backstage/projectoa/projectmgt/projectInfo/buildingPartDicData',
    method: 'post',
    params: params
  })
}