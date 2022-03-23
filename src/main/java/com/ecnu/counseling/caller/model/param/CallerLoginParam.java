package com.ecnu.counseling.caller.model.param;

import com.ecnu.counseling.common.result.BaseResult;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/20 5:35 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallerLoginParam {

    private String phone;

    private String password;

    public BaseResult checkLoginParam() {
        if (StringUtils.isEmpty(phone)) {
            return BaseResult.error("手机号不可为空");
        }
        String regEx = "/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$/";
        Pattern pattern = Pattern.compile(regEx);
        if (!pattern.matcher(phone).matches()) {
            return BaseResult.error("请输入正确的手机号");
        }
        return BaseResult.SUCCESS;
    }
}
