package com.ecnu.counseling.chat.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.chat.mapper.ChatMapper;
import com.ecnu.counseling.chat.model.dto.ChatDTO;
import com.ecnu.counseling.chat.model.param.AppraiseChatParam;
import com.ecnu.counseling.chat.model.param.ChatRecordListQueryParam;
import com.ecnu.counseling.chat.model.param.CreateChatParam;
import com.ecnu.counseling.chat.model.param.FinishChatParam;
import com.ecnu.counseling.chat.model.po.ChatPO;
import com.ecnu.counseling.chat.service.ChatService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.CheckUtils;
import com.ecnu.counseling.common.util.LocalDateTimeUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, ChatPO> implements ChatService {

    @Override
    public ResultInfo<Integer> create(CreateChatParam createChatParam) {
        if (CheckUtils.anyEmptyIds(createChatParam.getCallerId(), createChatParam.getCounselorId())) {
            return ResultInfo.error("请求参数中存在id不合法");
        }
        Integer count = new LambdaQueryChainWrapper<>(this.baseMapper)
            .eq(ChatPO::getCallerId, createChatParam.getCallerId())
            .eq(ChatPO::getCounselorId, createChatParam.getCounselorId())
            .isNull(ChatPO::getEndTime)
            .count();
        if (count != 0) {
            return ResultInfo.error("已存在未结束的会话, 请勿重复开始会话");
        }
        ChatPO po = ChatPO.builder().callerId(createChatParam.getCallerId())
            .counselorId(createChatParam.getCounselorId())
            .startTime(LocalDateTimeUtils.getDateTimeOfTimestamp(createChatParam.getStartTime()))
            .build();
        this.save(po);
        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult finish(FinishChatParam finishChatParam) {
        if (CheckUtils.isEmptyId(finishChatParam.getChatId())) {
            return BaseResult.error("chatId不合法");
        }
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
            .eq(BasePO::getId, finishChatParam.getChatId())
            .isNull(ChatPO::getEndTime)
            .set(ChatPO::getEndTime, LocalDateTimeUtils.getDateTimeOfTimestamp(finishChatParam.getEndTime()))
            .update();
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该会话记录或者该会话已结束");
    }

    @Override
    public BaseResult update(AppraiseChatParam appraiseChatParam) {
        if (CheckUtils.isEmptyId(appraiseChatParam.getChatId())) {
            return BaseResult.error("chatId不合法");
        }
        ChatPO old = new LambdaQueryChainWrapper<>(this.baseMapper)
            .eq(BasePO::getId, appraiseChatParam.getChatId())
            .isNotNull(ChatPO::getEndTime)
            .last("limit 1")
            .one();
        if (old == null) {
            return BaseResult.error("该记录不存在");
        }
        // 访客调用
        if (appraiseChatParam.getWho() == 0) {
            if (appraiseChatParam.getScore() == null && StringUtils.isEmpty(appraiseChatParam.getCommentCaller())) {
                return BaseResult.error("请输入评分或评价!");
            }
            if (appraiseChatParam.getScore() != null) {
                if (old.getScore() != null) {
                    return BaseResult.error("请勿重复评分!");
                }
                old.setScore(appraiseChatParam.getScore());
            }
            if (StringUtils.isNotEmpty(appraiseChatParam.getCommentCaller())) {
                if (StringUtils.isNotEmpty(old.getCommentCaller())) {
                    return BaseResult.error("请勿重复评价");
                }
                old.setCommentCaller(appraiseChatParam.getCommentCaller());
            }
        }
        // 咨询师调用
        else if (appraiseChatParam.getWho() == 1) {
            if (StringUtils.isEmpty(appraiseChatParam.getCommentCounselor())) {
                return BaseResult.error("请输入评价");
            }
            old.setCommentCounselor(appraiseChatParam.getCommentCounselor());
        } else {
            return BaseResult.error("评价者类型错误");
        }
        this.baseMapper.updateById(old);
        return BaseResult.SUCCESS;
    }

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
