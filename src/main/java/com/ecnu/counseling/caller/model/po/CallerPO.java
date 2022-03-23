package com.ecnu.counseling.caller.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.caller.model.dto.CallerDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/19 10:42 下午
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("caller")
public class CallerPO extends BasePO {

    private String name;

    private String phone;

    private String password;

    private String emergencyContact;

    private String emergencyNumber;

    public CallerDTO convert2DTO() {
        return CallerDTO.builder()
            .id(this.id)
            .name(this.name)
            .phone(this.phone)
            .password(this.password)
            .emergencyContactName(this.emergencyContact)
            .emergencyNumber(this.emergencyNumber)
            .build();
    }
}
