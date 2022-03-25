package com.ecnu.counseling.help.controller;

import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.help.model.dto.HelpMessageDetailDTO;
import com.ecnu.counseling.help.model.dto.HelpRecordDTO;
import com.ecnu.counseling.help.model.dto.RecordDTO;
import com.ecnu.counseling.help.model.param.HelpMessageDetailQueryParam;
import com.ecnu.counseling.help.model.param.HelpRecordListQueryParam;
import com.ecnu.counseling.help.service.HelpService;
import com.ecnu.counseling.help.service.RecordService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 12:52 上午
 */
@RestController
@RequestMapping("/api/counseling/help")
public class HelpController {

    @Autowired
    private HelpService helpService;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private CounselorService counselorService;
    @Autowired
    private RecordService recordService;

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
    EntityResponse<HelpRecordDTO> queryRecordDetail(@PathVariable("helpId") @Valid
                                                    @NotNull(message = "helpId不可为空")
                                                    @Min(value = 1, message = "helpId最小不得小于1") Integer helpId) {
        ListPagingResponse<HelpRecordDTO> pagingResponse = this.queryRecordList(HelpRecordListQueryParam.builder().supervisorId(helpId).start(0).length(1).build());
        if (!pagingResponse.isResponseRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, pagingResponse.getMessage(), null);
        }
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, pagingResponse.getData().get(0));
    }

    @PostMapping("/record/message/list")
    ListPagingResponse<HelpMessageDetailDTO> queryRecordRecordList(@RequestBody @Valid HelpMessageDetailQueryParam queryParam) {
        HelpDTO helpDTO = helpService.queryById(queryParam.getHelpId());
        if (helpDTO == null) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, "会话记录不存在", Collections.emptyList(), 0, 0, 0);
        }
        ListPagingResponse<RecordDTO> recordsPagingResult = recordService.queryPaging(queryParam);
        List<RecordDTO> recordDTOS = ListUtils.emptyIfNull(recordsPagingResult.getData());
        Set<Integer> relatedHelpIds = recordDTOS.stream().filter(e -> e.getType() == 4).map(RecordDTO::getRelatedHelpId).collect(Collectors.toSet());
        List<RecordDTO> relatedRecordPOS = recordService.queryByHelpIds(relatedHelpIds);
        Map<Integer, List<RecordDTO>> relatedRecordMap = relatedRecordPOS.stream().collect(Collectors.groupingBy(RecordDTO::getHelpId, Collectors.toList()));
        List<HelpMessageDetailDTO> detailDTOS = relatedRecordPOS.stream().map(e -> HelpMessageDetailDTO.valueOf(e, relatedRecordMap)).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, detailDTOS, queryParam.getStart(), queryParam.getLength(),
                recordsPagingResult.getRecordsTotal());
    }

}
