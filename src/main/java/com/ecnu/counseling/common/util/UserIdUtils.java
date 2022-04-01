package com.ecnu.counseling.common.util;

import com.ecnu.counseling.common.constant.UserConstant;

public class UserIdUtils {

    /**
     * 获取caller_IM_id
     *
     * @param id
     * @return
     */
    public static String getCallerUseId(Integer id) {
        if (CheckUtils.isEmptyId(id)) {
            throw new IllegalArgumentException("id非法");
        }
        return UserConstant.CALLER_PREFIX + id;
    }

}
