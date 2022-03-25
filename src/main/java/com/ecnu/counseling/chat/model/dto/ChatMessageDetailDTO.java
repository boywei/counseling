package com.ecnu.counseling.chat.model.dto;

import com.ecnu.counseling.chat.model.po.MessagePO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDetailDTO {

    /**
     * 咨询会话id
     */
    private Integer chatId;

    /**
     * 消息发送时间
     */
    private Long sendTime;

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
     * 聊天记录
     */
    private List<MessageDTO> relatedChatMessageList;

    /**
     * 消息内容
     */
    private String content;

    public static ChatMessageDetailDTO valueOf(MessageDTO messageDTO, Map<Integer, List<MessageDTO>> relatedMessageMap) {
        if (messageDTO == null) {
            return null;
        }
        return ChatMessageDetailDTO.builder()
            .chatId(messageDTO.getChatId())
            .sendTime(messageDTO.getSendTime().toEpochSecond(ZoneOffset.ofHours(8)))
            .senderId(messageDTO.getSenderId())
            .receiverId(messageDTO.getReceiverId())
            .owner(messageDTO.getOwner())
            .type(messageDTO.getType())
            .relatedChatMessageList(relatedMessageMap.getOrDefault(messageDTO.getChatId(), Collections.emptyList()))
            .content(messageDTO.getContent())
            .build();
    }
}
