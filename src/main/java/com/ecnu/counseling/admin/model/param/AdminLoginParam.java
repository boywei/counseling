package com.ecnu.counseling.admin.model.param;

import com.ecnu.counseling.common.constant.UserConstant;
import com.ecnu.counseling.common.result.BaseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

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

    private String phone;

    private String password;

    public BaseResult checkLoginParam() {
        if (StringUtils.isEmpty(phone)) {
            return BaseResult.error("手机号不可为空");
        }
        Pattern pattern = Pattern.compile(UserConstant.MOBILE_PHONE_NUMBER_PATTERN);
        if (!pattern.matcher(phone).matches()) {
            return BaseResult.error("请输入正确的手机号");
        }
        return BaseResult.SUCCESS;
    }
}
