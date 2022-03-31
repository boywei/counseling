package com.ecnu.counseling.counselor.service;

import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import java.util.Collection;

public interface CounselorService {

    ResultInfo<CounselorPO> detailById(Integer id);

    ListPagingResponse<CounselorPO> list(Collection<Integer> counselorIds, Integer start, Integer length);
}
