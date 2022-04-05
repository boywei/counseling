package com.ecnu.counseling.help.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelpDTO {

    private Integer id;

    private Integer supervisorId;

    private Integer counselorId;

    private Long startTime;

    private Long endTime;

}
