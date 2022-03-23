package com.ecnu.counseling.chat.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 1:03 上午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecordListQueryParam extends PagingParam {

    private Integer callerId;

    public BaseResult checkQueryParam() {
        return callerId <= 0
            ? BaseResult.error("访客id非法")
            : super.checkParam();
    }
}
