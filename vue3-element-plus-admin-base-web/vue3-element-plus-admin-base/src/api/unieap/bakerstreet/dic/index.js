import service from '@/utils/request'

/**
 * 剧本字典值
 * @param {*} params 
 * @returns 
 */
export function scriptDicDataApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/getScriptDicData',
    method: 'post',
    data: params,
    params:params
  })
}

/**
 * DM字典值
 * @param {*} params 
 * @returns 
 */
export function playerDicDataApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/player/getPlayerDicData',
    method: 'post',
    data: params,
    params:params
  })
}

/**
 * DM字典值
 * @param {*} params 
 * @returns 
 */
export function scriptDMDicDataApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/getScriptDMDicData',
    method: 'post',
    data: params,
    params:params
  })
}

/**
 * 角色数字典值
 * @param {*} params 
 * @returns 
 */
export function roleDicDataApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/getRoleDicData',
    method: 'post',
    data: params,
    params:params
  })
}

/**
 * 章节数字典值
 * @param {*} params 
 * @returns 
 */
export function chapterNumberDicDataApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/getChapterNumberDicData',
    method: 'post',
    data: params,
    params:params
  })
}

/**
 * 线索幕数字典值
 * @param {*} params 
 * @returns 
 */
export function clueDicDataApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/getClueDicData',
    method: 'post',
    data: params,
    params:params
  })
}