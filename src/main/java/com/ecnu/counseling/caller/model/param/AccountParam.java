package com.ecnu.counseling.caller.model.param;

import com.ecnu.counseling.common.model.enumeration.IdentityEnum;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/4/5 1:40 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountParam {

    @NotNull(message = "账号id不可为空")
    @Min(value = 1, message = "账号id最小为1")
    private Integer accountId;

    /**
     * @see IdentityEnum id
     */
    @NotNull(message = "账号身份不可为空")
    @Min(value = 0, message = "账号身份参数最小为1")
    @Max(value = 3, message = "账号身份参数最大为3")
    private Integer accountIdentity;

}
