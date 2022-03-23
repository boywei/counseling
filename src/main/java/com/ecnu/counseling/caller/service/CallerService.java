package com.ecnu.counseling.caller.service;

import com.ecnu.counseling.caller.model.dto.CallerDTO;
import com.ecnu.counseling.caller.model.param.CallerEditParam;
import com.ecnu.counseling.caller.model.param.CallerLoginParam;
import com.ecnu.counseling.caller.model.param.CallerRegisterParam;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import java.util.Collection;
import java.util.List;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/19 10:38 下午
 */
public interface CallerService {

    ResultInfo<Integer> register(CallerRegisterParam registerParam);

    BaseResult edit(CallerEditParam editParam);

    ResultInfo<CallerDTO> login(CallerLoginParam loginParam);

    ResultInfo<CallerDTO> detailById(Integer id);

    ResultInfo<List<CallerDTO>> detailByIds(Collection<Integer> ids);
}
