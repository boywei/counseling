package com.ecnu.counseling.help.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 2:50 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {

    private Integer id;

    /**
     * 求助会话id
     */
    private Integer helpId;

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
     * 消息发送者：0督导 1咨询师
     */
    private Integer owner;

    /**
     * 消息类型：0文字 1图片 2语音 3表情 4聊天记录 5无法识别
     */
    private Integer type;

    /**
     * 当消息类型为'聊天记录'时，此项存储关联的求助会话记录id，其余存0
     */
    private Integer relatedHelpId;

    /**
     * 消息内容
     */
    private String content;

}
