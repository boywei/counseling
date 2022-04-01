package com.ecnu.counseling.admin.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.admin.model.dto.AdminDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/25 10:42 上午
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("admin")
public class AdminPO extends BasePO {

    private String phone;

    private String name;

    private String password;

    public AdminDTO convert2DTO() {
        return AdminDTO.builder()
                .id(this.id)
                .phone(this.phone)
                .name(this.name)
                .build();
    }
}
