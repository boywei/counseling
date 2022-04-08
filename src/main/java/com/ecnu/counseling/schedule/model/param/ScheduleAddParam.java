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
public class ScheduleAddParam {

    @NotNull(message = "人员id不能为空")
    private Integer personId;

    @NotNull(message = "人员类型不能为空")
    private Integer personType;

    @NotNull(message = "排班日不能为空")
    private List<Integer> workday;

    public BaseResult checkAddParam() {
        if (personId <= 0) {
            return BaseResult.error("人员id非法");
        }
        if (personType != 0 && personType != 1) { // 0 counselor, 1 supervisor
            return BaseResult.error("人员类型非法");
        }
        return this.checkWorkdayParam();
    }

    private BaseResult checkWorkdayParam() {
        for (Integer day : this.workday) {
            if(day < 1 || day > 7) {
                return BaseResult.error("工作日中只能为[1-7]内的整数");
            }
        }
        return BaseResult.SUCCESS;
    }

}
