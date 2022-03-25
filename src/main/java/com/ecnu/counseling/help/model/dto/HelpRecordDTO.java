package com.ecnu.counseling.help.model.dto;

import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 1:06 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelpRecordDTO {

    private HelpDTO help;

    private SupervisorDTO supervisor;

    private CounselorDTO counselor;
}


