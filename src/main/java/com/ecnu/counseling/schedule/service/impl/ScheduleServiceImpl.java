package com.ecnu.counseling.schedule.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.CheckUtils;
import com.ecnu.counseling.schedule.mapper.ScheduleMapper;
import com.ecnu.counseling.schedule.model.dto.ScheduleDTO;
import com.ecnu.counseling.schedule.model.param.ScheduleAddParam;
import com.ecnu.counseling.schedule.model.param.ScheduleEditParam;
import com.ecnu.counseling.schedule.model.po.SchedulePO;
import com.ecnu.counseling.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:18 下午
 */
@Slf4j
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, SchedulePO> implements ScheduleService {

    @Override
    public ListPagingResponse<ScheduleDTO> list(Integer start, Integer length) {
        LambdaQueryChainWrapper<SchedulePO> queryWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(BasePO::getIsDeleted, 0);
        Integer count = queryWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<SchedulePO> page = new Page<>(start / length + 1, length);
        List<SchedulePO> records = queryWrapper.page(page).getRecords();
        List<ScheduleDTO> result = records.stream().map(SchedulePO::convert2DTO).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, result, start, length, count);
    }

    @Override
    public ResultInfo<Integer> add(ScheduleAddParam addParam) {
        int count = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(SchedulePO::getPersonId, addParam.getPersonId())
                .eq(SchedulePO::getPersonType, addParam.getPersonType())
                .count();
        if (count != 0) {
            return ResultInfo.error("该人员排班表已存在");
        }

        SchedulePO po = SchedulePO.builder()
                .personId(addParam.getPersonId())
                .personType(addParam.getPersonType())
                .workday(addParam.getWorkday().toString())
                .build();
        this.save(po);

        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult edit(ScheduleEditParam editParam) {
        SchedulePO po = SchedulePO.builder()
                .id(editParam.getScheduleId())
                .personId(editParam.getPersonId())
                .personType(editParam.getPersonType())
                .workday(editParam.getWorkday().toString())
                .build();
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(BasePO::getId, editParam.getScheduleId())
                .update(po);
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该条记录");
    }
    
    @Override
    public ResultInfo<ScheduleDTO> detailById(Integer personId, Integer personType) {
        if (CheckUtils.isEmptyId(personId) || (personType != 0 && personType != 1)) {
            return ResultInfo.error("id或类型不合法");
        }
        List<SchedulePO> schedulePOS = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(SchedulePO::getPersonId, personId)
                .eq(SchedulePO::getPersonType, personType)
                .eq(BasePO::getIsDeleted, 0)
                .list();
        if (schedulePOS == null || schedulePOS.size() == 0) {
            return ResultInfo.error("数据不存在");
        }
        return ResultInfo.success(schedulePOS.get(0).convert2DTO());
    }

}
