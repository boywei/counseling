package com.ecnu.counseling.chat.model.po;

import com.ecnu.counseling.chat.model.dto.MessageDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePO extends BasePO {

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

    /**
     * 消息是否删除：0都未删除 1访客未删除咨询师删除 2访客删除咨询师未删除 3均删除
     */
    private Integer messageDeleted;

//      `id` int unsigned NOT NULL AUTO_INCREMENT,
//        `chat_id` int NOT NULL,
//        `send_time` datetime NOT NULL COMMENT '信息发送时刻',
//        `sender_id` int NOT NULL,
//        `receiver_id` int NOT NULL,
//        `direction` int NOT NULL COMMENT '1访客向咨询师发送2咨询师向访客发送',
//        `type` int NOT NULL COMMENT '信息类型:1文字2图片3语音4表情5无法识别',
//        `content` varchar(255) NOT NULL,
//  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '0未删除, 1访客未删除咨询师删除, 2访客删除咨询师未删除, 3均删除',


    public MessageDTO convert2DTO() {
        return MessageDTO.builder()
            .id(super.id)
            .chatId(this.chatId)
            .sendTime(this.sendTime)
            .senderId(this.senderId)
            .receiverId(this.receiverId)
            .owner(this.owner)
            .type(this.type)
            .relatedChatId(this.relatedChatId)
            .content(this.content)
            .build();
    }
}
