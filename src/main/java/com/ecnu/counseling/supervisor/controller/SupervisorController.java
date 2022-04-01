package com.ecnu.counseling.supervisor.controller;

import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.UserIdUtils;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.model.param.SupervisorEditParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorLoginParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorQueryParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorRegisterParam;
import com.ecnu.counseling.supervisor.model.po.SupervisorPO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import com.ecnu.counseling.tencentcloudim.util.TencentCloudImUtils;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private TencentCloudImUtils tencentCloudImUtils;

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
        if (!registerInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, registerInfo.getMessage(), null);
        }
        // 将账号导入腾讯im
        String userId = UserIdUtils.getSupervisorUserId(registerInfo.getData());
        tencentCloudImUtils.accountImport(userId);
        // 校验账号是否成功导入
        String queryAccountResult = tencentCloudImUtils.queryAccount(Collections.singletonList(userId));
        log.info("校验账号是否成功导入, userId = {}, resultMessage = {}", userId, queryAccountResult);
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, registerInfo.getData());
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
    public EntityResponse<SupervisorDTO> login(@RequestBody SupervisorLoginParam param)  {
        BaseResult checkResult = param.checkLoginParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<SupervisorDTO> loginResult = supervisorService.login(param);
        return loginResult.isRight()
                ? new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, loginResult.getData())
                : new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, loginResult.getMessage(), null);
    }

    
}
