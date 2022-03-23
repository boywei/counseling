package com.ecnu.counseling.chat.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.chat.mapper.MessageMapper;
import com.ecnu.counseling.chat.model.dto.MessageDTO;
import com.ecnu.counseling.chat.model.param.ChatRecordDetailQueryParam;
import com.ecnu.counseling.chat.model.po.MessagePO;
import com.ecnu.counseling.chat.service.MessageService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 2:23 下午
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessagePO> implements MessageService {


    @Override
    public ListPagingResponse<MessageDTO> queryPaging(ChatRecordDetailQueryParam queryParam) {
        List<Integer> deleteType = Lists.newArrayList(0, queryParam.getQuerierType() == 0 ? 1 : 2);
        LambdaQueryChainWrapper<MessagePO> queryChainWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
            .eq(MessagePO::getChatId, queryParam.getChatId())
            .in(MessagePO::getMessageDeleted, deleteType);
        Integer count = queryChainWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<MessagePO> page = new Page<>(queryParam.getStart() / queryParam.getLength() + 1, queryParam.getLength());
        List<MessageDTO> dtos = queryChainWrapper.page(page).getRecords().stream().map(MessagePO::convert2DTO).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, dtos, queryParam.getStart(), queryParam.getLength(), count);
    }

    @Override
    public List<MessageDTO> queryByChatIds(Collection<Integer> chatIds) {
        if (CollectionUtils.isEmpty(chatIds)) {
            return Collections.emptyList();
        }
        List<MessagePO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
            .in(MessagePO::getChatId, chatIds)
            .list();
        return pos.stream().map(MessagePO::convert2DTO).collect(Collectors.toList());
    }
}
