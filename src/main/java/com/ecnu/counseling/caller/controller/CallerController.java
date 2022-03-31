package com.ecnu.counseling.caller.controller;

import com.ecnu.counseling.caller.model.dto.CallerDTO;
import com.ecnu.counseling.caller.model.param.CallerEditParam;
import com.ecnu.counseling.caller.model.param.CallerLoginParam;
import com.ecnu.counseling.caller.model.param.CallerRegisterParam;
import com.ecnu.counseling.caller.service.CallerService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.tencentcloudim.util.TencentCloudImUtils;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/counseling/caller")
public class CallerController {

    @Autowired
    private CallerService callerService;
    @Autowired
    private TencentCloudImUtils tencentCloudImUtils;

    @PostMapping("/register")
    public EntityResponse<Integer> register(@RequestBody CallerRegisterParam registerParam) {
        BaseResult checkResult = registerParam.checkRegisterParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<Integer> registerInfo = callerService.register(registerParam);
        if (!registerInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, registerInfo.getMessage(), null);
        }
        // 将账号导入腾讯im
        String userId = registerInfo.getData().toString();
        tencentCloudImUtils.accountImport(userId);
        // 校验账号是否成功导入
        String queryAccountResult = tencentCloudImUtils.queryAccount(Collections.singletonList(userId));
        log.info("校验账号是否成功导入, useId = {}, resultMessage = {}", userId, queryAccountResult);
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, registerInfo.getData());
    }

    @GetMapping("/detail/{id}")
    public EntityResponse<CallerDTO> detail(@PathVariable("id") Integer callerId) {
        ResultInfo<CallerDTO> resultInfo = callerService.detailById(callerId);
        return resultInfo.isRight()
            ? new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, resultInfo.getData())
            : new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, resultInfo.getMessage(), null);
    }

    @PutMapping("/edit")
    public BaseResponse edit(@RequestBody CallerEditParam param) {
        BaseResult checkResult = param.checkEditParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        BaseResult editResult = callerService.edit(param);
        return editResult.isRight()
            ? BaseResponse.success()
            : new BaseResponse(ResponseCodeEnum.FORBIDDEN, editResult.getMessage());
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody CallerLoginParam param) {
        BaseResult checkResult = param.checkLoginParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        ResultInfo<CallerDTO> loginResult = callerService.login(param);
        return loginResult.isRight()
            ? BaseResponse.success()
            : new BaseResponse(ResponseCodeEnum.FORBIDDEN, loginResult.getMessage());
    }


}
