package com.ecnu.counseling.supervisor.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.util.CheckUtils;
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
public class SupervisorQueryParam extends PagingParam {

    private Integer id;

    public BaseResult checkQueryParam() {
        if (CheckUtils.isEmptyId(this.id)) {
            return BaseResult.error("id不能为空或小于0");
        }
        return BaseResult.SUCCESS;
    }

}
