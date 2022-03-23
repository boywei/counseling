package com.ecnu.counseling.chat.service;

import com.ecnu.counseling.chat.model.dto.ChatMessageDetailDTO;
import com.ecnu.counseling.chat.model.dto.MessageDTO;
import com.ecnu.counseling.chat.model.param.ChatRecordDetailQueryParam;
import com.ecnu.counseling.chat.model.po.MessagePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import java.util.Collection;
import java.util.List;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 2:23 下午
 */
public interface MessageService {

    ListPagingResponse<MessageDTO> queryPaging(ChatRecordDetailQueryParam queryParam);

    List<MessageDTO> queryByChatIds(Collection<Integer> chatIds);
}
