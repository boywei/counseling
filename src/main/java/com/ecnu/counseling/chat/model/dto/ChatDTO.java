package com.ecnu.counseling.chat.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 1:07 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private Integer id;

    private Integer callerId;

    private Integer counselorId;

    private Long counselTime;

    private Integer duration;

    /**
     * 0-5
     */
    private Integer score;

    private String commentCaller;

    private String commentCounselor;

}
