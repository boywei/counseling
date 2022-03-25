package com.ecnu.counseling.admin.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.caller.model.dto.CallerDTO;
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

    private String name;

    private String password;

    public CallerDTO convert2DTO() {
        return CallerDTO.builder()
                .id(this.id)
                .name(this.name)
                .password(this.password)
                .build();
    }
}
