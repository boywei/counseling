package com.ecnu.counseling.common.response;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseResponse extends AbstractResponse {
    private static final BaseResponse SUCCESS = new BaseResponse(ResponseCodeEnum.SUCCESS, "成功");

    public BaseResponse(ResponseCodeEnum code, String message) {
        super(code, message);
    }

    public static BaseResponse success() {
        return SUCCESS;
    }
}
