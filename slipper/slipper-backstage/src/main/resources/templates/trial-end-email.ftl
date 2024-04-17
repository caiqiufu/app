<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
</head>
<body>
    <table>
        <tr style = "height:50px">
            <td>${userName}:先生/小姐:</td>
        </tr>
        <tr style = "height:20px">
        </tr>
        <tr style = "height:50px">
            <td><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的账户【${accountCode}】将于【${tradeType}】结束试用，请及时更新订阅服务，避免影响策略通知信息获取！</td>
        </tr>
        <tr style = "height:20px">
        </tr>
        <tr style = "height:50px">
            <td>请在应用程序上实时查看策略相关信息，点击<a href="${LZY_APP_URL}">应用下载<a/></td>
        </tr>
        <tr style = "height:50px">
            <td>风险提示:市场波动较大，请实时关注行情变化并执行相应操作。</td>
        </tr>
        <tr style = "height:20px">
        </tr>
    </table>

    <p>
        温馨提示：该信息为龙知易科技EA系统自动发出，请勿回复！
    </p>
    <p>
        <span>深圳龙知易科技有限公司</span>
    </p>
    <p>
        客服微信:${servicePhoneNumber}
    </p>
    <p>
        客服电邮:${serviceEmail}
    </p>
</body>
</html>