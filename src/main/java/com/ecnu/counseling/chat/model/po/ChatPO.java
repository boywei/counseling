package com.ecnu.counseling.chat.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.chat.model.dto.ChatDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.util.LocalDateTimeUtils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("chat")
public class ChatPO extends BasePO {

    private Integer callerId;

    private Integer counselorId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * 0-5
     */
    private Integer score;

    private String commentCaller;

    private String commentCounselor;


    public ChatDTO convert2DTO() {
        return ChatDTO.builder()
            .id(super.id)
            .callerId(this.callerId)
            .counselorId(this.counselorId)
            .startTime(LocalDateTimeUtils.getTimeStampMilli(this.startTime))
            .endTime(LocalDateTimeUtils.getTimeStampMilli(this.endTime))
            .score(this.score)
            .commentCaller(this.commentCaller)
            .commentCounselor(this.commentCounselor)
            .build();
    }
}
