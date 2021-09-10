<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        body {
            padding: 0;
            margin: 0;
            width: 100%;
            height: auto;
        }
        .outer-table, .inner-table {
            border-top: 1px solid #808080;
            border-left: 1px solid #808080;
            width: 100%;
        }
        .outer-table td, .inner-table td{
            border-bottom: 1px solid #808080;
            border-right: 1px solid #808080;
            height: 30px;
            line-height: 30px;
        }
        .container {
            padding: 20px;
        }
        .item {
            width: 30%;
        }
        .inner-table {
            padding: 0;
        }
    </style>
</head>
<body>
<div class="container">
    <table class="outer-table">
        <tr>
            <td class="item">项目名</td>
            <td>${projectName}</td>
        </tr>
        <tr>
            <td class="item">报警时间</td>
            <td>${triggerTime}</td>
        </tr>
        <tr>
            <td class="item">策略名</td>
            <td>${monitorName}</td>
        </tr>
        <tr>
            <td class="item">机器IP</td>
            <td>${ip}</td>
        </tr>
        <tr>
            <td class="item">机器平台</td>
            <td>${platform}</td>
        </tr>
        <tr>
            <td class="item">描述</td>
            <td>${content}</td>
        </tr>
        <tr>
            <td class="item">消息详情</td>
            <#if captureType == 0>
                <td>${message}</td>
            <#else>
                <td class="inner-table ">
                    <table class="inner">
                        <#list msgMap?keys as key>
                            <tr>
                                <td class="item">${key}</td>
                                <td class="val"> ${msgMap[key]}</td>
                            </tr>
                        </#list>
                    </table>
                </td>
            </#if>
        </tr>
    </table>
</div>
</body>
</html>