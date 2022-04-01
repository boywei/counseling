package com.ecnu.counseling.supervisor.service;

import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.supervisor.model.param.SupervisorQueryParam;
import com.ecnu.counseling.supervisor.model.po.SupervisorPO;
import java.util.Collection;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.model.param.SupervisorEditParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorLoginParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorRegisterParam;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import java.util.Collection;
import java.util.List;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:02 下午
 */
public interface SupervisorService {

    ListPagingResponse<SupervisorPO> list(Collection<Integer> supervisorIds, Integer start, Integer length);
    
    ResultInfo<Integer> register(SupervisorRegisterParam registerParam);

    BaseResult edit(SupervisorEditParam editParam);

    ResultInfo<SupervisorDTO> login(SupervisorLoginParam loginParam);

    ResultInfo<SupervisorDTO> detailById(Integer id);

    ResultInfo<List<SupervisorDTO>> detailByIds(Collection<Integer> ids);

    BaseResult allExist(Collection<Integer> ids);
}
