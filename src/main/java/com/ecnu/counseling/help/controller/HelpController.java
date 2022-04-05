package com.ecnu.counseling.help.controller;

import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.help.model.dto.HelpMessageDetailDTO;
import com.ecnu.counseling.help.model.dto.HelpRecordDTO;
import com.ecnu.counseling.help.model.dto.RecordDTO;
import com.ecnu.counseling.help.model.param.HelpMessageDetailQueryParam;
import com.ecnu.counseling.help.model.param.HelpRecordListQueryParam;
import com.ecnu.counseling.help.model.param.CreateHelpParam;
import com.ecnu.counseling.help.model.param.FinishHelpParam;
import com.ecnu.counseling.help.model.param.ReceiveMessageParam;
import com.ecnu.counseling.help.model.param.SendMessageParam;
import com.ecnu.counseling.help.service.HelpService;
import com.ecnu.counseling.help.service.RecordService;
import com.ecnu.counseling.common.constant.BaseConstant;
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
import com.ecnu.counseling.tencentcloudim.util.TencentCloudImUtils;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
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
@RequestMapping("/help")
public class HelpController {

    @Autowired
    private HelpService helpService;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private CounselorService counselorService;
    @Autowired
    private RecordService messageService;
    @Autowired
    private TencentCloudImUtils tencentCloudImUtils;

