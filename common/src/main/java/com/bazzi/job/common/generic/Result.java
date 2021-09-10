package com.bazzi.job.common.generic;

import com.bazzi.job.common.ex.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Result")
public class Result<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = -6391477326294342113L;
    @ApiModelProperty(value = "泛型对象")
    private T data;// 数据

    @ApiModelProperty(value = "状态，true成功，false失败")
    private boolean status = true;// 状态

    @ApiModelProperty(value = "状态码")
    private String code = "";// 状态码

    @ApiModelProperty(value = "消息")
    private String message = "";// 消息

    public Result() {
    }

    public Result(T data) {
        super();
        this.data = data;
    }

    public void setError(String code, String message) {
        this.code = code;
        this.message = message;
        this.status = false;
    }

    /**
     * 获取预期数据，status为true返回data，否则根据code和message扔出异常
     *
     * @return data里数据
     */
    @JsonIgnore
    public T getExpectedData() {
        if (!status)
            throw new BusinessException(code, message);
        return data;
    }

    /**
     * 构建一个data数据的成功结果
     *
     * @param data 数据
     * @param <T>  泛型类型
     * @return 成功结果
     */
    public static <T extends Serializable> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    /**
     * 构建一个以错误码和提示信息的失败结果
     *
     * @param code    错误码
     * @param message 提示信息
     * @param <T>     泛型类型
     * @return 失败结果
     */
    public static <T extends Serializable> Result<T> failure(String code, String message) {
        Result<T> result = new Result<>();
        result.setError(code, message);
        return result;
    }
}
