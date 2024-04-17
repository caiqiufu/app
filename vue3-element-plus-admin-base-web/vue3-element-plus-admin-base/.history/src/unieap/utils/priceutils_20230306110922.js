/**
 * @description: 数字转换成大写金额
 * @param {money 数字金额} input
 * @return {string 大写金额}
 */
export function digitUppercase(money) {
    var fraction = ['角', '分'];
    var digit = [
        '零', '壹', '贰', '叁', '肆',
        '伍', '陆', '柒', '捌', '玖'
    ];
    var unit = [
        ['元', '万', '亿'],
        ['', '拾', '佰', '仟']
    ];
    var head = money < 0 ? '欠' : '';
    money = Math.abs(money);
    var s = '';
    for (var i = 0; i < fraction.length; i++) {
        s += (digit[Math.floor(money * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
    }
    s = s || '整';
    money = Math.floor(money);
    for (var i = 0; i < unit[0].length && money > 0; i++) {
        var p = '';
        for (var j = 0; j < unit[i].length && money > 0; j++) {
            p = digit[money % 10] + unit[i][j] + p;
            money = Math.floor(money / 10);
        }
        s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
    }
    return head + s.replace(/(零.)*零元/, '元')
        .replace(/(零.)+/g, '零')
        .replace(/^整$/, '零元整');
}