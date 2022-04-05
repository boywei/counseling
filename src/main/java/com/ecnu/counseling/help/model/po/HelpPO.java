package com.ecnu.counseling.help.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.util.LocalDateTimeUtils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("help")
public class HelpPO extends BasePO {

    private Integer supervisorId;

    private Integer counselorId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public HelpDTO convert2DTO() {
        return HelpDTO.builder()
                .id(super.id)
                .supervisorId(this.supervisorId)
                .counselorId(this.counselorId)
                .startTime(LocalDateTimeUtils.getTimeStampMilli(this.startTime))
                .endTime(this.endTime == null ? null : LocalDateTimeUtils.getTimeStampMilli(this.endTime))

                .build();
    }
}
