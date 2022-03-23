package com.ecnu.counseling.common.response;

public enum ResponseCodeEnum {
    /**
     * 访问成功
     */
    SUCCESS(200),
    /**
     * 未登录
     */
    NOT_LOGIN(401),
    /**
     * 无权限
     */
    UNAUTHORIZED(402),
    /**
     * 禁止访问
     */
    FORBIDDEN(403),
    /**
     * 无法找到
     */
    NOT_FOUND(404),
    /**
     * 发生未知错误
     */
    INTERNAL_SERVER_ERROR(500);

    /**
     * 状态码
     */
    private Integer code;

    ResponseCodeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}