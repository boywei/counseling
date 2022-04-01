package com.ecnu.counseling.counselor.model.param;

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
public class CounselorEditParam extends CounselorRegisterParam{

    private Integer id;

    public BaseResult checkEditParam() {
        if (id == null) {
            return BaseResult.error("未指定id");
        }
        if (id <= 0) {
            return BaseResult.error("id非法");
        }
        return super.checkRegisterParam();
    }
}
