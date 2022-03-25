package com.ecnu.counseling.help.service;

import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.help.model.param.HelpRecordListQueryParam;
import com.ecnu.counseling.common.response.ListPagingResponse;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 12:53 上午
 */
public interface HelpService {

    ListPagingResponse<HelpDTO> queryRecordList(HelpRecordListQueryParam queryParam);

    HelpDTO queryById(Integer helpId);
}
