package com.ecnu.counseling.help.model.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateHelpParam {

    /**
     * 督导id
     */
    @NotNull(message = "督导id不可为空")
    private Integer supervisorId;

    /**
     * 咨询师id
     */
    @NotNull(message = "咨询师id不可为空")
    private Integer counselorId;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不可为空")
    @Min(value = 1, message = "开始时间必须大于0")
    private Long startTime;

}
