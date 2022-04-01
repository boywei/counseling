package com.ecnu.counseling.admin.model.param;

import com.ecnu.counseling.common.constant.UserConstant;
import com.ecnu.counseling.common.result.BaseResult;
import com.google.common.primitives.Chars;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 5:35 下午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterParam {

    private String name;

    private String phone;

    private String password;

    public BaseResult checkRegisterParam() {

        // name
        BaseResult nameResult = this.checkName(this.name, "管理员");
        if (!nameResult.isRight()) {
            return nameResult;
        }

        // phone
        BaseResult phoneCheckResult = this.checkPhone(this.phone, "管理员");
        if (!phoneCheckResult.isRight()) {
            return phoneCheckResult;
        }

        // password
        if (StringUtils.isEmpty(this.password)) {
            return BaseResult.error("密码不可为空");
        }

        return BaseResult.SUCCESS;
    }

    private BaseResult checkPhone(String phone, String who) {
        if (StringUtils.isEmpty(phone)) {
            return BaseResult.error(who + "手机号不可为空");
        }
        if (!Pattern.compile(UserConstant.MOBILE_PHONE_NUMBER_PATTERN).matcher(phone).matches()) {
            return BaseResult.error("请输入正确的" + who + "手机号");
        }
        return BaseResult.SUCCESS;
    }

    private BaseResult checkName(String name, String who) {
        if (StringUtils.isEmpty(name)) {
            return BaseResult.error(who + "姓名不可为空");
        }
        if (name.length() < UserConstant.NAME_MIN_LENGTH || name.length() > UserConstant.NAME_MAX_LENGTH) {
            return BaseResult.error(String.format(who + "姓名长度应在%d-%d个字符", UserConstant.NAME_MIN_LENGTH, UserConstant.NAME_MAX_LENGTH));
        }
        for (Character c : Chars.asList(UserConstant.LIMIT_CHARS)) {
            if (name.contains(c.toString())) {
                return BaseResult.error(who + "姓名中不能包含特殊字符: " + c);
            }
        }
        return BaseResult.SUCCESS;
    }
}
