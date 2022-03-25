package com.ecnu.counseling.help.model.dto;

import com.ecnu.counseling.help.model.dto.RecordDTO;
import com.ecnu.counseling.help.model.po.RecordPO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 一条聊天记录
 * @Author wei
 * @Date 2022/3/21 12:00 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelpMessageDetailDTO {

    /**
     * 咨询会话id
     */
    private Integer helpId;

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
     * 消息发送者：0督导 1咨询师
     */
    private Integer owner;

    /**
     * 消息类型：0文字 1图片 2语音 3表情 4聊天记录 5无法识别
     */
    private Integer type;

    /**
     * 聊天记录
     */
    private List<RecordDTO> relatedHelpMessageList;

    /**
     * 消息内容
     */
    private String content;

    public static HelpMessageDetailDTO valueOf(RecordDTO recordDTO, Map<Integer, List<RecordDTO>> relatedRecordMap) {
        if (recordDTO == null) {
            return null;
        }
        return HelpMessageDetailDTO.builder()
                .helpId(recordDTO.getHelpId())
                .sendTime(recordDTO.getSendTime().toEpochSecond(ZoneOffset.ofHours(8)))
                .senderId(recordDTO.getSenderId())
                .receiverId(recordDTO.getReceiverId())
                .owner(recordDTO.getOwner())
                .type(recordDTO.getType())
                .relatedHelpMessageList(relatedRecordMap.getOrDefault(recordDTO.getHelpId(), Collections.emptyList()))
                .content(recordDTO.getContent())
                .build();
    }
}
