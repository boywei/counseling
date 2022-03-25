package com.ecnu.counseling.admin.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 5:35 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginParam {

    private String name;

    private String password;

    public BaseResult checkLoginParam() {
        if (StringUtils.isEmpty(name)) {
            return BaseResult.error("名称不可为空");
        }
        return BaseResult.SUCCESS;
    }
}
