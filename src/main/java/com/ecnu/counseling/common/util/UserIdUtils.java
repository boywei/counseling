package com.ecnu.counseling.common.util;

import com.ecnu.counseling.caller.model.param.AccountParam;
import com.ecnu.counseling.common.constant.UserConstant;
import com.ecnu.counseling.common.model.enumeration.IdentityEnum;

public class UserIdUtils {

    /**
     * 获取caller_IM_id
     *
     * @param id
     * @return
     */
    @Deprecated
    public static String getCallerUseId(Integer id) {
        if (CheckUtils.isEmptyId(id)) {
            throw new IllegalArgumentException("id非法");
        }
        return UserConstant.CALLER_PREFIX + id;
    }

    /**
     * 获取会话记录在redis中的key
     *
     * @param chatId
     * @return
     */
    public static String getChatRecordRedisKey(Integer chatId) {
        return "chatId:" + chatId;
    }

    /**
     * 获取账号在redis中的key
     *
     * @param accountParam
     * @return
     */
    public static String getUserIdRedisKey(AccountParam accountParam) {
        return getUserId(accountParam) + ":exist";
    }

    /**
     * 获取对应身份下的IM账号
     *
     * @param id
     * @param identityEnum
     * @return
     */
    public static String getUserId(Integer id, IdentityEnum identityEnum) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id不合法");
        }
        return identityEnum.getPrefix() + id;
    }

    /**
     * 获取对应身份下的IM账号
     *
     * @param accountParam
     * @return
     */
    public static String getUserId(AccountParam accountParam) {
        Integer accountId = accountParam.getAccountId();
        IdentityEnum identityEnum = IdentityEnum.parseById(accountParam.getAccountIdentity());
        return getUserId(accountId, identityEnum);
    }

}
