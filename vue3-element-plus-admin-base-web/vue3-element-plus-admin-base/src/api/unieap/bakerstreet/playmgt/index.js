import service from '@/utils/request'


/**
 * @description: 分页
 * @param {*}
 * @return {*}
 */
export function dmPlayPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/dmlog/playPage',
    method: 'post',
    data: params,
    params: params
  })
}

/**
 * @description: 分页
 * @param {*}
 * @return {*}
 */
export function dmBrowsePageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/dmlog/browsePage',
    method: 'post',
    data: params,
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
    url: '/backstage/bakerstreet/playmgt/dmlog/info',
    method: 'post',
    data: params,
    params: params
  })
}

/**
 * @description: 玩家查询
 * @param {*}
 * @return {*}
 */
export function playerDicApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/dmlog/getDMDicData',
    method: 'post',
    data: params,
    params: params
  })
}

/**
 * @description: 新剧本列表
 * @param {*}
 * @return {*}
 */
export function scriptShowPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/dmlog/scriptShowPage',
    method: 'post',
    data: params,
    params: params
  })
}

/**
 * @description: 更新新剧本列表
 * @param {*}
 * @return {*}
 */
export function updateScriptShowApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/dmlog/updateScriptShow',
    method: 'post',
    data: params,
    params: params
  })
}

/**
 * @description: 优秀DM列表
 * @param {*}
 * @return {*}
 */
export function dmShowPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/dmlog/dmShowPage',
    method: 'post',
    data: params,
    params: params
  })
}

/**
 * @description: 更新优秀DM列表
 * @param {*}
 * @return {*}
 */
export function updateDmShowApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/dmlog/updateDmShow',
    method: 'post',
    data: params,
    params: params
  })
}


/**
 * @description: 黑名单状态变更
 * @param {*}
 * @return {*}
 */
export function updateBlacklistFlagApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/player/updateBlacklistFlag',
    method: 'post',
    params: params
  })
}

/**
 * @description: 黑名单状态变更
 * @param {*}
 * @return {*}
 */
export function updatePlayerTypeApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/player/updatePlayerType',
    method: 'post',
    params: params
  })
}

/**
 * @description: 开本权限变更
 * @param {*}
 * @return {*}
 */
export function updateScriptFlagApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/player/updateScriptFlag',
    method: 'post',
    params: params
  })
}

/**
 * @description: 回到玩家变更
 * @param {*}
 * @return {*}
 */
export function updateChangeFlagApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/player/updateChangeFlag',
    method: 'post',
    params: params
  })
}

/**
 * @description: 分页
 * @param {*}
 * @return {*}
 */
export function playerPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/player/playerPage',
    method: 'post',
    data: params,
    params: params
  })
}

/**
 * @description: 黑名单列表
 * @param {*}
 * @return {*}
 */
export function blacklistPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/player/blacklistPage',
    method: 'post',
    params: params
  })
}

/**
 * @description: 今日开房列表
 * @param {*}
 * @return {*}
 */
export function roomlistPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/playing/roomlistPage',
    method: 'post',
    params: params
  })
}

/**
 * @description: 剧本点赞列表
 * @param {*}
 * @return {*}
 */
export function scriptlikelistPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/playmgt/playing/scriptlikelistPage',
    method: 'post',
    params: params
  })
}