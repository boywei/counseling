package com.ecnu.counseling.chat.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.ecnu.counseling.caller.model.dto.CallerDTO;
import com.ecnu.counseling.caller.model.param.AccountParam;
import com.ecnu.counseling.caller.service.CallerService;
import com.ecnu.counseling.chat.model.dto.ChatDTO;
import com.ecnu.counseling.chat.model.dto.ChatMessageDetailDTO;
import com.ecnu.counseling.chat.model.dto.ChatRecordDTO;
import com.ecnu.counseling.chat.model.dto.MessageDTO;
import com.ecnu.counseling.chat.model.param.AppraiseChatParam;
import com.ecnu.counseling.chat.model.param.ChatRecordDetailQueryParam;
import com.ecnu.counseling.chat.model.param.ChatRecordListQueryParam;
import com.ecnu.counseling.chat.model.param.CreateChatParam;
import com.ecnu.counseling.chat.model.param.FinishChatParam;
import com.ecnu.counseling.chat.model.param.ReceiveMessageParam;
import com.ecnu.counseling.chat.model.param.SendMessageParam;
import com.ecnu.counseling.chat.service.ChatService;
import com.ecnu.counseling.chat.service.MessageService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.enumeration.IdentityEnum;
import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.JsonUtils;
import com.ecnu.counseling.common.util.NumberUtils;
import com.ecnu.counseling.common.util.UserIdUtils;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import com.ecnu.counseling.tencentcloudim.enumeration.MessageTypeEnum;
import com.ecnu.counseling.tencentcloudim.response.IMReceiveMessageResponse;
import com.ecnu.counseling.tencentcloudim.response.IMSendMessageResponse;
import com.ecnu.counseling.tencentcloudim.util.RedisServiceUtils;
import com.ecnu.counseling.tencentcloudim.util.TencentCloudImUtils;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/counseling/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private CallerService callerService;
    @Autowired
    private CounselorService counselorService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TencentCloudImUtils tencentCloudImUtils;
    @Autowired
    private RedisServiceUtils redisServiceUtils;

    @PostMapping("/begin")
    EntityResponse<Integer> createChat(@RequestBody @Valid CreateChatParam createChatParam) {
        ResultInfo<CallerDTO> callerDTOResultInfo = callerService.detailById(createChatParam.getCallerId());
        if (!callerDTOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, callerDTOResultInfo.getMessage(), null);
        }
        ResultInfo<CounselorPO> counselorPOResultInfo = counselorService.detailById(createChatParam.getCounselorId());
        if (!counselorPOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, counselorPOResultInfo.getMessage(), null);
        }
        ResultInfo<Integer> resultInfo = chatService.create(createChatParam);
        if (!resultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, resultInfo.getMessage(), null);
        }
        // 添加缓存
        redisServiceUtils.set(UserIdUtils.getChatRecordRedisKey(resultInfo.getData()), true);
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, resultInfo.getData());
    }

    @PutMapping("/finish")
    BaseResponse finishChat(@RequestBody @Valid FinishChatParam finishChatParam) {
        BaseResult finishResult = chatService.finish(finishChatParam);
        if (!finishResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, finishResult.getMessage());
        }
        // 删除缓存
        redisServiceUtils.set(UserIdUtils.getChatRecordRedisKey(finishChatParam.getChatId()), false);
        return BaseResponse.success();
    }

    @PutMapping("/appraise")
    BaseResponse appraiseChat(@RequestBody @Valid AppraiseChatParam appraiseChatParam) {
        if (appraiseChatParam.isEmpty()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, "请求参数不可全为空");
        }
        BaseResult updateResult = chatService.update(appraiseChatParam);
        if (!updateResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, updateResult.getMessage());
        }
        return BaseResponse.success();
    }

    @PostMapping("/send")
    EntityResponse<IMSendMessageResponse> sendMessage(@RequestBody @Valid SendMessageParam sendMessageParam) {
        AccountParam fromUser = sendMessageParam.getFromUser();
        AccountParam toUser = sendMessageParam.getToUser();
        BaseResult baseResult = this.accountIdAllExist(fromUser, toUser);
        if (!baseResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), null);
        }
        BaseResult chatExistResult = this.chatRecordExist(sendMessageParam.getChatId());
        if (!chatExistResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, chatExistResult.getMessage(), null);
        }
        try {
            String sendMessageResult = tencentCloudImUtils.sendMsg(Objects.isNull(sendMessageParam.getSyncOtherMachine()) ? 1 : sendMessageParam.getSyncOtherMachine(),
                UserIdUtils.getUserId(fromUser), UserIdUtils.getUserId(toUser), MessageTypeEnum.parseById(sendMessageParam.getMsgType()).getImMessageType(),
                StringUtils.defaultString(sendMessageParam.getMsgContent()));
            log.info("{} -> {}发送消息结果：{}", fromUser.getAccountId(), toUser.getAccountId(), sendMessageResult);
            Optional<IMSendMessageResponse> optional = JsonUtils.readValue(sendMessageResult, IMSendMessageResponse.class);
            if (!optional.isPresent()) {
                log.error("IM发送消息响应参数格式解析错误, sendMessageResult = {}", sendMessageResult);
            }
            IMSendMessageResponse imSendMessageResponse = optional.get();
            if (!imSendMessageResponse.isRight()) {
                return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, imSendMessageResponse.getErrorInfo(), imSendMessageResponse);
            }
            return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, imSendMessageResponse);
        } catch (Exception e) {
            log.error("发送消息失败：", e);
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, e.getMessage(), null);
        }
    }

    /**
     * 双方账号校验
     *
     * @param one
     * @param two
     * @return
     */
    private BaseResult accountIdAllExist(AccountParam one, AccountParam two) {
        BaseResult baseResult = this.accountIdExist(one);
        return baseResult.isRight() ? this.accountIdExist(two) : baseResult;
    }

    /**
     * 检验账号是否存在
     *
     * @param accountParam
     * @return
     */
    private BaseResult accountIdExist(AccountParam accountParam) {
        Integer accountId = accountParam.getAccountId();
        IdentityEnum identityEnum = IdentityEnum.parseById(accountParam.getAccountIdentity());

        // 先检查缓存
        String userIdRedisKey = UserIdUtils.getUserIdRedisKey(accountParam);
        Object redisValue = redisServiceUtils.get(userIdRedisKey);
        // 缓存存在且为true
        if (redisValue != null && BooleanUtils.isTrue((Boolean) redisValue)) {
            return BaseResult.SUCCESS;
        }
        // 缓存存在且为false(已删除)
        if (redisValue != null && BooleanUtils.isFalse((Boolean) redisValue)) {
            return BaseResult.error("账号已删除");
        }

        BaseResult baseResult = BaseResult.error("账号不存在");
        if (identityEnum == IdentityEnum.ADMINISTRATOR) {
            //TODO
        } else if (identityEnum == IdentityEnum.CALLER) {
            baseResult = callerService.allExist(Collections.singleton(accountId));
        } else if (identityEnum == IdentityEnum.COUNSELOR) {
            //TODO
        } else if (identityEnum == IdentityEnum.SUPERVISOR) {
            //TODO
        }

        if (baseResult != null && baseResult.isRight()) {
            // 账号缓存24小时
            redisServiceUtils.setIfAbsent(userIdRedisKey, true, 24L, TimeUnit.HOURS);
            return BaseResult.SUCCESS;
        }
        return BaseResult.error("身份类型错误");
    }

    /**
     * 检验会话记录是否存在
     *
     * @param chatId
     * @return
     */
    private BaseResult chatRecordExist(Integer chatId) {
        String chatRecordRedisKey = UserIdUtils.getChatRecordRedisKey(chatId);
        Object value = redisServiceUtils.get(chatRecordRedisKey);
        // 缓存存在且为true
        if (value != null && BooleanUtils.isTrue((Boolean) value)) {
            return BaseResult.SUCCESS;
        }
        // 缓存存在且为false(已删除)
        if (value != null && BooleanUtils.isFalse((Boolean) value)) {
            return BaseResult.error("会话记录已结束");
        }
        ChatDTO chatDTO = chatService.queryById(chatId);
        if (chatDTO == null) {
            redisServiceUtils.setIfAbsent(chatRecordRedisKey, false, 30L, TimeUnit.SECONDS);
            return BaseResult.error("会话记录不存在");
        }
        // 会话记录存在 缓存30分钟
        redisServiceUtils.setIfAbsent(chatRecordRedisKey, true, 30L, TimeUnit.SECONDS);
        return BaseResult.SUCCESS;
    }

    @PostMapping("/receive")
    EntityResponse<IMReceiveMessageResponse> queryMessage(@RequestBody @Valid ReceiveMessageParam receiveMessageParam) {
        AccountParam fromUser = receiveMessageParam.getFromUser();
        AccountParam toUser = receiveMessageParam.getToUser();
        BaseResult baseResult = this.accountIdAllExist(fromUser, toUser);
        if (!baseResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), null);
        }
        BaseResult chatRecordExist = this.chatRecordExist(receiveMessageParam.getChatId());
        if (!chatRecordExist.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, chatRecordExist.getMessage(), null);
        }
        try {
            String queryMessageResult = tencentCloudImUtils.adminGetRoamMsg(UserIdUtils.getUserId(fromUser), UserIdUtils.getUserId(toUser),
                NumberUtils.ifNullUseZero(receiveMessageParam.getCount()), receiveMessageParam.getStartTime(),
                receiveMessageParam.getEndTime(), receiveMessageParam.getLastMsgKey());
            log.info("接收消息结果：{}", queryMessageResult);
            Optional<IMReceiveMessageResponse> optional = JsonUtils.readValue(queryMessageResult, IMReceiveMessageResponse.class);
            if (!optional.isPresent()) {
                log.error("IM接收消息响应参数格式解析错误, sendMessageResult = {}", queryMessageResult);
            }
            IMReceiveMessageResponse imReceiveMessageResponse = optional.get();
            System.out.println(imReceiveMessageResponse);
            if (!imReceiveMessageResponse.isRight()) {
                return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, imReceiveMessageResponse.getErrorInfo(), null);
            }
            return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, imReceiveMessageResponse);
        } catch (Exception e) {
            log.error("接收消息失败: ", e);
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, e.getMessage(), null);
        }
    }

    @PostMapping("/record/list")
    ListPagingResponse<ChatRecordDTO> queryRecordList(@RequestBody ChatRecordListQueryParam queryParam) {
        BaseResult baseResult = queryParam.checkQueryParam();
        if (!baseResult.isRight()) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), Collections.emptyList(), 0, 0, 0);
        }
        ListPagingResponse<ChatDTO> chatsPagingResult = chatService.queryRecordList(queryParam);
        List<ChatDTO> chats = chatsPagingResult.getData();
        if (CollectionUtils.isEmpty(chats)) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Set<Integer> callerIds = chats.stream().map(ChatDTO::getCallerId).collect(Collectors.toSet());
        Set<Integer> counselorIds = chats.stream().map(ChatDTO::getCounselorId).collect(Collectors.toSet());
        ResultInfo<List<CallerDTO>> callerResultInfo = callerService.detailByIds(callerIds);
        Map<Integer, CallerDTO> callerDTOMap = ListUtils.emptyIfNull(callerResultInfo.getData()).stream()
            .collect(Collectors.toMap(CallerDTO::getId, Function.identity(), (a, b) -> a));
        ListPagingResponse<CounselorPO> counselorPagingResult = counselorService
            .list(counselorIds, PagingParam.DEFAULT_PAGING_PARAM.getStart(), PagingParam.DEFAULT_PAGING_PARAM.getLength());
        Map<Integer, CounselorDTO> counselorDTOMap = ListUtils.emptyIfNull(counselorPagingResult.getData()).stream()
            .collect(Collectors.toMap(BasePO::getId, CounselorPO::convert2DTO, (a, b) -> a));
        List<ChatRecordDTO> chatRecordDTOS = chats.stream()
            .map(e -> ChatRecordDTO.builder().chat(e).caller(callerDTOMap.get(e.getCallerId())).counselor(counselorDTOMap.get(e.getCounselorId())).build()).collect(
                Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, chatRecordDTOS, queryParam.getStart(), queryParam.getLength(),
            chatsPagingResult.getRecordsTotal());
    }

    @GetMapping("/record/detail/{chatId}")
    EntityResponse<ChatRecordDTO> queryRecordDetail(@PathVariable("chatId") @Valid
                                                    @NotNull(message = "chatId不可为空")
                                                    @Min(value = 1, message = "chatId最小不得小于1") Integer chatId) {
        ListPagingResponse<ChatRecordDTO> pagingResponse = this.queryRecordList(ChatRecordListQueryParam.builder().callerId(chatId).start(0).length(1).build());
        if (!pagingResponse.isResponseRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, pagingResponse.getMessage(), null);
        }
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, pagingResponse.getData().get(0));
    }

    @PostMapping("/record/message/list")
    ListPagingResponse<ChatMessageDetailDTO> queryRecordMessageList(@RequestBody @Valid ChatRecordDetailQueryParam queryParam) {
        ChatDTO chatDTO = chatService.queryById(queryParam.getChatId());
        if (chatDTO == null) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, "会话记录不存在", Collections.emptyList(), 0, 0, 0);
        }
        ListPagingResponse<MessageDTO> messagesPagingResult = messageService.queryPaging(queryParam);
        List<MessageDTO> messageDTOS = ListUtils.emptyIfNull(messagesPagingResult.getData());
        Set<Integer> relatedChatIds = messageDTOS.stream().filter(e -> e.getType() == 4).map(MessageDTO::getRelatedChatId).collect(Collectors.toSet());
        List<MessageDTO> relatedMessagePOS = messageService.queryByChatIds(relatedChatIds);
        Map<Integer, List<MessageDTO>> relatedMessageMap = relatedMessagePOS.stream().collect(Collectors.groupingBy(MessageDTO::getChatId, Collectors.toList()));
        List<ChatMessageDetailDTO> detailDTOS = relatedMessagePOS.stream().map(e -> ChatMessageDetailDTO.valueOf(e, relatedMessageMap)).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, detailDTOS, queryParam.getStart(), queryParam.getLength(),
            messagesPagingResult.getRecordsTotal());
    }

}
