package com.ecnu.counseling.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {

    private Integer id;

    private Integer personId;

    private Integer personType;

    private List<Integer> workday;

}
