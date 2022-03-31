package com.ecnu.counseling.caller.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.caller.model.dto.CallerDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("caller")
public class CallerPO extends BasePO {

    private String name;

    private String phone;

    private String password;

    private String emergencyName;

    private String emergencyPhone;

    public CallerDTO convert2DTO() {
        return CallerDTO.builder()
            .id(this.id)
            .name(this.name)
            .phone(this.phone)
            .emergencyContactName(this.emergencyName)
            .emergencyNumber(this.emergencyPhone)
            .build();
    }
}
