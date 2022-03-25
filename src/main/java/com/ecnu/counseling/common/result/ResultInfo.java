package com.ecnu.counseling.common.result;

public class ResultInfo<T> {

    private Boolean isRight;

    private String message;

    private T data;

    public Boolean isRight() {
        return this.isRight;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public ResultInfo(Boolean isRight, String message, T data) {
        this.isRight = isRight;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultInfo<T> success(T data) {
        return new ResultInfo<>(true, "SUCCESS", data);
    }

    public static <T> ResultInfo<T> success() {
        return new ResultInfo<>(true, "SUCCESS", null);
    }

    public static <T> ResultInfo<T> error(String message) {
        return new ResultInfo<>(false, message, null);
    }

    public static <T> ResultInfo<T> error(String message, T data) {
        return new ResultInfo<>(false, message, data);
    }

}
