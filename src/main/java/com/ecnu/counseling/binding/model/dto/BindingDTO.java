package com.ecnu.counseling.binding.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BindingDTO {

    private Integer id;

    private Integer counselorId;

    private Integer supervisorId;

}
