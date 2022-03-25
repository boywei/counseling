package com.ecnu.counseling.counselor.controller;

import com.ecnu.counseling.common.model.param.PagingParam;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import com.ecnu.counseling.counselor.model.param.CounselorQueryParam;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
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

@RestController
@RequestMapping("/api/counseling/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

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


}
