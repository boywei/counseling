package com.ecnu.counseling.schedule.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wei
 * @description
 * @date 2022-04-08 18:30
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEditParam extends ScheduleAddParam {

    @NotNull(message = "排班表id不能为空")
    private Integer scheduleId;

    public BaseResult checkEditParam() {
        if (this.scheduleId <= 0) {
            return BaseResult.error("排班表id非法");
        }
        return BaseResult.SUCCESS;
    }

}
