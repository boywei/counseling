package com.ecnu.counseling.schedule.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.schedule.model.dto.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("schedule")
public class SchedulePO extends BasePO {

    private Integer personId;

    private Integer personType;

    private String workday;

    public ScheduleDTO convert2DTO() {
        return ScheduleDTO.builder()
                .id(super.id)
                .personId(this.personId)
                .personType(this.personType)
                .workday(stringToList(this.workday))
                .build();
    }

    public List<Integer> stringToList(String workdayStr) {
        String[] str = workdayStr.split("[^0-9]");
        List<Integer> days = new ArrayList<>();
        for(String s : str) {
            if(s.matches("[1-7]")) {
                days.add(Integer.parseInt(s));
            }
        }
        return days;
    }

}
