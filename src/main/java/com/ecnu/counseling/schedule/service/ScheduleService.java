package com.ecnu.counseling.schedule.service;

import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.schedule.model.dto.ScheduleDTO;
import com.ecnu.counseling.schedule.model.param.ScheduleAddParam;
import com.ecnu.counseling.schedule.model.param.ScheduleEditParam;
import com.ecnu.counseling.schedule.model.po.SchedulePO;

import java.util.Collection;
import java.util.List;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:02 下午
 */
public interface ScheduleService {

    ListPagingResponse<ScheduleDTO> list(Integer start, Integer length);

    ResultInfo<Integer> add(ScheduleAddParam registerParam);

    BaseResult edit(ScheduleEditParam editParam);

    ResultInfo<ScheduleDTO> detailById(Integer personId, Integer personType);

}
