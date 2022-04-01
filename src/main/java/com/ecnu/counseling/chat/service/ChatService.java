package com.ecnu.counseling.chat.service;

import com.ecnu.counseling.chat.model.dto.ChatDTO;
import com.ecnu.counseling.chat.model.param.AppraiseChatParam;
import com.ecnu.counseling.chat.model.param.ChatRecordListQueryParam;
import com.ecnu.counseling.chat.model.param.CreateChatParam;
import com.ecnu.counseling.chat.model.param.FinishChatParam;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;

public interface ChatService {

    ResultInfo<Integer> create(CreateChatParam createChatParam);

    BaseResult finish(FinishChatParam finishChatParam);

    BaseResult update(AppraiseChatParam appraiseChatParam);

    ListPagingResponse<ChatDTO> queryRecordList(ChatRecordListQueryParam queryParam);

    ChatDTO queryById(Integer chatId);
}
