import service from '@/utils/request'


/**
 * @description: 更新剧本上架状态
 * @param {*}
 * @return {*}
 */
export function updatePublishFlagApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/updatePublishFlag',
    method: 'post',
    params: params
  })
}

/**
 * @description: 更新剧本DM列表
 * @param {*}
 * @return {*}
 */
export function updateScriptDMApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/updateScriptDM',
    method: 'post',
    params: params
  })
}

/**
 * @description: 新增剧本
 * @param {*}
 * @return {*}
 */
export function createScriptApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/create',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑剧本
 * @param {*}
 * @return {*}
 */
export function editScriptApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/update',
    method: 'post',
    data: params
  })
}

/**
 * @description: 剧本列表
 * @param {*}
 * @return {*}
 */
export function scriptPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/scriptPage',
    method: 'post',
    params: params
  })
}

/**
 * @description: 角色信息
 * @param {*}
 * @return {*}
 */
export function scriptInfoApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/scriptInfo',
    method: 'post',
    data: params
  })
}

/**
 * @description: 角色列表
 * @param {*}
 * @return {*}
 */
export function rolePageApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/rolePage',
    method: 'post',
    params: params
  })
}

/**
 * @description: 新增角色
 * @param {*}
 * @return {*}
 */
export function createRoleApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/createRole',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑角色
 * @param {*}
 * @return {*}
 */
export function editRoleApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/editRole',
    method: 'post',
    data: params
  })
}

/**
 * @description: 角色信息
 * @param {*}
 * @return {*}
 */
export function roleInfoApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/roleInfo',
    method: 'post',
    data: params
  })
}

/**
 * @description: 章节列表
 * @param {*}
 * @return {*}
 */
export function chapterPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/chapterPage',
    method: 'post',
    params: params
  })
}

/**
 * @description: 新增章节
 * @param {*}
 * @return {*}
 */
export function createChapterApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/createChapter',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑章节
 * @param {*}
 * @return {*}
 */
export function editChapterApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/editChapter',
    method: 'post',
    data: params
  })
}

/**
 * @description: 章节信息
 * @param {*}
 * @return {*}
 */
export function chapterInfoApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/chapterInfo',
    method: 'post',
    data: params
  })
}

/**
 * @description: 章节列表
 * @param {*}
 * @return {*}
 */
export function clueDetailPageApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/clueDetailPage',
    method: 'post',
    params: params
  })
}

/**
 * @description: 剧本幕列表
 * @param {*}
 * @return {*}
 */
export function clueDetailInfoApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/clueDetailInfo',
    method: 'post',
    data: params
  })
}

/**
 * @description: 多选图片新增剧本幕
 * @param {*}
 * @return {*}
 */
export function createClueDetailByBatchApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/createClueDetailByBatch',
    method: 'post',
    data: params
  })
}

/**
 * @description: 编辑剧本幕
 * @param {*}
 * @return {*}
 */
export function editClueDetailApi(params) {
  return service({
    url: '/backstage/bakerstreet/scriptmgt/scriptinfo/editClueDetail',
    method: 'post',
    data: params
  })
}