package com.ecnu.counseling.help.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 12:54 上午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("help")
public class HelpPO extends BasePO {

    private Integer supervisorId;

    private Integer counselorId;

    private LocalDateTime helpTime;

    private Integer duration;

    public HelpDTO convert2DTO() {
        return HelpDTO.builder()
                .id(super.id)
                .counselorId(this.supervisorId)
                .supervisorId(this.counselorId)
                .helpTime(this.helpTime.toEpochSecond(ZoneOffset.ofHours(8)))
                .duration(this.duration)
                .build();
    }
}
