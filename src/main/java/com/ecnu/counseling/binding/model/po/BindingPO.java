package com.ecnu.counseling.binding.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.binding.model.dto.BindingDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("binding")
public class BindingPO extends BasePO {

    private Integer counselorId;

    private Integer supervisorId;

    public BindingDTO convert2DTO() {
        return BindingDTO.builder()
                .id(this.id)
                .counselorId(this.counselorId)
                .supervisorId(this.supervisorId)
                .build();
    }
}
