package com.ecnu.counseling.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractResponse {

    /**
     * 请求处理状态
     */
    private Integer code;
    /**
     * 状态信息
     */
    private String message;


    public AbstractResponse(ResponseCodeEnum responseCodeEnum, String message) {
        this.code = responseCodeEnum.getCode();
        this.message = message;
    }

    @JsonIgnore
    public boolean isResponseRight() {
        return code.equals(ResponseCodeEnum.SUCCESS.getCode());
    }
}