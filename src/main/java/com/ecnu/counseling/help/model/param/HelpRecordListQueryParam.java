package com.ecnu.counseling.help.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 1:03 上午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HelpRecordListQueryParam extends PagingParam {

    private Integer supervisorId;

    public BaseResult checkQueryParam() {
        return supervisorId <= 0
                ? BaseResult.error("督导id非法")
                : super.checkParam();
    }
}
