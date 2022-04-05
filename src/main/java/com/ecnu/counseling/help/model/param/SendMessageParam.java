package com.ecnu.counseling.help.model.param;

import com.ecnu.counseling.tencentcloudim.enumeration.MessageTypeEnum;
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
     * 所属求助会话id
     */
    private Integer helpId;

    /**
     * 是否同步消息
     */
    private Integer syncOtherMachine;

    /**
     * 发送者
     */
    @NotNull(message = "发送者id不可为空")
    private Integer fromUserId;

    /**
     * 接收者
     */
    @NotNull(message = "接收者id不可为空")
    private Integer toUserId;

    /**
     * 发送者身份：0督导 1咨询师
     */
    @NotNull(message = "发送者身份不可为空")
    private Integer owner;

    /**
     * 消息类型
     * @see MessageTypeEnum
     */
    @NotNull(message = "消息类型不可为空")
    private Integer msgType;

    /**
     * 消息内容
     */
    private String msgContent;

}
