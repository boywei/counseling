package com.ecnu.counseling.chat.model.param;

import com.ecnu.counseling.caller.model.param.AccountParam;
import lombok.Data;

@Data
public class MessageSetReadParam {

    /**
     * 设置已读方
     */
    private AccountParam report;

    /**
     * 会话对方
     */
    private AccountParam peer;

}
