
/**
 * @description: 价格
 * @param {*} input
 * @return {*}
 */
export function isPrice(rule, value, callback) {
  if (!value) {
    return callback(new Error("金额不能为空"));
  }
  //var regex = /^\d*(\.?\d{0,1})/
  var regex = /^\d+(\.\d+)?$/
  if (!regex.test(value)) {
    return callback(new Error("请输入正确的金额"));
  } else {
    callback();
  }
}

/**
 * @description: 数字
 * @param {*} input
 * @return {*}
 */
export function isNumber(rule, value, callback) {
  if (!value) {
    return callback(new Error(rule.message));
  }
  //var regex = /^\d*(\.?\d{0,1})/
  var regex = /^\d+(\.\d+)?$/
  if (!regex.test(value)) {
    return callback(new Error(rule.message));
  } else {
    callback();
  }
}