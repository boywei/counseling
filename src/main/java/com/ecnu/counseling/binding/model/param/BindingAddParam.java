package com.ecnu.counseling.binding.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BindingAddParam{

    @NotNull(message = "咨询师id不能为空")
    private Integer counselorId;

    @NotNull(message = "督导id不能为空")
    private Integer supervisorId;

    public BaseResult checkAddParam() {
        if(counselorId <= 0 || supervisorId <= 0) {
            BaseResult.error("id非法");
        }
        return BaseResult.SUCCESS;
    }
}
