package com.ecnu.counseling.schedule.controller;

import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import com.ecnu.counseling.schedule.model.dto.ScheduleDTO;
import com.ecnu.counseling.schedule.model.param.ScheduleAddParam;
import com.ecnu.counseling.schedule.model.param.ScheduleEditParam;
import com.ecnu.counseling.schedule.service.ScheduleService;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:01 下午
 */
@Slf4j
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private CounselorService counselorService;

    @GetMapping("/list")
    ListPagingResponse<ScheduleDTO> list(@RequestParam(required = false, name = "start") Integer start,
                                         @RequestParam(required = false, name = "length") Integer length) {
        PagingParam param = new PagingParam(start, length);
        BaseResult baseResult = param.checkParam();
        if (!baseResult.isRight()) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), Collections.emptyList(), 0, 0, 0);
        }
        return scheduleService.list(param.getStart(), param.getLength());
    }

    @PostMapping("/add")
    public EntityResponse<Integer> add(@RequestBody ScheduleAddParam addParam) {
        BaseResult checkResult = addParam.checkAddParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        BaseResult personResult = personExist(addParam.getPersonId(), addParam.getPersonType());
        if (!personResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<Integer> addInfo = scheduleService.add(addParam);
        if (!addInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, addInfo.getMessage(), null);
        }
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, addInfo.getData());
    }

    @GetMapping("/detail")
    public EntityResponse<ScheduleDTO> detail(@RequestParam("personId") Integer personId, @RequestParam("personType") Integer personType) {
        ResultInfo<ScheduleDTO> resultInfo = scheduleService.detailById(personId, personType);
        return resultInfo.isRight()
                ? new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, resultInfo.getData())
                : new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, resultInfo.getMessage(), null);
    }

    @PutMapping("/edit")
    public BaseResponse edit(@RequestBody ScheduleEditParam param) {
        BaseResult checkResult = param.checkEditParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        BaseResult editResult = scheduleService.edit(param);
        return editResult.isRight()
                ? BaseResponse.success()
                : new BaseResponse(ResponseCodeEnum.FORBIDDEN, editResult.getMessage());
    }

    private BaseResult personExist(Integer personId, Integer personType) {
        if(personType == 0) { // 咨询师
            ResultInfo<CounselorPO> counselorPOResultInfo = counselorService.detailById(personId);
            return counselorPOResultInfo == null ? BaseResult.error("该人员记录不存在") : BaseResult.SUCCESS;
        } else { // 督导
            ResultInfo<SupervisorDTO> supervisorDTOResultInfo = supervisorService.detailById(personId);
            return supervisorDTOResultInfo == null ? BaseResult.error("该人员记录不存在") : BaseResult.SUCCESS;
        }
    }

}
