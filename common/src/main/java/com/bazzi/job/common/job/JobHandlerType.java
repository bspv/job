package com.bazzi.job.common.job;

public enum JobHandlerType {
    ADD(0, "新增"),
    UPDATE(1, "更新"),
    PAUSE(2, "暂停"),
    RESUME(3, "恢复"),
    DELETE(4, "删除"),
    LIST_ALL(5, "同步所有job信息");

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    JobHandlerType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static JobHandlerType getByCode(int code) {
        JobHandlerType[] values = JobHandlerType.values();
        for (JobHandlerType value : values) {
            if (value.code == code)
                return value;
        }
        return null;
    }
}
