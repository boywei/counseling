package com.ecnu.counseling.chat.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.chat.mapper.ChatMapper;
import com.ecnu.counseling.chat.model.dto.ChatDTO;
import com.ecnu.counseling.chat.model.param.ChatRecordListQueryParam;
import com.ecnu.counseling.chat.model.po.ChatPO;
import com.ecnu.counseling.chat.service.ChatService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, ChatPO> implements ChatService {

    @Override
    public ListPagingResponse<ChatDTO> queryRecordList(ChatRecordListQueryParam queryParam) {
        LambdaQueryChainWrapper<ChatPO> queryChainWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
            .eq(ChatPO::getCallerId, queryParam.getCallerId())
            .eq(BasePO::getIsDeleted, 0);
        Integer count = queryChainWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<ChatPO> page = new Page<>(queryParam.getStart() / queryParam.getLength() + 1, queryParam.getLength());
        List<ChatDTO> dtos = queryChainWrapper.page(page).getRecords().stream().map(ChatPO::convert2DTO).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, dtos, queryParam.getStart(), queryParam.getLength(),
            count);
    }

    @Override
    public ChatDTO queryById(Integer chatId) {
        ChatPO po = this.getById(chatId);
        return po == null ? null : po.convert2DTO();
    }
}
