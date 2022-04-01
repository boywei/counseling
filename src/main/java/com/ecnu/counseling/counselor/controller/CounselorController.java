package com.ecnu.counseling.counselor.controller;

import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.BaseResponse;
import com.ecnu.counseling.common.response.EntityResponse;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.UserIdUtils;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import com.ecnu.counseling.counselor.model.param.CounselorEditParam;
import com.ecnu.counseling.counselor.model.param.CounselorLoginParam;
import com.ecnu.counseling.counselor.model.param.CounselorQueryParam;
import com.ecnu.counseling.counselor.model.param.CounselorRegisterParam;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import com.ecnu.counseling.tencentcloudim.util.TencentCloudImUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;
    @Autowired
    private TencentCloudImUtils tencentCloudImUtils;

    @PostMapping("/list")
    ListPagingResponse<CounselorDTO> list(@RequestBody CounselorQueryParam param) {
        BaseResult baseResult = param.checkParam();
        if (!baseResult.isRight()) {
            return new ListPagingResponse<>(ResponseCodeEnum.FORBIDDEN, baseResult.getMessage(), Collections.emptyList(), 0, 0, 0);
        }
        //TODO: 排班表
        Set<Integer> scheduleCounselorIds = Collections.emptySet();
        ListPagingResponse<CounselorPO> counselorPOSPagingRes = counselorService.list(scheduleCounselorIds, param.getStart(), param.getLength());
        List<CounselorPO> counselorPOS = counselorPOSPagingRes.getData();
        if (CollectionUtils.isEmpty(counselorPOS)) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Set<Integer> counselorIds = counselorPOS.stream().map(BasePO::getId).collect(Collectors.toSet());
        //TODO: 查询char 是否咨询过
        return null;
    }

    @PostMapping("/register")
    public EntityResponse<Integer> register(@RequestBody CounselorRegisterParam registerParam) {
        BaseResult checkResult = registerParam.checkRegisterParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<Integer> registerInfo = counselorService.register(registerParam);
        if (!registerInfo.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, registerInfo.getMessage(), null);
        }
        // 将账号导入腾讯im
        String userId = UserIdUtils.getCounselorUserId(registerInfo.getData());
        tencentCloudImUtils.accountImport(userId);
        // 校验账号是否成功导入
        String queryAccountResult = tencentCloudImUtils.queryAccount(Collections.singletonList(userId));
        log.info("校验账号是否成功导入, userId = {}, resultMessage = {}", userId, queryAccountResult);
        return new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, registerInfo.getData());
    }

    @GetMapping("/detail/{id}")
    public EntityResponse<CounselorPO> detail(@PathVariable("id") Integer id) {
        ResultInfo<CounselorPO> resultInfo = counselorService.detailById(id);
        return resultInfo.isRight()
                ? new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, resultInfo.getData())
                : new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, resultInfo.getMessage(), null);
    }

    @PutMapping("/edit")
    public BaseResponse edit(@RequestBody CounselorEditParam param) {
        BaseResult checkResult = param.checkEditParam();
        if (!checkResult.isRight()) {
            return new BaseResponse(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage());
        }
        BaseResult editResult = counselorService.edit(param);
        return editResult.isRight()
                ? BaseResponse.success()
                : new BaseResponse(ResponseCodeEnum.FORBIDDEN, editResult.getMessage());
    }

    @PostMapping("/login")
    public EntityResponse<CounselorDTO> login(@RequestBody CounselorLoginParam param)  {
        BaseResult checkResult = param.checkLoginParam();
        if (!checkResult.isRight()) {
            return new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, checkResult.getMessage(), null);
        }
        ResultInfo<CounselorDTO> loginResult = counselorService.login(param);
        return loginResult.isRight()
                ? new EntityResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, loginResult.getData())
                : new EntityResponse<>(ResponseCodeEnum.FORBIDDEN, loginResult.getMessage(), null);
    }

}
