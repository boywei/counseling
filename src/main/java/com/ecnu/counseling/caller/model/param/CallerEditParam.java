package com.ecnu.counseling.caller.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CallerEditParam extends CallerRegisterParam{

    private Integer callerId;

    public BaseResult checkEditParam() {
        if (callerId <= 0) {
            return BaseResult.error("id非法");
        }
        return super.checkRegisterParam();
    }
}
