package com.ecnu.counseling.admin.controller;

import com.ecnu.counseling.admin.model.dto.AdminDTO;
import com.ecnu.counseling.admin.model.param.AdminLoginParam;
import com.ecnu.counseling.admin.model.param.AdminRegisterParam;
import com.ecnu.counseling.admin.service.AdminService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/19 10:37 下午
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody AdminLoginParam param) {
        BaseResult checkResult = param.checkLoginParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        ResultInfo<AdminDTO> loginResult = adminService.login(param);
        return loginResult.isRight()
                ? BaseResponse.success()
                : new BaseResponse(ResponseCodeEnum.FORBIDDEN, loginResult.getMessage());
    }

    @PostMapping("/register")
    public EntityResponse<Integer> register(@RequestBody AdminRegisterParam registerParam) {
        BaseResult checkResult = registerParam.checkRegisterParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<Integer> registerInfo = adminService.register(registerParam);
        if (!registerInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, registerInfo.getMessage(), null);
        }
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, registerInfo.getData());
    }

}
