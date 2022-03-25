package com.ecnu.counseling.chat.model.dto;

import com.ecnu.counseling.caller.model.dto.CallerDTO;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecordDTO {

    private ChatDTO chat;

    private CallerDTO caller;

    private CounselorDTO counselor;
}


