package com.ecnu.counseling.help.service;

import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.help.model.param.HelpRecordListQueryParam;
import com.ecnu.counseling.help.model.param.CreateHelpParam;
import com.ecnu.counseling.help.model.param.FinishHelpParam;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;

public interface HelpService {

    ResultInfo<Integer> create(CreateHelpParam createHelpParam);

    BaseResult finish(FinishHelpParam finishHelpParam);

    ListPagingResponse<HelpDTO> queryRecordList(HelpRecordListQueryParam queryParam);

    HelpDTO queryById(Integer helpId);
}
