package com.ecnu.counseling.schedule.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.util.CheckUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author wei
 * @description
 * @date 2022-04-08 00:35
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleQueryParam extends PagingParam {

    @NotNull(message = "人员id不能为空")
    private Integer personId;

    @NotNull(message = "人员类型不能为空")
    private Integer personType;

    public BaseResult checkQueryParam() {
        if (CheckUtils.isEmptyId(personId)) {
            return BaseResult.error("人员id非法");
        }
        if (personType != 0 && personType != 1) {
            return BaseResult.error("人员类型非法");
        }
        return super.checkParam();
    }

}
