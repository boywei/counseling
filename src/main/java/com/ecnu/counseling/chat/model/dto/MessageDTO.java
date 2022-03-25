package com.ecnu.counseling.chat.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Integer id;

    /**
     * 咨询会话id
     */
    private Integer chatId;

    /**
     * 消息发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送者id
     */
    private Integer senderId;

    /**
     * 接收者id
     */
    private Integer receiverId;

    /**
     * 消息发送者：0访客 1咨询师
     */
    private Integer owner;

    /**
     * 消息类型：0文字 1图片 2语音 3表情 4聊天记录 5无法识别
     */
    private Integer type;

    /**
     * 当消息类型为'聊天记录'时，此项存储关联的咨询会话记录id，其余存0
     */
    private Integer relatedChatId;

    /**
     * 消息内容
     */
    private String content;

}
