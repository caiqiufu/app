
/**
 * @description: 数字
 * @param {*} input
 * @return {*}
 */
export function isNumber(value) {
    //var regex = /^\d*(\.?\d{0,1})/
    var regex = /^\d+(\.\d+)?$/
    return regex.test(value)
  }

  