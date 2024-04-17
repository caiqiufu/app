/**
 * 千分位钱
 */
export function moneyFormatter(row, column, cellValue, index) {
    /*
         * 参数说明：
         * number：要格式化的数字
         * decimals：保留几位小数
         * dec_point：小数点符号
         * thousands_sep：千分位符号
         * roundtag:舍入参数，默认 'ceil' 向上取,'floor'向下取,'round' 四舍五入
         * */
    var number = cellValue
    var decimals = 2
    var decPoint = '.'
    var thousandsSep = ','
    var roundtag = ''
    number = (number + '').replace(/[^0-9+-Ee.]/g, '')
    roundtag = roundtag || 'ceil' // 'ceil','floor','round'
    var n = !isFinite(+number) ? 0 : +number
    var prec = !isFinite(+decimals) ? 0 : Math.abs(decimals)
    var sep = (typeof thousandsSep === 'undefined') ? ',' : thousandsSep
    var dec = (typeof decPoint === 'undefined') ? '.' : decPoint
    var s = ''
    var toFixedFix = function (n, prec) {
        var k = Math.pow(10, prec)
        console.log()

        return '' + parseFloat(Math[roundtag](parseFloat((n * k).toFixed(prec * 2))).toFixed(prec * 2)) / k
    }
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.')
    var re = /(-?\d+)(\d{3})/
    while (re.test(s[0])) {
        s[0] = s[0].replace(re, '$1' + sep + '$2')
    }

    if ((s[1] || '').length < prec) {
        s[1] = s[1] || ''
        s[1] += new Array(prec - s[1].length + 1).join('0')
    }
    return s.join(dec)
}
/**
 * 时间格式化为年月日
 * @param {*} row 
 * @param {*} column 
 * @param {*} cellValue 
 * @param {*} index 
 * @returns 
 */
export function dateFormatter(row, column, cellValue, index) {
    var dd = new Date(cellValue)
    var y = dd.getFullYear()
    var m = dd.getMonth() + 1 < 10 ? '0' + (dd.getMonth() + 1) : dd.getMonth() + 1
    var d = dd.getDate() < 10 ? '0' + dd.getDate() : dd.getDate()
    return y + '年' + m + '月' + d + '日'
}