package com.ecnu.counseling.supervisor.controller;

import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.model.param.SupervisorEditParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorLoginParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorQueryParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorRegisterParam;
import com.ecnu.counseling.supervisor.model.po.SupervisorPO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:01 下午
 */
@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @PostMapping("/list")
    ListPagingResponse<SupervisorDTO> list(@RequestBody SupervisorQueryParam param) {
        BaseResult baseResult = param.checkParam();
        if (!baseResult.isRight()) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), Collections.emptyList(), 0, 0, 0);
        }
        //TODO: 排班表
        Set<Integer> scheduleSupervisorIds = Collections.emptySet();
        ListPagingResponse<SupervisorPO> supervisorPOSPagingRes = supervisorService.list(scheduleSupervisorIds, param.getStart(), param.getLength());
        List<SupervisorPO> supervisorPOS = supervisorPOSPagingRes.getData();
        if (CollectionUtils.isEmpty(supervisorPOS)) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Set<Integer> supervisorIds = supervisorPOS.stream().map(BasePO::getId).collect(Collectors.toSet());

        return null;
    }

    @PostMapping("/register")
    public EntityResponse<Integer> register(@RequestBody SupervisorRegisterParam registerParam) {
        BaseResult checkResult = registerParam.checkRegisterParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<Integer> registerInfo = supervisorService.register(registerParam);
        return registerInfo.isRight()
                ? new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, registerInfo.getData())
                : new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, registerInfo.getMessage(), null);
    }

    @GetMapping("/detail/{id}")
    public EntityResponse<SupervisorDTO> detail(@PathVariable("id") Integer id) {
        ResultInfo<SupervisorDTO> resultInfo = supervisorService.detailById(id);
        return resultInfo.isRight()
                ? new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, resultInfo.getData())
                : new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, resultInfo.getMessage(), null);
    }

    @PutMapping("/edit")
    public BaseResponse edit(@RequestBody SupervisorEditParam param) {
        BaseResult checkResult = param.checkEditParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        BaseResult editResult = supervisorService.edit(param);
        return editResult.isRight()
                ? BaseResponse.success()
                : new BaseResponse(ResponseCodeEnum.FORBIDDEN, editResult.getMessage());
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody SupervisorLoginParam param)  {
        BaseResult checkResult = param.checkLoginParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        ResultInfo<SupervisorDTO> loginResult = supervisorService.login(param);
        return loginResult.isRight()
                ? BaseResponse.success()
                : new BaseResponse(ResponseCodeEnum.FORBIDDEN, loginResult.getMessage());
    }

    
}
