package com.ecnu.counseling.help.model.po;

import com.ecnu.counseling.help.model.dto.RecordDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 2:12 下午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RecordPO extends BasePO {

    /**
     * 咨询会话id
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

    /**
     * 消息是否删除：0都未删除 1督导未删除咨询师删除 2督导删除咨询师未删除 3均删除
     */
    private Integer recordDeleted;

    public RecordDTO convert2DTO() {
        return RecordDTO.builder()
                .id(super.id)
                .helpId(this.helpId)
                .sendTime(this.sendTime)
                .senderId(this.senderId)
                .receiverId(this.receiverId)
                .owner(this.owner)
                .type(this.type)
                .relatedHelpId(this.relatedHelpId)
                .content(this.content)
                .build();
    }
}
