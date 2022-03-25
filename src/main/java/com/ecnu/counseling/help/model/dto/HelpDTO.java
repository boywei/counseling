package com.ecnu.counseling.help.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 1:07 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelpDTO {

    private Integer id;

    private Integer supervisorId;

    private Integer counselorId;

    private Long helpTime;

    private Integer duration;

}
