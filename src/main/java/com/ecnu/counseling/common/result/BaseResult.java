package com.ecnu.counseling.common.result;

import lombok.AllArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/20 12:58 下午
 */
@AllArgsConstructor
public class BaseResult {

    public static final BaseResult SUCCESS =  new BaseResult(true, "SUCCESS");

    private final Boolean isRight;

    private final String message;

    public Boolean isRight() {
        return this.isRight;
    }

    public String getMessage() {
        return this.message;
    }

    public static BaseResult error(String message) {
        return new BaseResult(false, message);
    }
}
