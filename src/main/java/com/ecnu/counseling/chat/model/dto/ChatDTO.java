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
public class ChatDTO {

    private Integer id;

    private Integer callerId;

    private Integer counselorId;

    private Long startTime;

    private Long endTime;

    /**
     * 0-5
     */
    private Integer score;

    private String commentCaller;

    private String commentCounselor;

}
