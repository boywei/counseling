package com.ecnu.counseling.common.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PagingParam {

    public static final PagingParam DEFAULT_PAGING_PARAM = new PagingParam(0, 1000);

    private Integer start;

    private Integer length;

    public BaseResult checkParam() {
        if (this.start < 0 || this.length < 0) {
            return BaseResult.error("分页参数错误");
        }
        return BaseResult.SUCCESS;
    }

}
