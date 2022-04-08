package com.ecnu.counseling.binding.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BindingEditParam extends BindingAddParam {

    private Integer bindingId;

    public BaseResult checkEditParam() {
        if (bindingId <= 0) {
            return BaseResult.error("id非法");
        }
        return super.checkAddParam();
    }
}
