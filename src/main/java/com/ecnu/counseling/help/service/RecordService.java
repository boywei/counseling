package com.ecnu.counseling.help.service;

import com.ecnu.counseling.help.model.dto.HelpMessageDetailDTO;
import com.ecnu.counseling.help.model.dto.RecordDTO;
import com.ecnu.counseling.help.model.param.HelpMessageDetailQueryParam;
import com.ecnu.counseling.help.model.po.RecordPO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import java.util.Collection;
import java.util.List;

public interface RecordService {

    ListPagingResponse<RecordDTO> queryPaging(HelpMessageDetailQueryParam queryParam);

    List<RecordDTO> queryByHelpIds(Collection<Integer> helpIds);
}
