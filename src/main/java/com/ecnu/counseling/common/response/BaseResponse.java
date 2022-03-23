package com.ecnu.counseling.common.response;

import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/19 10:52 下午
 */
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
