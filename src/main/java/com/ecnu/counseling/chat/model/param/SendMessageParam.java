package com.ecnu.counseling.chat.model.param;

import com.ecnu.counseling.caller.model.param.AccountParam;
import com.ecnu.counseling.tencentcloudim.enumeration.MessageTypeEnum;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageParam {

    /**
     * 所属会话id
     */
    private Integer chatId;

    /**
     * 是否同步消息
     */
    @Max(value = 2, message = "是否同步消息字段最大不超过2")
    @Min(value = 1, message = "是否同步消息字段最小不小于1")
    private Integer syncOtherMachine;

    /**
     * 发送者
     */
    @NotNull(message = "发送者账号信息不可为空")
    private AccountParam fromUser;

    /**
     * 接收者
     */
    @NotNull(message = "接收者账号信息不可为空")
    private AccountParam toUser;

    /**
     * 消息类型
     * @see MessageTypeEnum
     */
    @NotNull(message = "消息类型不可为空")
    private Integer msgType;

    /**
     * 消息内容
     * TODO：如果是其他类型的话应该是Object 没时间做其他类型消息 暂时只支持文本
     */
    private String msgContent;

}
