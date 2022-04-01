package com.ecnu.counseling.caller.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallerDTO {

    private Integer id;

    private String name;

    private String phone;

    private String emergencyContactName;

    private String emergencyNumber;
}
