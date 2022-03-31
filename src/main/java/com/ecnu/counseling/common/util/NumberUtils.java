package com.ecnu.counseling.common.util;


public class NumberUtils {

    public static Integer ifNullUseZero(Integer n) {
        return n == null ? 0 : n;
    }
}
