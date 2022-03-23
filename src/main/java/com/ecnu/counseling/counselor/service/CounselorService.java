package com.ecnu.counseling.counselor.service;

import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import java.util.Collection;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/20 10:02 下午
 */
public interface CounselorService {

    ListPagingResponse<CounselorPO> list(Collection<Integer> counselorIds, Integer start, Integer length);
}
