package com.ecnu.counseling.chat.service;

import com.ecnu.counseling.chat.model.dto.ChatDTO;
import com.ecnu.counseling.chat.model.param.ChatRecordListQueryParam;
import com.ecnu.counseling.common.response.ListPagingResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface ChatService {

    ListPagingResponse<ChatDTO> queryRecordList(ChatRecordListQueryParam queryParam);

    ChatDTO queryById(Integer chatId);
}
