package com.bazzi.job.common.generic;

public enum StatusCode {
    CODE_000("000", "成功"),

    // 客户端需要处理的情况
    CODE_001("001", "登录无效，请重新登录"),
    CODE_002("002", "请求参数有误，请检查"),
    CODE_003("003", "系统异常，请稍后再试"),
    CODE_004("004", "访问ip不合法！"),
    CODE_005("005", "不支持的请求方式！"),
    CODE_006("006", "异常的响应状态: %s"),
    CODE_007("007", "JSON序列化异常"),
    CODE_008("008", "该接口不支持`%s`方式请求"),
    CODE_009("009", "HTTP请求不正确"),

    CODE_140("140", "不支持的处理类型(%s)"),
    CODE_141("141", "%s对应记录不存在，请检查！"),
    CODE_142("142", "%s的任务状态为禁用，操作失败"),
    CODE_143("143", "%s的任务名称为空，操作失败"),
    CODE_144("144", "%s的任务分组为空，操作失败"),
    CODE_145("145", "%s的cron表达式为空，操作失败"),
    CODE_146("146", "%s的cron表达式(%s)无效，操作失败"),
    CODE_147("147", "%s的groovy脚本为空，操作失败"),

    CODE_300("300", "未知异常"),
    CODE_301("301", "ID为：%s 的项目不存在"),
    CODE_302("302", "ID为：%s 的项目状态无效"),
    CODE_303("303", "ID为：%s 的监控策略不存在"),
    CODE_304("304", "ID为：%s 的监控策略状态无效"),
    CODE_305("305", "ID为：%s 的报警配置不存在"),
    CODE_306("306", "ID为：%s 的报警配置状态无效"),
    CODE_307("307", "ID为：%s 的报警用户组不存在"),
    CODE_308("308", "ID为：%s 的报警用户组状态无效"),
    CODE_309("309", "ID为：%s 的报警用户组下不存在状态正常的用户"),

    ;

    private final String code;
    private final String message;

    StatusCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
