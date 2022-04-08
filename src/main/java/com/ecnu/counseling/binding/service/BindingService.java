package com.ecnu.counseling.binding.service;

import com.ecnu.counseling.binding.model.param.BindingAddParam;
import com.ecnu.counseling.binding.model.param.BindingEditParam;
import com.ecnu.counseling.binding.model.param.BindingQueryParam;
import com.ecnu.counseling.binding.model.po.BindingPO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;

public interface BindingService {

    ResultInfo<Integer> add(BindingAddParam addParam);

    BaseResult remove(Integer bindingId);

    BaseResult edit(BindingEditParam editParam);

    ListPagingResponse<BindingPO> detailById(BindingQueryParam queryParam, Integer start, Integer length);

    ListPagingResponse<BindingPO> list(Integer start, Integer length);

}
