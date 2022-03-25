package com.ecnu.counseling.chat.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.chat.model.dto.ChatDTO;
import com.ecnu.counseling.common.model.po.BasePO;
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

    private LocalDateTime counselTime;

    private Integer duration;

    /**
     * 0-5
     */
    private Integer score;

    private String commentCaller;

    private String commentCounselor;

//      `id` int unsigned NOT NULL AUTO_INCREMENT,
//        `caller_id` int NOT NULL,
//        `counselor_id` int NOT NULL,
//        `counsel_time` datetime NOT NULL COMMENT '咨询会话起始时间',
//        `duration` int NOT NULL COMMENT '咨询会话持续时间, 以秒为单位',
//        `score` int DEFAULT NULL COMMENT '0-5',
//        `comment_caller` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '访客对咨询师的评价',
//        `comment_counselor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '咨询师对访客的评价',
//        `is_delete` varchar(255) DEFAULT NULL COMMENT '0未删除1已删除',


    public ChatDTO convert2DTO() {
        return ChatDTO.builder()
            .id(super.id)
            .callerId(this.callerId)
            .counselorId(this.counselorId)
            .counselTime(this.counselTime.toEpochSecond(ZoneOffset.ofHours(8)))
            .duration(this.duration)
            .score(this.score)
            .commentCaller(this.commentCaller)
            .commentCounselor(this.commentCounselor)
            .build();
    }
}
