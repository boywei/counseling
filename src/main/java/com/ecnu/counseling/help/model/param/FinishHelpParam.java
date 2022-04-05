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
public class FinishHelpParam {

    /**
     * helpId
     */
    @NotNull(message = "helpId不可为空")
    private Integer helpId;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不可为空")
    @Min(value = 1, message = "结束时间必须大于0")
    private Long endTime;

}
