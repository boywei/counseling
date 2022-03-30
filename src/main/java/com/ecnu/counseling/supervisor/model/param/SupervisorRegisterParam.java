package com.ecnu.counseling.supervisor.model.param;

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
 * @Date 2022/3/21 1:02 上午
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorRegisterParam {

    private String name;

    private Integer gender;

    private Integer age;

    private String idCard;

    private String phone;

    private String email;

    private String userName;

    private String password;

    private String workplace;

    private String position;

    /**
     * 在线状态0不在线1空闲2忙碌
     */
    private Integer status;

    private String url;

//    private Integer countHelp;

    private String qualification;

    private String qualificationId;

    public BaseResult checkRegisterParam() {
        // name
        BaseResult nameResult = this.checkName(this.name);
        if (!nameResult.isRight()) {
            return nameResult;
        }

        // phone
        BaseResult phoneCheckResult = this.checkPhone(this.phone);
        if (!phoneCheckResult.isRight()) {
            return phoneCheckResult;
        }

        // password
        if (StringUtils.isEmpty(this.password)) {
            return BaseResult.error("密码不可为空");
        }

        return BaseResult.SUCCESS;
    }

    private BaseResult checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return BaseResult.error("手机号不可为空");
        }
        if (!Pattern.compile(UserConstant.MOBILE_PHONE_NUMBER_PATTERN).matcher(phone).matches()) {
            return BaseResult.error("请输入正确的手机号");
        }
        return BaseResult.SUCCESS;
    }

    private BaseResult checkName(String name) {
        if (StringUtils.isEmpty(name)) {
            return BaseResult.error("姓名不可为空");
        }
        if (name.length() < UserConstant.NAME_MIN_LENGTH || name.length() > UserConstant.NAME_MAX_LENGTH) {
            return BaseResult.error(String.format("姓名长度应在%d-%d个字符", UserConstant.NAME_MIN_LENGTH, UserConstant.NAME_MAX_LENGTH));
        }
        for (Character c : Chars.asList(UserConstant.LIMIT_CHARS)) {
            if (name.contains(c.toString())) {
                return BaseResult.error("名字中不能包含特殊字符: " + c);
            }
        }
        return BaseResult.SUCCESS;
    }
}
