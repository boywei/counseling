package com.ecnu.counseling.supervisor.controller;

import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.model.param.SupervisorQueryParam;
import com.ecnu.counseling.supervisor.model.po.SupervisorPO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:01 下午
 */
@RestController
@RequestMapping("/api/counseling/supervisor")
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


}
