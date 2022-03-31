package com.ecnu.counseling.chat.model.param;

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
public class CreateChatParam {

    /**
     * 访客id
     */
    @NotNull(message = "访客id不可为空")
    private Integer callerId;

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
