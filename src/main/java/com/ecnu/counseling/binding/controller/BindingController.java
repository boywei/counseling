package com.ecnu.counseling.binding.controller;

import com.ecnu.counseling.binding.model.param.BindingAddParam;
import com.ecnu.counseling.binding.model.param.BindingEditParam;
import com.ecnu.counseling.binding.model.param.BindingQueryParam;
import com.ecnu.counseling.binding.model.po.BindingPO;
import com.ecnu.counseling.binding.service.BindingService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.counselor.model.param.CounselorQueryParam;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/binding")
public class BindingController {

    @Autowired
    private BindingService bindingService;
    @Autowired
    private CounselorService counselorService;
    @Autowired
    private SupervisorService supervisorService;
    
    @PostMapping("/add")
    public EntityResponse<Integer> add(@RequestBody BindingAddParam addParam) {
        BaseResult checkResult = addParam.checkAddParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<CounselorPO> counselorCheckResult = counselorService.detailById(addParam.getCounselorId());
        if(!counselorCheckResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, counselorCheckResult.getMessage(), null);
        }
        ResultInfo<SupervisorDTO> supervisorCheckResult = supervisorService.detailById(addParam.getSupervisorId());
        if(!supervisorCheckResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, supervisorCheckResult.getMessage(), null);
        }

        ResultInfo<Integer> addInfo = bindingService.add(addParam);
        if (!addInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, addInfo.getMessage(), null);
        }
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, addInfo.getData());
    }

    @DeleteMapping("/remove/{id}")
    public BaseResponse remove(@PathVariable("id") Integer bindingId) {
        BaseResult editResult = bindingService.remove(bindingId);
        return editResult.isRight()
                ? BaseResponse.success()
                : new BaseResponse(ResponseCodeEnum.FORBIDDEN, editResult.getMessage());
    }

    @PostMapping("/detail")
    public ListPagingResponse<BindingPO> detail(@RequestBody BindingQueryParam queryParam) {
        BaseResult baseResult = queryParam.checkQueryParam();
        if (!baseResult.isRight()) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), Collections.emptyList(), 0, 0, 0);
        }
        ListPagingResponse<BindingPO> bindingPOSPagingRes = bindingService.detailById(queryParam, queryParam.getStart(), queryParam.getLength());
        List<BindingPO> bindingPOS = bindingPOSPagingRes.getData();
        return CollectionUtils.isEmpty(bindingPOS)? ListPagingResponse.EMPTY_SUCCESS : 
                new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, bindingPOS, 
                        queryParam.getStart(), queryParam.getLength(), bindingPOSPagingRes.getRecordsTotal());
    }

    @PutMapping("/edit")
    public BaseResponse edit(@RequestBody BindingEditParam param) {
        BaseResult checkResult = param.checkEditParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        ResultInfo<CounselorPO> counselorCheckResult = counselorService.detailById(param.getCounselorId());
        if(!counselorCheckResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, counselorCheckResult.getMessage());
        }
        ResultInfo<SupervisorDTO> supervisorCheckResult = supervisorService.detailById(param.getSupervisorId());
        if(!supervisorCheckResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, supervisorCheckResult.getMessage());
        }

        BaseResult editResult = bindingService.edit(param);
        return editResult.isRight()
                ? BaseResponse.success()
                : new BaseResponse(ResponseCodeEnum.FORBIDDEN, editResult.getMessage());
    }

    @PostMapping("/list")
    ListPagingResponse<BindingPO> list(@RequestBody CounselorQueryParam queryParam) {
        BaseResult baseResult = queryParam.checkParam();
        if (!baseResult.isRight()) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), Collections.emptyList(), 0, 0, 0);
        }
        ListPagingResponse<BindingPO> bindingPOSPagingRes = bindingService.list(queryParam.getStart(), queryParam.getLength());
        List<BindingPO> bindingPOS = bindingPOSPagingRes.getData();
        return CollectionUtils.isEmpty(bindingPOS)? ListPagingResponse.EMPTY_SUCCESS :
                new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, bindingPOS,
                        queryParam.getStart(), queryParam.getLength(), bindingPOSPagingRes.getRecordsTotal());
    }

}