    @PostMapping("/begin")
    EntityResponse<Integer> createHelp(@RequestBody @Valid CreateHelpParam createHelpParam) {
        ResultInfo<SupervisorDTO> supervisorDTOResultInfo = supervisorService.detailById(createHelpParam.getSupervisorId());
        if (!supervisorDTOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, supervisorDTOResultInfo.getMessage(), null);
        }
        ResultInfo<CounselorPO> counselorPOResultInfo = counselorService.detailById(createHelpParam.getCounselorId());
        if (!counselorPOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, counselorPOResultInfo.getMessage(), null);
        }
        ResultInfo<Integer> resultInfo = helpService.create(createHelpParam);
        if (!resultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, resultInfo.getMessage(), null);
        }
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, resultInfo.getData());
    }

    @PutMapping("/finish")
    BaseResponse finishHelp(@RequestBody @Valid FinishHelpParam finishHelpParam) {
        BaseResult finishResult = helpService.finish(finishHelpParam);
        if (!finishResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, finishResult.getMessage());
        }
        return BaseResponse.success();
    }

    @PostMapping("/send")
    EntityResponse<IMSendMessageResponse> sendMessage(@RequestBody @Valid SendMessageParam sendMessageParam) {
        Integer fromUserId = sendMessageParam.getFromUserId();
        Integer toUserId = sendMessageParam.getToUserId();
        // 检查id是否存在，并构造im中id
        Integer owner = sendMessageParam.getOwner();
        String sender, receiver;
        Integer supervisorId, counselorId;
        if(owner == 0) {
            sender = UserIdUtils.getSupervisorUserId(fromUserId);
            receiver = UserIdUtils.getCounselorUserId(toUserId);
            supervisorId = fromUserId;
            counselorId = toUserId;
        } else if (owner == 1) {
            sender = UserIdUtils.getCounselorUserId(fromUserId);
            receiver = UserIdUtils.getSupervisorUserId(toUserId);
            supervisorId = toUserId;
            counselorId = fromUserId;
        } else {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, "未正确指定发送方", null);
        }

        ResultInfo<SupervisorDTO> supervisorDTOResultInfo = supervisorService.detailById(supervisorId);
        if (!supervisorDTOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, supervisorDTOResultInfo.getMessage(), null);
        }
        ResultInfo<CounselorPO> counselorPOResultInfo = counselorService.detailById(counselorId);
        if (!counselorPOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, counselorPOResultInfo.getMessage(), null);
        }

        HelpDTO helpDTO = helpService.queryById(sendMessageParam.getHelpId());
        if (helpDTO == null) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, "会话记录不存在", null);
        }
        try {
            String sendMessageResult = tencentCloudImUtils.sendMsg(Objects.isNull(sendMessageParam.getSyncOtherMachine()) ? 1 : sendMessageParam.getSyncOtherMachine(),
                    sender, receiver, MessageTypeEnum.parseById(sendMessageParam.getMsgType()).getImMessageType(),
                    StringUtils.defaultString(sendMessageParam.getMsgContent()));
            log.info("{} -> {}发送消息结果：{}", fromUserId, toUserId, sendMessageResult);
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

    @PostMapping("/receive")
    EntityResponse<IMReceiveMessageResponse> queryMessage(@RequestBody @Valid ReceiveMessageParam receiveMessageParam) {
        Integer fromUserId = receiveMessageParam.getFromUserId();
        Integer toUserId = receiveMessageParam.getToUserId();
        // 检查id是否存在，并构造im中id
        Integer owner = receiveMessageParam.getOwner();
        String sender, receiver;
        Integer supervisorId, counselorId;
        if(owner == 0) {
            sender = UserIdUtils.getSupervisorUserId(fromUserId);
            receiver = UserIdUtils.getCounselorUserId(toUserId);
            supervisorId = fromUserId;
            counselorId = toUserId;
        } else if (owner == 1) {
            sender = UserIdUtils.getCounselorUserId(fromUserId);
            receiver = UserIdUtils.getSupervisorUserId(toUserId);
            supervisorId = toUserId;
            counselorId = fromUserId;
        } else {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, "未正确指定发送方", null);
        }

        ResultInfo<SupervisorDTO> supervisorDTOResultInfo = supervisorService.detailById(supervisorId);
        if (!supervisorDTOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, supervisorDTOResultInfo.getMessage(), null);
        }
        ResultInfo<CounselorPO> counselorPOResultInfo = counselorService.detailById(counselorId);
        if (!counselorPOResultInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, counselorPOResultInfo.getMessage(), null);
        }

        try {
            String queryMessageResult = tencentCloudImUtils.adminGetRoamMsg(sender, receiver,
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
    ListPagingResponse<HelpRecordDTO> queryRecordList(@RequestBody HelpRecordListQueryParam queryParam) {
        BaseResult baseResult = queryParam.checkQueryParam();
        if (!baseResult.isRight()) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), Collections.emptyList(), 0, 0, 0);
        }
        ListPagingResponse<HelpDTO> helpsPagingResult = helpService.queryRecordList(queryParam);
        List<HelpDTO> helps = helpsPagingResult.getData();
        if (CollectionUtils.isEmpty(helps)) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Set<Integer> supervisorIds = helps.stream().map(HelpDTO::getSupervisorId).collect(Collectors.toSet());
        Set<Integer> counselorIds = helps.stream().map(HelpDTO::getCounselorId).collect(Collectors.toSet());
        ResultInfo<List<SupervisorDTO>> supervisorResultInfo = supervisorService.detailByIds(supervisorIds);
        Map<Integer, SupervisorDTO> supervisorDTOMap = ListUtils.emptyIfNull(supervisorResultInfo.getData()).stream()
                .collect(Collectors.toMap(SupervisorDTO::getId, Function.identity(), (a, b) -> a));
        ListPagingResponse<CounselorPO> counselorPagingResult = counselorService
                .list(counselorIds, PagingParam.DEFAULT_PAGING_PARAM.getStart(), PagingParam.DEFAULT_PAGING_PARAM.getLength());
        Map<Integer, CounselorDTO> counselorDTOMap = ListUtils.emptyIfNull(counselorPagingResult.getData()).stream()
                .collect(Collectors.toMap(BasePO::getId, CounselorPO::convert2DTO, (a, b) -> a));
        List<HelpRecordDTO> helpRecordDTOS = helps.stream()
                .map(e -> HelpRecordDTO.builder().help(e).supervisor(supervisorDTOMap.get(e.getSupervisorId())).counselor(counselorDTOMap.get(e.getCounselorId())).build()).collect(
                        Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, helpRecordDTOS, queryParam.getStart(), queryParam.getLength(),
                helpsPagingResult.getRecordsTotal());
    }

    @GetMapping("/record/detail/{helpId}")
    EntityResponse<HelpRecordDTO> queryMessageDetail(@PathVariable("helpId") @Valid
                                                    @NotNull(message = "helpId不可为空")
                                                    @Min(value = 1, message = "helpId最小不得小于1") Integer helpId) {
        ListPagingResponse<HelpRecordDTO> pagingResponse = this.queryRecordList(HelpRecordListQueryParam.builder().supervisorId(helpId).start(0).length(1).build());
        if (!pagingResponse.isResponseRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, pagingResponse.getMessage(), null);
        }
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, pagingResponse.getData().get(0));
    }

    @PostMapping("/record/message/list")
    ListPagingResponse<HelpMessageDetailDTO> queryRecordMessageList(@RequestBody @Valid HelpMessageDetailQueryParam queryParam) {
        HelpDTO helpDTO = helpService.queryById(queryParam.getHelpId());
        if (helpDTO == null) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, "会话记录不存在", Collections.emptyList(), 0, 0, 0);
        }
        ListPagingResponse<RecordDTO> messagesPagingResult = messageService.queryPaging(queryParam);
        List<RecordDTO> messageDTOS = ListUtils.emptyIfNull(messagesPagingResult.getData());
        Set<Integer> relatedHelpIds = messageDTOS.stream().filter(e -> e.getType() == 4).map(RecordDTO::getRelatedHelpId).collect(Collectors.toSet());
        List<RecordDTO> relatedRecordPOS = messageService.queryByHelpIds(relatedHelpIds);
        Map<Integer, List<RecordDTO>> relatedRecordMap = relatedRecordPOS.stream().collect(Collectors.groupingBy(RecordDTO::getHelpId, Collectors.toList()));
        List<HelpMessageDetailDTO> detailDTOS = relatedRecordPOS.stream().map(e -> HelpMessageDetailDTO.valueOf(e, relatedRecordMap)).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, detailDTOS, queryParam.getStart(), queryParam.getLength(),
                messagesPagingResult.getRecordsTotal());
    }

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }

}
