package com.ecnu.counseling.supervisor.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 1:02 上午
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorEditParam extends SupervisorRegisterParam{

    private Integer supervisorId;

    public BaseResult checkEditParam() {
        if (supervisorId <= 0) {
            return BaseResult.error("id非法");
        }
        return super.checkRegisterParam();
    }
}
