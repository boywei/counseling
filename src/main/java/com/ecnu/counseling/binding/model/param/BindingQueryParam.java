package com.ecnu.counseling.binding.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.util.CheckUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wei
 * @description 根据id查询绑定关系
 * @date 2022-04-08 00:35
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BindingQueryParam extends PagingParam {

    private Integer bindingId;

    private Integer counselorId;

    private Integer supervisorId;

    public BaseResult checkQueryParam() {
        if (CheckUtils.isEmptyId(bindingId) && CheckUtils.isEmptyId(counselorId) && CheckUtils.isEmptyId(supervisorId)) {
            return BaseResult.error("查询参数id不能全为空或全无效");
        }
        return super.checkParam();
    }

}
