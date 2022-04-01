package com.ecnu.counseling.counselor.service;

import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import com.ecnu.counseling.counselor.model.param.CounselorEditParam;
import com.ecnu.counseling.counselor.model.param.CounselorLoginParam;
import com.ecnu.counseling.counselor.model.param.CounselorRegisterParam;

import java.util.Collection;
import java.util.List;

public interface CounselorService {

    ResultInfo<CounselorPO> detailById(Integer id);

    ListPagingResponse<CounselorPO> list(Collection<Integer> counselorIds, Integer start, Integer length);

    ResultInfo<Integer> register(CounselorRegisterParam registerParam);

    BaseResult edit(CounselorEditParam editParam);

    ResultInfo<CounselorDTO> login(CounselorLoginParam loginParam);

}
