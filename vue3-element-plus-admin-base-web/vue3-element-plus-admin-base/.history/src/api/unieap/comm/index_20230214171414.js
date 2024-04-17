import service from '@/utils/request'

/**
 * 简介：axios传参一般分两种方式，一种是用data,一种是用params，至于到底用data还是params这取决与后端
 * 1、一般后端接收参数时、使用@RequestBody就代表接收的是一个类（对象），此时前端应该用data传参
 * 2、后端用@RequestParam就代表单个数值（比如一个字符串，一个int类型数据等）。此时前端应该用params传参
 * ////重点
 * 3、params 是通过key-value方式传递，后端可以多个对象接收，Springboot框架可以自动映射到参数或者对象属性
 * 4、data 通过json格式传递参数到后台，后端只能通过@RequestBody 接收并转换成对象
 * 
 * 该文件包含公共处理函数：字典值获取infoDicDataApi,通用业务处理方法bizHandleApi
 */

/**
 * @description: 数据库序列号
 * @param {{'serialName':'unieap'}}
 * @return {data:seqNumber}
 * @author: chai
 */
export function getDBSeqApi(params) {
  return service({
    url: `/backstage/common/getDBSeq`,
    method: 'post',
    params: params
  })
}


/**
 * @description: 字典值
 * @param {{'groupCode':'ACTIVATE_FLAG'}}
 * @return {data:[{dicCode:'value',dicName:'name'}]}
 * @author: chai
 */
export function infoDicDataApi(params) {
  return service({
    url: `/backstage/common/getDicData`,
    method: 'post',
    params: params 
  })
}
/**
 * 业务处理公共函数,如果传递的是json对象，需要设置参数类型为type=data
 * @param {*,params.url 为必填参数} params 
 * @returns 
 */
export function bizHandleApi(params) {
  if (params.type === 'data') {
    //请求参数是复杂的json结构,通过body内容发送,后端只能一个VO接收
    return service({
      url: params.url,
      method: 'post',
      data: params
    })
  } else if (params.type === 'params') {
    //请求参数是key-value结构，通过header发送,后端Springboot框架自动映射到参数或者对象属性
    return service({
      url: params.url,
      method: 'post',
      params: params
    })
  } else {
    //默认采用header中key-value 发送
    return service({
      url: params.url,
      method: 'post',
      params: params
    })
  }
}
